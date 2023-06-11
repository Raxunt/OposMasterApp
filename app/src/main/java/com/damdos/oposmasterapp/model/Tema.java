package com.damdos.oposmasterapp.model;

public class Tema {

    private int numero;
    private String nombre;
    private int repasos;
    private String evaluacion;
    private String nota;

    public Tema() {
    }
    public Tema(int numero, String nombre) {
        this.numero = numero;
        this.nombre = nombre;
        this.repasos = 0;
        this.evaluacion = "Pendiente";
        this.nota = "";
    }

    public Tema(int numero, String nombre, int repasos, String evaluacion, String nota) {
        this.numero = numero;
        this.nombre = nombre;
        this.repasos = repasos;
        this.evaluacion = evaluacion;
        this.nota = nota;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRepasos() {
        return repasos;
    }

    public void setRepasos(int repasos) {
        this.repasos = repasos;
    }

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
