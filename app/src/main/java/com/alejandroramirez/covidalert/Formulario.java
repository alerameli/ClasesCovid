package com.alejandroramirez.covidalert;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Formulario extends AppCompatActivity {
    Usuario usuario;
    Clase clase;
    TextView tv;
    RadioGroup rg1, rg2, rg3;
    RadioButton rb1_s, rb1_n, rb2_s, rb2_n, rb3_s, rb3_n;
    Button btnEnviar, btnAccion;
    int puntos = 0, ins;
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        tv = findViewById(R.id.tv_mensaje);
        tv.setVisibility(View.INVISIBLE);
        //Se relacionan los radioGroups
        rg1 = findViewById(R.id.RG_P1);
        rg2 = findViewById(R.id.RG_P2);
        rg3 = findViewById(R.id.RG_P3);
        //Se relacionan los radioButtons
        rb1_s = findViewById(R.id.RB1_Si);
        rb1_n = findViewById(R.id.RB1_No);
        rb2_s = findViewById(R.id.RB2_Si);
        rb2_n = findViewById(R.id.RB2_No);
        rb3_s = findViewById(R.id.RB3_Si);
        rb3_n = findViewById(R.id.RB3_No);
        //Se relacionan los botones
        btnEnviar = findViewById(R.id.btnEnviar);
        btnAccion = findViewById(R.id.btnAccion);
        //Se obtienen todos los valores del intent
        usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
        clase = (Clase) getIntent().getExtras().getSerializable("clase");

        rq = Volley.newRequestQueue(getApplicationContext());

        btnAccion.setVisibility(View.INVISIBLE);

        btnEnviar.setOnClickListener(view -> {
            if ((rg1.getCheckedRadioButtonId() != -1) && (rg2.getCheckedRadioButtonId() != -1) && (rg3.getCheckedRadioButtonId() != -1)) {
                if (rb1_s.isSelected()) {
                    puntos++;
                }
                if (rb2_s.isSelected()) {
                    puntos++;
                }
                if (rb3_s.isSelected()) {
                    puntos++;
                }
                tv.setVisibility(View.VISIBLE);
                switch (puntos) {
                    case 0:
                        tv.setText(R.string.txtForm1);
                        btnAccion.setText(R.string.txtInscribirse);
                        ins=1;
                        break;
                    case 1:
                    case 2:
                        tv.setText(R.string.txtForm3);
                        btnAccion.setText(R.string.txtInscribirse);
                        ins=1;
                        break;
                    case 3:
                        tv.setText(R.string.txtForm3);
                        btnAccion.setText(R.string.txtRegresar);
                        ins=0;
                        break;

                }
                btnAccion.setVisibility(View.VISIBLE);
                btnEnviar.setVisibility(View.INVISIBLE);
            } else
                Toast.makeText(getApplicationContext(), "Faltan por llenar", Toast.LENGTH_SHORT).show();
        });

        btnAccion.setOnClickListener(view -> {
            Intent intento=new Intent(getApplicationContext(),PaginaInicioAlumno.class);
            intento.putExtra("usuario",usuario);
            if(ins==1){
                inscribirse();
            }
            startActivity(intento);
        });

    }

    private void inscribirse(){
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
}