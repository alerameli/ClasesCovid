package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class GenerarAlerta extends AppCompatActivity {

    private RequestQueue rq;
    private String URL;
    private Usuario usuario;
    private EditText descript;
    private RadioGroup mRadioGroup;

    private String now;
    private int BETWEENDAYS = -5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_alerta);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        mRadioGroup = findViewById(R.id.radioGroup);
        descript = findViewById(R.id.edit_descript);

        findViewById(R.id.btn_accept).setOnClickListener(v -> {

            String result = "Dudoso";

            switch (mRadioGroup.getCheckedRadioButtonId()){
                case R.id.radioP:
                    result = "Positivo";
                    break;
                case R.id.radioD:
                    result = "Dudoso";
                    break;
            }

            URL = "https://a217200082.000webhostapp.com/agregarAlerta.php?" +
                    "USER=" + usuario.getId() + "&" +
                    "RESULT=" + result + "&" +
                    "DESCRIPT=" + descript.getText() + "&" +
                    "BEFORE5DAYS=" + getDate() + "&" +
                    "TODAY=" + now + "&" +
                    "TYPE=" + usuario.getTipo();

            generarAlerta();

        });

        findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            Intent intento_alert = null;
            if(usuario.getTipo().equals("Alumno"))
                intento_alert = new Intent(getApplicationContext(),PaginaInicioAlumno.class);
            else
                intento_alert = new Intent(getApplicationContext(),PaginaInicioMaestro.class);
            intento_alert.putExtra("usuario",usuario);
            startActivity(intento_alert);
        });

        rq = Volley.newRequestQueue(getApplicationContext());

    }

    private void generarAlerta() {
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET,URL,null,
                response ->{
                    try{
                        if(Integer.parseInt(String.valueOf(response.getInt("dato"))) != 1){
                            Toast.makeText(getApplicationContext(), "* Se ha generado la alerta *", Toast.LENGTH_SHORT).show();
                            Intent intento_alert = null;
                            if(usuario.getTipo().equals("Alumno"))
                                intento_alert = new Intent(getApplicationContext(),PaginaInicioAlumno.class);
                            else
                                intento_alert = new Intent(getApplicationContext(),PaginaInicioMaestro.class);
                            intento_alert.putExtra("usuario",usuario);
                            startActivity(intento_alert);
                        }else
                            Toast.makeText(getApplicationContext(), "* No se genero la alerta *", Toast.LENGTH_SHORT).show();
                    }catch (JSONException error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                } ,error -> {
            Toast.makeText(getApplicationContext(), "No se pudo conectar", Toast.LENGTH_SHORT).show();
        });
        rq.add(request);
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        c.add(Calendar.DATE, BETWEENDAYS);
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
    }

}