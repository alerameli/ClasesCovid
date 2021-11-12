package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AgregarClase extends AppCompatActivity {

    EditText tfFecha, tfNombre, tfLugar, tfDesc, tfContra;
    Spinner spinHora, spinStatus;
    Button btnAgregar;
    String nombre, lugar, desc, contra, hora, status, fecha, nombres, apellidos;
    int propietario;
    RequestQueue rq;
    Usuario usuario;
    String URL, edicion="xd";
    Clase clase;
    TextView textoStatus,titulo,textoContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_clase);

        spinStatus = findViewById(R.id.spinner_status);
        spinHora = findViewById(R.id.spinner_hora);
        tfFecha = findViewById(R.id.et_AC_Fecha);
        tfNombre = findViewById(R.id.et_AC_Nombre);
        tfLugar = findViewById(R.id.et_AC_Lugar);
        tfDesc = findViewById(R.id.et_AC_Desc);
        tfContra = findViewById(R.id.et_AC_Contra);
        btnAgregar = findViewById(R.id.btn_AgregarClase);
        textoStatus= findViewById(R.id.textoStatus);
        titulo=findViewById(R.id.textView20);
        textoContra=findViewById(R.id.textView21);

        usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
        edicion = getIntent().getExtras().getString("Edicion");
        nombres = getIntent().getExtras().getString("nombres");
        apellidos = getIntent().getExtras().getString("apellidos");

        ArrayAdapter<CharSequence> adapterHoras = ArrayAdapter.createFromResource(this,
                R.array.horas, android.R.layout.simple_spinner_item);
        adapterHoras.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinHora.setAdapter(adapterHoras);
        spinStatus.setAdapter(adapterStatus);

        //Este if es para saber si se hara un insert o un update
        //Si se va a editar, se llenan los campos necesarios para ello
        if (edicion.equals("editar")) {
            clase = (Clase) getIntent().getExtras().getSerializable("clase");
            llenarCampos();
            titulo.setText("Editar clase");
        }else{
            titulo.setText("Agregar clase");
            textoStatus.setVisibility(View.INVISIBLE);
            textoContra.setVisibility(View.INVISIBLE);
            spinStatus.setVisibility(View.INVISIBLE);
        }
        tfFecha.setOnClickListener(view -> {
            if (view.getId() == R.id.et_AC_Fecha) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + "/" + (month + 1) + "/" + year;
                        tfFecha.setText(selectedDate);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        //La accion que haga el boton depende de si es edidicion o no
        btnAgregar.setOnClickListener(view -> {
            if (edicion.equals("editar")) {
                editar();
            } else {
                Agregar();
            }
        });
    }

    //Metodo para llenar los campos en caso de ser una edicion
    private void llenarCampos() {
        String aux = clase.getHora().toString();
        int pos = 0;
        for (int i = 0; i < 20; i++) {
            if (aux.equals(spinHora.getItemAtPosition(i).toString())) {
                pos = i;
                break;
            }
        }
        spinHora.setSelection(pos);
        tfFecha.setText(clase.getFecha());
        tfNombre.setText(clase.getNombre());
        tfLugar.setText(clase.getLugar());
        tfDesc.setText(clase.getDescripcion());
        tfContra.setText(clase.getContra());
    }

    private void editar() {
        rq = Volley.newRequestQueue(getApplicationContext());
        nombre = tfNombre.getText().toString().trim();
        lugar = tfLugar.getText().toString().trim();
        desc = tfDesc.getText().toString().trim();
        contra = tfContra.getText().toString().trim();
        fecha = tfFecha.getText().toString().trim();
        hora = spinHora.getSelectedItem().toString();
        propietario = usuario.getId();
        status = spinStatus.getSelectedItem().toString();

        URL = "https://a217200082.000webhostapp.com/actualizarClase.php?" +
                "Nombre=" + nombre +
                "&Lugar=" + lugar +
                "&Desc=" + desc +
                "&Contra=" + contra +
                "&Fecha=" + fecha +
                "&Hora=" + hora +
                "&Propietario=" + propietario +
                "&Status=" + status +
                "&AIDI=" + clase.getId();

        StringRequest solicitud = new StringRequest(Request.Method.POST, URL,
                response -> {
                    mostrarToast("Registro exitoso");
                    Intent intento = new Intent(getApplicationContext(), PaginaInicioMaestro.class);
                    intento.putExtra("usuario", usuario);
                    startActivity(intento);
                },
                error ->
                        mostrarToast("Error")) {
        };
        rq.add(solicitud);
    }

    private void Agregar() {

        rq = Volley.newRequestQueue(getApplicationContext());
        nombre = tfNombre.getText().toString().trim();
        lugar = tfLugar.getText().toString().trim();
        desc = tfDesc.getText().toString().trim();
        contra = tfContra.getText().toString().trim();
        fecha = tfFecha.getText().toString().trim();
        hora = spinHora.getSelectedItem().toString();
        propietario = usuario.getId();
        status = "proxima";

        URL = "https://a217200082.000webhostapp.com/agregarClase.php?" +
                "Nombre=" + nombre +
                "&Lugar=" + lugar +
                "&Desc=" + desc +
                "&Contra=" + contra +
                "&Fecha=" + fecha +
                "&Hora=" + hora +
                "&Propietario=" + propietario +
                "&Status=" + status;

        StringRequest solicitud = new StringRequest(Request.Method.POST, URL,
                response -> {
                    mostrarToast("Registro exitoso");
                    Intent intento = new Intent(getApplicationContext(), PaginaInicioMaestro.class);
                    intento.putExtra("usuario", usuario);
                    startActivity(intento);
                },
                error ->
                        mostrarToast("Error")) {
        };
        rq.add(solicitud);
    }

    private void mostrarToast(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}