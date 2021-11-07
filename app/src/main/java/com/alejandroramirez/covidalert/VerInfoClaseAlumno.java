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

    private int id;

    // Datos de la clase.
    private String nombre, lugar, fecha, desc, hora, status, prop, contra, contraUsuario;
    private TextView tvNombre, tvLugar, tvFecha, tvDesc, tvHora, tvStatus, tvProp;
    private Button btnAsistir;
    private EditText tfContra;
    private RequestQueue rq;
    private JsonRequest jrq;

    // Se crea un Objeto tipo "Clase".
    private Clase clase;

    // Se crea la variable tipo Objeto de Usuario.
    private Usuario usuario;

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

        // Se obtiene los datos de "Usuario" de la clase anterior "MainActivity".
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

        // Accion al seleccionar el Boton Asistir.
        btnAsistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si el EditText "tfContra" es igual a la contraseña de la clase "contra" entra al if.
                // No es igual, recive un Toast de "Contraseña incorrecta".
                if (tfContra.getText().toString().equals(contra)) {

                    // Se inicial el metodo de inscripcion.
                    inscripcion();

                    // Al finalizar los datos anterior se regresa a la actividad anterior "PaginaInicioAlumno".
                    Intent intento = new Intent(getApplicationContext(), PaginaInicioAlumno.class);
                    intento.putExtra("usuario",usuario);
                    startActivity(intento);
                } else {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Metodo para la solicitud de registro del usuario a una clase.
    // Si el registro es exitoso se manda un Toast de exito.
    // Si no es exitoso se manda un Toast de error.
    private void inscripcion(){
        StringRequest solicitud = new StringRequest(Request.Method.POST, "https://a217200082.000webhostapp.com/agregarAsistencia.php?clase="+clase.getId()+"&usuario="+usuario.getId(),
                response -> {
                    Toast.makeText(getApplicationContext(), "Te has inscrito con exito", Toast.LENGTH_SHORT).show();
                },
                error ->
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()) {
        };

        // Se añade una solicitud
        rq.add(solicitud);
    }

    // Se agregan los campos de la clase en el View.
    private void llenadoTV() {
        tvProp.setText(clase.getHost());
        tvNombre.setText(clase.getNombre());
        tvLugar.setText(clase.getLugar());
        tvFecha.setText(clase.getFecha());
        tvDesc.setText(clase.getDescripcion());
        tvHora.setText(clase.getHora());
        tvStatus.setText(clase.getStatus());
    }

    // Se obtiene la información de la clase utilizando el ID
    private void obtenerDatos(int id) {
        String url = "https://a217200082.000webhostapp.com/infoClase.php?aidi=" + id;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    // Error en la obtencion de la url
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // Se obtienen los datos de la base de datos y llenarlos con el objeto "Clase".
    // Una vez obtenido los datos de la clase se iniciliza el metodo "llenadoTV()".
    // Si hubo un error se manda un Toast.
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