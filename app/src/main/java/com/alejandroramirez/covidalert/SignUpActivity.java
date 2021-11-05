package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Spinner spinner;
    Button btnRegistrarse;
    EditText tfNombres, tfApellidos, tfCorreo, tfCelular, tfUsuario, tfContraseña;
    String nombres, apellidos, correo, celular, usuario, contraseña, tipo;
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tfNombres = findViewById(R.id.et_SU_Nombres);
        tfApellidos = findViewById(R.id.et_SU_Apellidos);
        tfCorreo = findViewById(R.id.et_SU_Correo);
        tfCelular = findViewById(R.id.et_SU_Celular);
        tfUsuario = findViewById(R.id.et_SU_Usuario);
        tfContraseña = findViewById(R.id.et_SU_Contraseña);
        btnRegistrarse = findViewById(R.id.btn_SU_Registrarse);
        spinner = findViewById(R.id.spinner_SU);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.getSelectedItem().toString();

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarRegistro();
            }
        });
    }

    private void mostrarToast(String oracion){
        Toast.makeText(getApplicationContext(), oracion, Toast.LENGTH_SHORT).show();
    }

    private void guardarRegistro() {

        rq= Volley.newRequestQueue(getApplicationContext());

        nombres = tfNombres.getText().toString().trim();
        apellidos = tfApellidos.getText().toString().trim();
        correo = tfCorreo.getText().toString().trim();
        celular = tfCelular.getText().toString().trim();
        usuario = tfUsuario.getText().toString().trim();
        contraseña = tfContraseña.getText().toString().trim();
        tipo = spinner.getSelectedItem().toString();
        StringRequest solicitud = new StringRequest(Request.Method.POST, "https://a217200082.000webhostapp.com/agregarUsuario.php?",
                response -> {
                    mostrarToast("Registro exitoso");
                    Intent intento=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intento);
                },
                error ->
                        mostrarToast("Error")) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> datos = new HashMap<>();
                datos.put("MandaNombres", nombres);
                datos.put("MandaApellidos", apellidos);
                datos.put("MandaCorreo", correo);
                datos.put("MandaCelular", celular);
                datos.put("MandaUsuario", usuario);
                datos.put("MandaContraseña", contraseña);
                datos.put("MandaTipo", tipo);
                return datos;
            }
        };
        rq.add(solicitud);
    }
}