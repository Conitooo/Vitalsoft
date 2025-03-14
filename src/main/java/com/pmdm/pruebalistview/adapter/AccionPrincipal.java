package com.pmdm.pruebalistview.adapter;

import android.graphics.drawable.Drawable;

public class AccionPrincipal {
    String nombre;
    String descripcion;

    Drawable src;

    public AccionPrincipal(String nombre, String descripcion, Drawable src) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.src = src;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Drawable getSrc() {
        return src;
    }

    public void setSrc(Drawable src) {
        this.src = src;
    }
}
