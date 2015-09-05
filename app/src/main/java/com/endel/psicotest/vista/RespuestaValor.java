package com.endel.psicotest.vista;

/**
 * Created by Usuario on 03/09/2015.
 */
public class RespuestaValor {
    int id;
    float valor;
    int siguiente;

    public RespuestaValor(int id, float valor, int siguiente) {
        this.id = id;
        this.valor = valor;
        this.siguiente = siguiente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getSiguiente() {
        return siguiente;
    }

}
