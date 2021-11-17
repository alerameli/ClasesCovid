package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PaginaInicioAlumno extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    // Se crea la variable tipo Objeto de Usuario
    private Usuario usuario;
    private RecyclerView rv;
    private RequestQueue rq;
    private JsonRequest jrq;
    private TextView titulo;

    private ListaClasesAdapter adapter;
    private ArrayList<Clase> listaClases;
    private PendingIntent mPendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private static final int NOTIFICACION_ID = 11636;
    private ArrayList<Usuario> AlertaListaUsuario;
    private ArrayList<Clase> miListaClases;
    private ArrayList<ArrayList<String>> ListaAlerta;
    private String date;

    private ArrayList<Clase> AlertaListaClases;

    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio_alumno);

        titulo=findViewById(R.id.textView2);
        titulo.setText("Materias disponibles");

        rv = findViewById(R.id.rv_PIA);
        rq = Volley.newRequestQueue(getApplicationContext());
        listaClases = new ArrayList<Clase>();

        // Se obtiene los datos de "Usuario" de la clase anterior "MainActivity"
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        // Se crea una URL para mostrar las diferentes clases para el "Usuario"
        URL = "https://a217200082.000webhostapp.com/mostrarClasesDisponiblesAlumno.php?AIDI="+usuario.getId();

        // Se inicializa el RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        // Se obtienen las "lista de las clases".
        listarClases();

        busquedaAlertas();

    }

    private void busquedaAlertas() {
        BusquedaAlertas busquedaAlertas = new BusquedaAlertas(getApplicationContext(), usuario.getId());
        busquedaAlertas.setValorBusqueda(0);
        busquedaAlertas.getAlertaUsuario();
        //busquedaAlertas.setValorBusqueda(1);
        //busquedaAlertas.getAlertaClases();
        date = busquedaAlertas.getDate();
        if(busquedaAlertas.getAlerta()){
            AlertaListaUsuario = busquedaAlertas.getListaUsuarios();
            //miListaClases = busquedaAlertas.getListaClases();
            ListaAlerta = busquedaAlertas.getListaAlerta();
            createNotificationChannel();
        }
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        createNotification();

    }

    private int getCantidadClasesSimilares(){
        int count = 0;
        //for (int i = 0; i < miListaClases.size(); i++){
        //    for (int o = 0; o < ListaAlerta.size(); o++){
        //        if(miListaClases.get(i).getId() == Integer.parseInt(ListaAlerta.get(o).get(0))){
        //            AlertaListaClases.add(miListaClases.get(i));
        //            count++;
        //        }
        //    }
        //}

        return count;
    }

    @SuppressLint("ResourceAsColor")
    private void createNotification() {
        int count;
        if(ListaAlerta == null)
            count = 0;
        else
            count = getCantidadClasesSimilares();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Aviso de contagio");
        builder.setAutoCancel(true);
        builder.setContentText("Hay "+count+" de tus clases con almenos un estudiante positivo.");
        builder.setColor(R.color.background_green);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.CYAN, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
    }

    // Metodo para la obtención de las "Listas de las clases" del Usuario
    public void listarClases() {
        jrq = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
        rq.add(jrq);
    }

    // Se obtienen los datos de la base de datos y llenarlos con el objeto "Clase"
    // Una vez obtenido los datos de una clase se añade al listado de clases "listaClases".
    // Una vez termido la "ListaClases" se añade al adaptador del "RecycleView"
    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray jsonArray = response.optJSONArray("datos");
            for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                // Se obtiene la clase del Array de la base de datos
                JSONObject claseObject = jsonArray.getJSONObject(i);

                // Se crea un Objeto tipo "Clase", añadiendo los datos del mismo
                Clase clase = new Clase(
                        claseObject.getInt("clase_id"),
                        claseObject.getString("clase_nombre"),
                        claseObject.getString("clase_lugar"),
                        claseObject.getString("clase_hora"),
                        claseObject.getString("clase_desc"),
                        claseObject.getString("clase_fecha"),
                        claseObject.getString("clase_contra"),
                        claseObject.getString("clase_status"),
                        claseObject.getString("clase_propietario")
                );

                // Se agregan al listado de clases
                listaClases.add(clase);
            }

            // Se crea un adaptador para el RecycleView
            adapter = new ListaClasesAdapter(usuario, listaClases);

            // Se añade el adaptador en el RecycleView
            rv.setAdapter(adapter);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"No se encontraron clases por mostrar", Toast.LENGTH_SHORT).show();
        }
    }

    // Error en la obtencion de la url
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "Ha ocurrido un error de conexion", Toast.LENGTH_SHORT).show();
    }

    // Menu para las distintas acciones que puede realizar el usuario
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acciones_alumno, menu);
        return true;
    }

    // Acciones que realiza el usuario al seleccionar un item del menu.
    // Al accionar algun item, se rellena el recyclerView con la informacion correcta.
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ClasesDisponibles:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesDisponiblesAlumno.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                titulo.setText("Materias disponibles");
                break;
            case R.id.MisProximasClases:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesFuturasAlumno.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                titulo.setText("Mis proximas clases");
                break;
            case R.id.MisClasesPasadas:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesPasadasAlumno.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                titulo.setText("Mis clases pasadas");
                break;
            case R.id.VerAlumnosInfectados:
                Intent intento=new Intent(getApplicationContext(),VerAlumnosInfectados.class);
                intento.putExtra("usuario",usuario);
                startActivity(intento);
                break;
            case R.id.GenerarAlerta:
                break;
        }
        return true;
    }

}