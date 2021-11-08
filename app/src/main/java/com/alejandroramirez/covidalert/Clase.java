package com.alejandroramirez.covidalert;

import java.io.Serializable;

public class Clase implements Serializable {

    String nombre,lugar,hora,descripcion,fecha,contra,status,host;
    int id;

    public Clase(int id, String nombre, String lugar, String hora, String descripcion, String fecha, String contra, String status, String host) {
        this.id=id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.hora = hora;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.contra = contra;
        this.status = status;
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
