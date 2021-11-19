package com.alejandroramirez.covidalert;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BusquedaAlertas implements Response.Listener<JSONObject>, Response.ErrorListener {

    private RequestQueue rq;
    private JsonRequest jrq;
    private final Context mContext;

    private Boolean alerta = false;
    private String now;
    private final int IDUsuario;

    private int countClasesInfected;
    private int countUserInfected;

    public BusquedaAlertas(Context context, int IDUsuario){
        mContext = context;
        this.IDUsuario = IDUsuario;
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        now = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
        c.add(Calendar.DATE, -5);
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
    }

    public void getAlertaBusqueda(){
        countClasesInfected = 0;
        countUserInfected = 0;
        String URL = "https://a217200082.000webhostapp.com/mostrarAlertasClasesInfected.php?" +
                     "BEFORE5DAYS=" + getDate() + "&" +
                     "TODAY=" + now + "&" +
                     "IDUsuario=" + IDUsuario;
        volleyRequest(URL);
    }

    public void volleyRequest(String URL){
        rq = Volley.newRequestQueue(mContext);
        jrq = new JsonObjectRequest(Request.Method.GET, URL, null, this, null);
        rq.add(jrq);
    }

    public void onResponse(JSONObject response) {
        try {
            JSONArray jsonArray = response.optJSONArray("datos");

            setAlerta(jsonArray.length() > 0);

            for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                // Se obtiene la clase del Array de la base de datos
                JSONObject claseObject = jsonArray.getJSONObject(i);

                countUserInfected = claseObject.getInt("userInfected");
                countClasesInfected = claseObject.getInt("claseInfected");

            }

        } catch (JSONException e) {
            //Toast.makeText(mContext,"No se encontraron clases por mostrar", Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext,e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // Error en la obtencion de la url
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(mContext, "Ha ocurrido un error de conexion", Toast.LENGTH_SHORT).show();
    }

    public Boolean getAlerta() {
        return alerta;
    }

    public void setAlerta(Boolean alerta) {
        this.alerta = alerta;
    }

    public int getCountClasesInfected() {
        return countClasesInfected;
    }

    public int getCountUserInfected() {
        return countUserInfected;
    }
}
