package com.damdos.oposmasterapp.model;

import java.util.List;

public class Especialidad {
    private String nombre;
    private List<Tema> temas;

    public Especialidad() {
    }

    public Especialidad(String nombre, List<Tema> temas) {
        this.nombre = nombre;
        this.temas = temas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Tema> getTemas() {
        return temas;
    }

    public void setTemas(List<Tema> temas) {
        this.temas = temas;
    }
}
