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
    private final ArrayList<Clase> mListaClases = new ArrayList<>();
    private final ArrayList<Usuario> mListaUsuarios = new ArrayList<>();
    private final ArrayList<ArrayList<String>> mListaAlerta = new ArrayList<>();
    private String now;
    public int valorBusqueda;
    private final int IDUsuario;

    public BusquedaAlertas(Context context, int IDUsuario){
        mContext = context;
        this.IDUsuario = IDUsuario;
    }

    public void setValorBusqueda(int valorBusqueda) {
        this.valorBusqueda = valorBusqueda;
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        now = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
        c.add(Calendar.DATE, -5);
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(c.getTime());
    }

    public void getAlertaUsuario(){
        String URL = "https://a217200082.000webhostapp.com/mostrarAlertasUsuario.php?" +
                     "BEFORE5DAYS="+getDate()+"&" +
                     "TODAY="+now;
        volleyRequest(URL);
    }

    public void getAlertaClases(){
        String URL = "https://a217200082.000webhostapp.com/mostrarMisClases.php?" +
                "IDUsuario="+IDUsuario;
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

            switch (valorBusqueda){
                case 0:

                    setAlerta(jsonArray.length() > 0);

                    for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        // Se obtiene la clase del Array de la base de datos
                        JSONObject claseObject = jsonArray.getJSONObject(i);

                        // Se crea un Objeto tipo "Usuario", añadiendo los datos del mismo
                        Usuario usuario = new Usuario();
                        //usuario.setId(claseObject.getInt("usuario_id"));
                        usuario.setNombres(claseObject.getString("usuario_nombres"));
                        usuario.setApellidos(claseObject.getString("usuario_apellidos"));
                        usuario.setCelular(claseObject.getString("usuario_celular"));
                        usuario.setCorreo(claseObject.getString("usuario_correo"));
                        usuario.setTipo(claseObject.getString("usuario_tipo"));

                        ArrayList<String> alerta = new ArrayList<String>();
                        //alerta.add(claseObject.getString("alerta_clase"));
                        alerta.add(claseObject.getString("alerta_positivo"));
                        alerta.add(claseObject.getString("alerta_sintomas"));
                        alerta.add(claseObject.getString("alerta_fecha"));

                        mListaAlerta.add(alerta);

                        // Se agregan al listado de usuarios
                        mListaUsuarios.add(usuario);
                    }

                    break;
                case 1:

                    for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        // Se obtiene la clase del Array de la base de datos
                        JSONObject claseObject = jsonArray.getJSONObject(i);

                        // Se crea un Objeto tipo "Clase", añadiendo los datos del mismo
                        Clase clase = new Clase(
                                claseObject.getInt("clase_id"),
                                claseObject.getString("clase_nombre"),
                                claseObject.getString("clase_lugar"),
                                claseObject.getString("clase_hora"),
                                claseObject.getString("clase_desc"),
                                claseObject.getString("clase_fecha"),
                                "",
                                claseObject.getString("clase_status"),
                                claseObject.getString("clase_propietario")
                        );

                        // Se agregan al listado de clases
                        mListaClases.add(clase);

                    }

                    break;
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

    public ArrayList<Usuario> getListaUsuarios() {return mListaUsuarios;}

    public ArrayList<Clase> getListaClases(){return mListaClases;}

    public ArrayList<ArrayList<String>> getListaAlerta(){return mListaAlerta;}
}
