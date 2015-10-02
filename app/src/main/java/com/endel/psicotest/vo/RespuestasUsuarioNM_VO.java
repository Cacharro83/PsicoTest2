package com.endel.psicotest.vo;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by JavierH on 02/10/2015.
 */
public class RespuestasUsuarioNM_VO implements KvmSerializable {
    private int IdRespuestaUsuario;
    private int IdRespuesta;
    private int IdUsuario;
    private String valor;
    private int enviado;

    public RespuestasUsuarioNM_VO(int idRespuesta, int idUsuario, String valor) {
        IdRespuesta = idRespuesta;
        IdUsuario = idUsuario;
        this.valor = valor;
    }

    public RespuestasUsuarioNM_VO() {

    }

    public int getIdRespuestaUsuario() {
        return IdRespuestaUsuario;
    }

    public void setIdRespuestaUsuario(int idRespuestaUsuario) {
        IdRespuestaUsuario = idRespuestaUsuario;
    }

    public int getIdRespuesta() {
        return IdRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        IdRespuesta = idRespuesta;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getValor() {
        return valor;
    }


    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }
}
