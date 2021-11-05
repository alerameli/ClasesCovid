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

    FloatingActionButton fabAgregar;
    ListaClasesAdapter adapter;
    Usuario usuario;
    RecyclerView rv;
    ArrayList<Clase> listaClases;
    RequestQueue rq;
    JsonRequest jrq;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio_maestro);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        fabAgregar = findViewById(R.id.fab_AC_Agrega);
        rv = findViewById(R.id.rv_PIM);
        rq = Volley.newRequestQueue(getApplicationContext());
        listaClases = new ArrayList<Clase>();
        URL = "https://a217200082.000webhostapp.com/mostrarClasesFuturasMaestro.php?AIDI=" + usuario.getId();

        rv.setLayoutManager(new LinearLayoutManager(this));
        listarClases();

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(getApplicationContext(), AgregarClase.class);
                intento.putExtra("usuario", usuario);
                startActivity(intento);
            }
        });
    }



    public void listarClases(){
        jrq=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray jsonArray = response.optJSONArray("datos");
            for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                JSONObject claseObject = jsonArray.getJSONObject(i);
                int id = claseObject.getInt("clase_id");
                String nombre = claseObject.getString("clase_nombre");
                String lugar = claseObject.getString("clase_lugar");
                String fecha = claseObject.getString("clase_fecha");
                String hora = claseObject.getString("clase_hora");
                String propietario = claseObject.getString("clase_propietario");
                String desc = claseObject.getString("clase_desc");
                String status = claseObject.getString("clase_status");
                String contra = claseObject.getString("clase_contra");
                Clase clase= new Clase(id, nombre, lugar, hora, desc, fecha, contra, status, propietario);
                listaClases.add(clase);
            }
            adapter=new ListaClasesAdapter(usuario,listaClases);
            rv.setAdapter(adapter);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}