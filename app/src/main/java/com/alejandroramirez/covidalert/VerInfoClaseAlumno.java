package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerInfoClaseAlumno extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    int id;
    String nombre, lugar, fecha, desc, hora, status, prop, contra, contraUsuario;
    TextView tvNombre, tvLugar, tvFecha, tvDesc, tvHora, tvStatus, tvProp;
    Button btnAsistir;
    EditText tfContra;
    RequestQueue rq;
    JsonRequest jrq;
    Clase clase;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_clase_alumno);

        tvNombre = findViewById(R.id.tv_VIC_nombre);
        tvLugar = findViewById(R.id.tv_VIC_Lugar);
        tvFecha = findViewById(R.id.tv_VIC_Fecha);
        tvDesc = findViewById(R.id.tv_VIC_Desc);
        tvHora = findViewById(R.id.tv_VIC_Hora);
        tvStatus = findViewById(R.id.tv_VIC_Status);
        tvProp = findViewById(R.id.tv_VIC_Prop);
        btnAsistir = findViewById(R.id.btn_VIC);
        tfContra = findViewById(R.id.et_VIC);

        usuario=(Usuario) getIntent().getExtras().getSerializable("usuario");
        rq = Volley.newRequestQueue(getApplicationContext());
        int aidi = getIntent().getExtras().getInt("ID");
        obtenerDatos(aidi);

        tfContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAsistir.setEnabled(true);
            }
        });

        btnAsistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tfContra.getText().toString().equals(contra)) {
                    inscripcion();
                    Intent intento = new Intent(getApplicationContext(), PaginaInicioAlumno.class);
                    intento.putExtra("usuario",usuario);
                    startActivity(intento);
                } else {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void inscripcion(){
        StringRequest solicitud = new StringRequest(Request.Method.POST, "https://a217200082.000webhostapp.com/agregarAsistencia.php?clase="+clase.getId()+"&usuario="+usuario.getId(),
                response -> {
                    Toast.makeText(getApplicationContext(), "Te has inscrito con exito", Toast.LENGTH_SHORT).show();
                },
                error ->
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()) {
        };
        rq.add(solicitud);
    }

    private void llenadoTV() {
        tvProp.setText(clase.getHost());
        tvNombre.setText(clase.getNombre());
        tvLugar.setText(clase.getLugar());
        tvFecha.setText(clase.getFecha());
        tvDesc.setText(clase.getDescripcion());
        tvHora.setText(clase.getHora());
        tvStatus.setText(clase.getStatus());
    }

    private void obtenerDatos(int id) {
        String url = "https://a217200082.000webhostapp.com/infoClase.php?aidi=" + id;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray jsonArray = response.optJSONArray("datos");
            JSONObject claseObject = jsonArray.getJSONObject(0);
            id = claseObject.getInt("clase_id");
            nombre = claseObject.getString("clase_nombre");
            lugar = claseObject.getString("clase_lugar");
            fecha = claseObject.getString("clase_fecha");
            hora = claseObject.getString("clase_hora");
            prop = claseObject.getString("clase_propietario");
            desc = claseObject.getString("clase_desc");
            status = claseObject.getString("clase_status");
            contra = claseObject.getString("clase_contra");
            clase = new Clase(id, nombre, lugar, hora, desc, fecha, contra, status, prop);
            llenadoTV();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}