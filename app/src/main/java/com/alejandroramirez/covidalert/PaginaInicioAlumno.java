package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Objects;

public class PaginaInicioAlumno extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    // Se crea la variable tipo Objeto de Usuario
    private Usuario usuario;
    private RecyclerView rv;
    private RequestQueue rq;
    private JsonRequest jrq;

    private ListaClasesAdapter adapter;
    private ArrayList<Clase> listaClases;

    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio_alumno);

        rv = findViewById(R.id.rv_PIA);
        rq = Volley.newRequestQueue(getApplicationContext());
        listaClases = new ArrayList<Clase>();

        // Se obtiene los datos de "Usuario" de la clase anterior "MainActivity"
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        // Se crea una URL para mostrar las diferentes clases para el "Usuario"
        URL = "https://a217200082.000webhostapp.com/mostrarClasesDisponiblesAlumno.php?AIDI=" + usuario.getId();

        // Se inicializa el RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Se obtienen las "lista de las clases".
        listarClases();

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
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // Error en la obtencion de la url
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.MisProximasClases:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesFuturasAlumno.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                break;
            case R.id.MisClasesPasadas:
                URL = "https://a217200082.000webhostapp.com/mostrarClasesPasadasAlumno.php?AIDI=" + usuario.getId();
                listaClases.clear();
                listarClases();
                break;
            case R.id.GenerarAlerta:
                break;
        }
        return true;
    }

}