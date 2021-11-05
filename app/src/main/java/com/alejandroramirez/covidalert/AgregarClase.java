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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AgregarClase extends AppCompatActivity {

    EditText tfFecha, tfNombre, tfLugar, tfDesc, tfContra;
    Spinner spinHora, spinMin, spinAP;
    Button btnAgregar;
    String nombre, lugar, desc, contra, hora, status, fecha, nombres, apellidos;
    int propietario;
    RequestQueue rq;
    Usuario usuario;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_clase);

        usuario=(Usuario) getIntent().getExtras().getSerializable("usuario");

        spinHora = findViewById(R.id.spinner_hora);
        spinMin = findViewById(R.id.spinner_minutos);
        spinAP = findViewById(R.id.spinner_meridiano);
        tfFecha = findViewById(R.id.et_AC_Fecha);
        tfNombre = findViewById(R.id.et_AC_Nombre);
        tfLugar = findViewById(R.id.et_AC_Lugar);
        tfDesc = findViewById(R.id.et_AC_Desc);
        tfContra = findViewById(R.id.et_AC_Contra);
        btnAgregar = findViewById(R.id.btn_AgregarClase);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        nombres = getIntent().getExtras().getString("nombres");
        apellidos = getIntent().getExtras().getString("apellidos");

        ArrayAdapter<CharSequence> adapterHoras = ArrayAdapter.createFromResource(this,
                R.array.horas, android.R.layout.simple_spinner_item);
        adapterHoras.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHora.setAdapter(adapterHoras);

        ArrayAdapter<CharSequence> adapterMin = ArrayAdapter.createFromResource(this,
                R.array.minutos, android.R.layout.simple_spinner_item);
        adapterMin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMin.setAdapter(adapterMin);

        ArrayAdapter<CharSequence> adapterAP = ArrayAdapter.createFromResource(this,
                R.array.ampm, android.R.layout.simple_spinner_item);
        adapterAP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAP.setAdapter(adapterAP);

        tfFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.et_AC_Fecha) {
                    DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            // +1 because January is zero
                            final String selectedDate = day + " / " + (month + 1) + " / " + year;
                            tfFecha.setText(selectedDate);
                        }
                    });
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Agregar();
            }
        });
    }

    private void Agregar() {

        rq = Volley.newRequestQueue(getApplicationContext());
        nombre = tfNombre.getText().toString().trim();
        lugar = tfLugar.getText().toString().trim();
        desc = tfDesc.getText().toString().trim();
        contra = tfContra.getText().toString().trim();
        fecha = tfFecha.getText().toString().trim();
        hora = spinHora.getSelectedItem().toString() + ":" + spinMin.getSelectedItem().toString() + ":" + spinAP.getSelectedItem().toString();
        propietario =usuario.getId();
        status = "proxima";

        URL= "https://a217200082.000webhostapp.com/agregarClase.php?" +
                "Nombre="+nombre+
                "&Lugar="+lugar+
                "&Desc="+desc+
                "&Contra="+contra+
                "&Fecha="+fecha+
                "&Hora="+hora+
                "&Propietario="+propietario+
                "&Status="+status;

        StringRequest solicitud = new StringRequest(Request.Method.POST, URL,
                response -> {
                    mostrarToast("Registro exitoso");
                    Intent intento=new Intent(getApplicationContext(),PaginaInicioMaestro.class);
                    intento.putExtra("usuario",usuario);
                    startActivity(intento);
                },
                error ->
                        mostrarToast("Error")) {};
        rq.add(solicitud);
    }

    private void mostrarToast(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}