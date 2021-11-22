package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class VerAlumnosInfectados extends AppCompatActivity {

    private ListaUsuariosAdapter adapter;
    private RecyclerView rv;
    private RequestQueue rq;
    private ArrayList<Usuario> listaUsuarios;
    private String URL;
    private Usuario usuario;

    private String now;
    private int BETWEENDAYS = -5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_alumnos_infectados);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Toast.makeText(getApplicationContext(), usuario.getNombres(), Toast.LENGTH_SHORT).show();

        rv= findViewById(R.id.rv_AlumnosInfectados);
        rq = Volley.newRequestQueue(getApplicationContext());

        listaUsuarios=new ArrayList<Usuario>();

        URL="https://a217200082.000webhostapp.com/mostrarUsuariosInfectados.php?AIDI=" +
                usuario.getId() + "&" +
                "BEFORE5DAYS=" + getDate() + "&" +
                "TODAY=" + now;
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mostrarUsuarios();
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        c.add(Calendar.DATE, BETWEENDAYS);
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
    }

    private void mostrarUsuarios(){
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET,URL,null,
                response ->{
            try{
                JSONArray jsonArray = response.optJSONArray("datos");
                for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                    JSONObject usuarioObject = jsonArray.getJSONObject(i);
                    if(usuarioObject.optInt("usuario_id") != 0){
                        Usuario usuario = new Usuario();
                        usuario.setId(usuarioObject.optInt("usuario_id"));
                        usuario.setNombres(usuarioObject.optString("usuario_nombres"));
                        usuario.setApellidos(usuarioObject.optString("usuario_apellidos"));
                        usuario.setCorreo(usuarioObject.optString("usuario_correo"));
                        usuario.setCelular(usuarioObject.optString("usuario_celular"));
                        usuario.setUsuario(usuarioObject.optString("usuario_usuario"));
                        usuario.setContraseña(usuarioObject.optString("usuario_contraseña"));
                        usuario.setTipo(usuarioObject.optString("usuario_tipo"));
                        listaUsuarios.add(usuario);
                    }
                }
                adapter=new ListaUsuariosAdapter(usuario,listaUsuarios);
                rv.setAdapter(adapter);
            }catch (JSONException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
                } ,error -> {
            Toast.makeText(getApplicationContext(), "No se pudo conectar", Toast.LENGTH_SHORT).show();
        });
        rq.add(request);
    }
}