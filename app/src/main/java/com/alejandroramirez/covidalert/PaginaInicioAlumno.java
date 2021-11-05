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

    Usuario usuario;
    RecyclerView rv;
    RequestQueue rq;
    JsonRequest jrq;

    ListaClasesAdapter adapter;
    ArrayList<Clase> listaClases;

    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio_alumno);

        rv = findViewById(R.id.rv_PIA);
        rq = Volley.newRequestQueue(getApplicationContext());
        listaClases = new ArrayList<Clase>();

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        URL = "https://a217200082.000webhostapp.com/mostrarClasesA.php?AIDI=" + usuario.getId();
        rv.setLayoutManager(new LinearLayoutManager(this));
        listarClases();

    }

    public void listarClases() {
        jrq = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
        rq.add(jrq);
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
                Clase clase = new Clase(id, nombre, lugar, hora, desc, fecha, contra, status, propietario);
                listaClases.add(clase);
            }
            adapter = new ListaClasesAdapter(usuario,listaClases);
            rv.setAdapter(adapter);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acciones_alumno, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ClasesDisponibles:
                break;
            case R.id.MisProximasClases:
                Intent intento=new Intent(getApplicationContext(),PaginaClasesFuturasAlumno.class);
                intento.putExtra("usuario",usuario);
                startActivity(intento);
                break;
            case R.id.MisClasesPasadas:
                Intent intento2=new Intent(getApplicationContext(),PaginaClasesPasasdasAlumno.class);
                intento2.putExtra("usuario",usuario);
                startActivity(intento2);
                break;
            case R.id.GenerarAlerta:
                break;
        }
        return true;
    }

}