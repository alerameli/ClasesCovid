package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    Button btnSignUp, btnLogIn;
    EditText tfUsuario, tfContraseña;
    String usuario, contraseña;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tfUsuario = findViewById(R.id.et_IS_Usuario);
        tfContraseña = findViewById(R.id.et_IS_Contraseña);
        btnSignUp = findViewById(R.id.btn_IS_Signup);
        btnLogIn = findViewById(R.id.btn_IS_Login);
        rq = Volley.newRequestQueue(getApplicationContext());

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intento);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        usuario = tfUsuario.getText().toString();
        contraseña = tfContraseña.getText().toString();
        String url = "https://a217200082.000webhostapp.com/iniciarSesion.php?Usuario=" + usuario + "&Contrasena=" + contraseña;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Usuario usuario = new Usuario();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            assert jsonArray != null;
            jsonObject = jsonArray.getJSONObject(0);
            usuario.setId((jsonObject.optInt("usuario_id")));
            usuario.setNombres(jsonObject.optString("usuario_nombres"));
            usuario.setApellidos(jsonObject.optString("usuario_apellidos"));
            usuario.setCorreo(jsonObject.optString("usuario_correo"));
            usuario.setCelular(jsonObject.optString("usuario_celular"));
            usuario.setUsuario(jsonObject.optString("usuario_usuario"));
            usuario.setContraseña(jsonObject.optString("usuario_contraseña"));
            usuario.setTipo(jsonObject.optString("usuario_tipo"));
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "errore joven", Toast.LENGTH_SHORT).show();
        }
        Intent intento=null;
        switch (usuario.getTipo()){
            case "Alumno":
                intento = new Intent(getApplicationContext(), PaginaInicioAlumno.class);
                break;
            case "Maestro o personal administrativo":
                intento = new Intent(getApplicationContext(), PaginaInicioMaestro.class);
                break;
        }
        assert intento != null;
        intento.putExtra("usuario",usuario);
        startActivity(intento);
    }
}