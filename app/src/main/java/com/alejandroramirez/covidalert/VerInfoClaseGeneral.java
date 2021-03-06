package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
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

import java.lang.reflect.TypeVariable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VerInfoClaseGeneral extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    int id;
    String nombre, lugar, fecha, desc, hora, status, prop, contra, contraUsuario;
    TextView tvNombre, tvLugar, tvFecha, tvDesc, tvHora, tvStatus, tvProp;
    Button btnAsistir;
    EditText tfContra;
    RequestQueue rq;
    JsonRequest jrq;
    Clase clase;
    Usuario usuario;
    int inscrito = 6;
    String accion, activo = "nose";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_clase_general);

        tvNombre = findViewById(R.id.tv_VIC_nombre);
        tvLugar = findViewById(R.id.tv_VIC_Lugar);
        tvFecha = findViewById(R.id.tv_VIC_Fecha);
        tvDesc = findViewById(R.id.tv_VIC_Desc);
        tvHora = findViewById(R.id.tv_VIC_Hora);
        tvStatus = findViewById(R.id.tv_VIC_Status);
        tvProp = findViewById(R.id.tv_VIC_Prop);
        btnAsistir = findViewById(R.id.btn_VIC);
        tfContra = findViewById(R.id.et_VIC);
        tfContra.setClickable(false);

        usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
        rq = Volley.newRequestQueue(getApplicationContext());
        int aidi = getIntent().getExtras().getInt("ID");
        obtenerDatos(aidi);


        btnAsistir.setOnClickListener(view -> {
            Intent intento;
            switch (accion) {
                case ("Darse de baja"):
                    baja();
                    intento = new Intent(getApplicationContext(), PaginaInicioAlumno.class);
                    intento.putExtra("usuario", usuario);
                    startActivity(intento);
                    break;
                case ("Inscribirse"):
                    //Fecha que nos daria el objeto
                    String fechaObj = clase.getFecha();
                    //Se crea el formato de fecha
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date dateObj = format.parse(fechaObj);
                        Calendar calendar=Calendar.getInstance();
                        Date dateToday=calendar.getTime();
                        int dif=(int) ((dateObj.getTime()-dateToday.getTime())/86400000);
                        if(dif<=1){
                            dif++;
                        }
                        if(dif<=1){
                            if(tfContra.getText().toString().equals(clase.getContra())){
                                intento = new Intent(getApplicationContext(), Formulario.class);
                                intento.putExtra("clase", clase);
                                intento.putExtra("usuario", usuario);
                                startActivity(intento);
                            }else{
                                Toast.makeText(getApplicationContext(), "Contrase??a incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Solo te puedes inscribir el mismo dia o un dia antes", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException exception) {
                        Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ("Editar"):
                    intento = new Intent(getApplicationContext(), AgregarClase.class);
                    intento.putExtra("Edicion", "editar");
                    intento.putExtra("clase", clase);
                    intento.putExtra("usuario", usuario);
                    startActivity(intento);
                    break;
            }

        });

    }

    private void comprobarInscrito() {
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET,
                "https://a217200082.000webhostapp.com/comprobarInscrito.php?clase=" + clase.getId() + "&usuario=" + usuario.getId(), null,
                response -> {
                    try {
                        JSONArray jsonArray = response.optJSONArray("datos");
                        assert jsonArray != null;
                        JSONObject claseObject = jsonArray.getJSONObject(0);
                        inscrito = claseObject.getInt("existe");
                        if (inscrito == 1) {
                            tfContra.setVisibility(View.INVISIBLE);
                            btnAsistir.setText(R.string.txtDesincribirse);
                            accion = "Darse de baja";
                        } else {
                            btnAsistir.setText(R.string.txtInscribirse);
                            accion = "Inscribirse";
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {

        });
        rq.add(solicitud);
    }


    private void inscripcion() {
        StringRequest solicitud = new StringRequest(Request.Method.POST, "https://a217200082.000webhostapp.com/agregarAsistencia.php?clase=" +
                clase.getId() + "&usuario=" + usuario.getId(),
                response -> {
                    Toast.makeText(getApplicationContext(), "Te has inscrito a la clase", Toast.LENGTH_SHORT).show();
                },
                error ->
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()) {
        };
        rq.add(solicitud);
    }

    private void baja() {
        StringRequest solicitud = new StringRequest(Request.Method.POST, "https://a217200082.000webhostapp.com/eliminarAsistencia.php?clase=" +
                clase.getId() + "&usuario=" + usuario.getId(),
                response -> {
                    Toast.makeText(getApplicationContext(), "Ya no asistiras a esta clase", Toast.LENGTH_SHORT).show();
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
            if (usuario.getTipo().equals("Alumno")) {//if para ver si es alumno
                tfContra.setClickable(false);
                comprobarInscrito();
            } else {
                btnAsistir.setText(R.string.txtEditar);
                tfContra.setText(clase.getContra());
                accion = "Editar";
            }
            if (!clase.getStatus().equals("proxima")) {
                btnAsistir.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}