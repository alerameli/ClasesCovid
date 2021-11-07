package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Objects;

public class PaginaInicioMaestro extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{

    private FloatingActionButton fabAgregar;
    private ListaClasesAdapter adapter;

    // Se crea la variable tipo Objeto de Usuario
    private Usuario usuario;
    private RecyclerView rv;
    private ArrayList<Clase> listaClases;
    private RequestQueue rq;
    private JsonRequest jrq;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio_maestro);

        // Se obtiene los datos de "Usuario" de la clase anterior "MainActivity"
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        fabAgregar = findViewById(R.id.fab_AC_Agrega);
        rv = findViewById(R.id.rv_PIM);
        rq = Volley.newRequestQueue(getApplicationContext());
        listaClases = new ArrayList<Clase>();

        // Se crea una URL para mostrar las diferentes clases para el "Usuario"
        URL = "https://a217200082.000webhostapp.com/mostrarClasesFuturasMaestro.php?AIDI=" + usuario.getId();

        // Se inicializa el RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Se obtienen las "lista de las clases".
        listarClases();

        // Al accionar el boton flotante, iniciara una clase "AgregarClase"
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(getApplicationContext(), AgregarClase.class);
                intento.putExtra("usuario", usuario);
                startActivity(intento);
            }
        });
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
            adapter = new ListaClasesAdapter(usuario,listaClases);

            // Se añade el adaptador en el RecycleView
            rv.setAdapter(adapter);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}