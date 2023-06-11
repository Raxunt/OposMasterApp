package com.damdos.oposmasterapp.model;

import java.util.List;

public class Cuerpo {
    private String nombre;
    private List<Especialidad> especialidades;

    public Cuerpo() {
    }

    public Cuerpo(String nombre, List<Especialidad> especialidades) {
        this.nombre = nombre;
        this.especialidades = especialidades;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }
}
