package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class VerInfoAlumno extends AppCompatActivity {

    Usuario usuario;
    RequestQueue rq;
    TextView tv_titulo,tv_nombre,tv_celular,tv_correo;
    RecyclerView rv;
    String nombre,correo,celular;
    private ListaClasesAdapter adapter;
    private ArrayList<Clase> listaClases;
    int compaID;
    private String now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_alumno);

        tv_titulo=findViewById(R.id.tv_UI_titulo);
        tv_nombre=findViewById(R.id.tv_IU_nombre);
        tv_celular=findViewById(R.id.tv_IU_Celular);
        tv_correo=findViewById(R.id.tv_IU_correo);
        rv=findViewById(R.id.rv_UI_Clases);
        listaClases = new ArrayList<>();

        usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
        Toast.makeText(getApplicationContext(), usuario.getNombres(), Toast.LENGTH_SHORT).show();

        rq = Volley.newRequestQueue(getApplicationContext());
        int aidi = getIntent().getExtras().getInt("ID");

        obtenerDatos(aidi);

        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rv.setClickable(false);
    }

    private void obtenerDatos(int id){
        String URL="https://a217200082.000webhostapp.com/infoUsuario.php?AIDI="+id;
        JsonObjectRequest solicitud=new JsonObjectRequest(Request.Method.GET,URL,null,
                response -> {
                    try {
                        JSONArray jsonArray = response.optJSONArray("datos");
                        JSONObject UserObject = jsonArray.getJSONObject(0);
                        compaID=UserObject.getInt("usuario_id");
                        nombre = UserObject.getString("usuario_nombres")+" "+UserObject.getString("usuario_apellidos");
                        celular = UserObject.getString("usuario_celular");
                        correo= UserObject.getString("usuario_correo");
                        tv_nombre.setText(nombre);
                        tv_celular.setText(celular);
                        tv_correo.setText(correo);
                        mostrarClases();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al cargar la informacion", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error de conexion", Toast.LENGTH_SHORT).show();
                });
        rq.add(solicitud);
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        c.add(Calendar.DATE, -5);
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
    }

    private void mostrarClases(){
        String URL="https://a217200082.000webhostapp.com/mostrarClasesIguales_R.php?" +
                   "User=" + usuario.getId() + "&" +
                   "Compa="+compaID + "&" +
                   "BEFORE5DAYS=" + getDate() + "&" +
                   "TODAY=" + now;
        JsonObjectRequest solicitud=new JsonObjectRequest(Request.Method.GET,URL,null,
                response -> {
                    try {
                        JSONArray jsonArray = response.optJSONArray("datos");
                        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                            JSONObject claseObject = jsonArray.getJSONObject(i);

                            Clase clase = new Clase(
                                    claseObject.getInt("clase_id"),
                                    claseObject.getString("clase_nombre"),
                                    claseObject.getString("clase_lugar"),
                                    claseObject.getString("clase_hora"),
                                    claseObject.getString("clase_desc"),
                                    claseObject.getString("clase_fecha"),
                                    "",
                                    claseObject.getString("clase_status"),
                                    claseObject.getString("usuario_nombres") + " " + claseObject.getString("usuario_apellidos")
                            );
                            listaClases.add(clase);
                        }
                        adapter = new ListaClasesAdapter(usuario, listaClases,this);
                        rv.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"No se encontraron clases por mostrar", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        rq.add(solicitud);
    }
}