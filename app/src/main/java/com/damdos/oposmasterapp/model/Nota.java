package com.damdos.oposmasterapp.model;

import java.util.Date;

public class Nota {
    private String contenido;
    private Date fecha;

    public Nota(){
    }

    public Nota(String contenido, Date fecha) {
        this.contenido = contenido;
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public Date getFecha() {
        return fecha;
    }
}
