package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class PaginaInicioMaestro extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{

    private FloatingActionButton fabAgregar;
    private ListaClasesAdapter adapter;

    // Se crea la variable tipo Objeto de Usuario
    private Usuario usuario;
    private RecyclerView rv;
    private RequestQueue rq;
    private JsonRequest jrq;
    private TextView titulo;

    private final static String CHANNEL_ID = "NOTIFICACION";
    private static final int NOTIFICACION_ID = 11636;
    private int countUserInfected = 0;
    private ArrayList<Clase> listaClases = new ArrayList<>();

    private String URL;
    private String now;
    private int BETWEENDAYS = -5;
    private Menu mMenu;
    private FragmentActivity fragmentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio_maestro);

        // Se obtiene los datos de "Usuario" de la clase anterior "MainActivity"
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        fabAgregar = findViewById(R.id.fab_AC_Agrega);
        fragmentActivity = this;
        rv = findViewById(R.id.rv_PIM);
        rq = Volley.newRequestQueue(getApplicationContext());
        listaClases = new ArrayList<>();

        // Se inicializa el RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));

        jsonQueryImInfected();

    }

    private void onDisabled(){
        rv.setVisibility(View.INVISIBLE);

        int i = 0;
        while (i < mMenu.size()){
            mMenu.getItem(i).setVisible(false);
            i++;
        }
        mMenu.getItem(mMenu.size()-1).setVisible(true);
        mMenu.getItem(mMenu.size()-2).setVisible(true);
    }

    private void onEnabled(){
        rv.setVisibility(View.VISIBLE);

        int i = 0;
        while (i < mMenu.size()){
            mMenu.getItem(i).setVisible(true);
            i++;
        }
        mMenu.getItem(mMenu.size()-2).setVisible(false);
    }

    private void jsonQueryImInfected() {

        String URL = "https://a217200082.000webhostapp.com/mostrarEstoyInfectado.php?" +
                "USER=" + usuario.getId() + "&" +
                "BEFORE5DAYS=" + getDate() + "&" +
                "TODAY=" + now;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    try {
                        if(Integer.parseInt(String.valueOf(response.getInt("dato"))) != 1){
                            Toast.makeText(getApplicationContext(), "* Estas enfermo, no podras acceder al menu *", Toast.LENGTH_SHORT).show();
                            onDisabled();
                        }else{
                            // Se crea una URL para mostrar las diferentes clases para el "Usuario"
                            this.URL = "https://a217200082.000webhostapp.com/mostrarClasesFuturasMaestro.php?AIDI=" + usuario.getId();

                            // Se obtienen las "lista de las clases".
                            listarClases();

                            // Al accionar el boton flotante, iniciara una clase "AgregarClase"
                            fabAgregar.setOnClickListener(view -> {
                                Intent intento = new Intent(getApplicationContext(), AgregarClase.class);
                                intento.putExtra("Edicion","agregar");
                                intento.putExtra("usuario", usuario);
                                startActivity(intento);
                            });
                            jsonQuery();
                            onEnabled();
                            Toast.makeText(getApplicationContext(), "* Bienvenido *", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        //Toast.makeText(mContext,"No se encontraron clases por mostrar", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Ha ocurrido un error de conexion", Toast.LENGTH_SHORT).show());

        rq.add(jsonObjectRequest);

    }

    // Metodo para la obtención de las "Listas de las clases" del Usuario
    public void listarClases(){
        jrq=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        rq.add(jrq);
    }

    // Error en la obtencion de la url
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
            adapter = new ListaClasesAdapter(usuario,listaClases, this);

            // Se añade el adaptador en el RecycleView
            rv.setAdapter(adapter);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Menu para las distintas acciones que puede realizar el usuario
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acciones_maestro, mMenu);
        return true;
    }

    // Acciones que realiza el usuario al seleccionar un item del menu.
    // Al accionar algun item, se inicializa una clase.
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MisClasesFuturasM:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesFuturasMaestro.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                break;
            case R.id.MisClasesPasadasM:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesPasadasMaestro.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                break;
            case R.id.GenerarAlertaM:
                Intent intento_alert = new Intent(getApplicationContext(), GenerarAlerta.class);
                intento_alert.putExtra("usuario", usuario);
                startActivity(intento_alert);
                break;
            case R.id.LiberarAlertaM:
                AlertDialog.Builder alert = new AlertDialog.Builder(fragmentActivity);
                alert.setMessage("Al eliminar tus alertas, podras acceder al menu para volver a inscribirte libremente a tus materias.\n\n¿Desea eliminar tus alertas?").setCancelable(true)
                        .setPositiveButton("Aceptar", ((dialog, which) -> {
                            jsonQueryLiberarAlertas();
                            dialog.dismiss();
                        }))
                        .setNegativeButton("Cancelar", ((dialog, which) -> dialog.cancel()));

                AlertDialog title = alert.create();
                title.setTitle("Liberar Alertas");
                title.show();
                break;
            case R.id.cerrarSesionM:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "* Cerrar sesion *", Toast.LENGTH_SHORT);
                finish();
                break;
        }
        return true;
    }

    private void jsonQueryLiberarAlertas(){
        String URL = "https://a217200082.000webhostapp.com/eliminarAlertas.php?" +
                "USER=" + usuario.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    try {
                        if(Integer.parseInt(String.valueOf(response.getInt("dato"))) != 1)
                            Toast.makeText(getApplicationContext(), "* Se han eliminado con exito *", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "* No eliminaron las alertas *", Toast.LENGTH_SHORT).show();

                        jsonQueryImInfected();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Ha ocurrido un error de conexion", Toast.LENGTH_SHORT).show());

        rq.add(jsonObjectRequest);
    }

    private void jsonQuery(){
        String URL = "https://a217200082.000webhostapp.com/mostrarAlertasClasesInfected_teacher.php?" +
                "BEFORE5DAYS=" + getDate() + "&" +
                "TODAY=" + now + "&" +
                "IDUsuario=" + usuario.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    try {
                        JSONArray jsonArray = response.optJSONArray("datos");

                        countUserInfected = Objects.requireNonNull(jsonArray).getInt(0);

                        if (countUserInfected > 0){
                            createNotificationChannel();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Ha ocurrido un error de conexion", Toast.LENGTH_SHORT).show());

        rq.add(jsonObjectRequest);
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        c.add(Calendar.DATE, BETWEENDAYS);
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
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

    @SuppressLint("ResourceAsColor")
    private void createNotification() {
        if(countUserInfected > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Aviso de contagio");
            builder.setAutoCancel(true);
            builder.setContentText("Hay " + countUserInfected + " estudiante(s) positivo.");
            builder.setColor(R.color.background_green);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setLights(Color.CYAN, 1000, 1000);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setDefaults(Notification.DEFAULT_SOUND);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
        }
    }
}