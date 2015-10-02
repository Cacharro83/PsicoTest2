package com.endel.psicotest.vo;

public class RespuestaRellenada {
	
	private String texto_respuesta;
	private int siguiente_pregunta ;
	private String texto_error;
	private float valor;
	
	public String getTextoRespuesta(){
		return texto_respuesta;
	}
	public void setTextoRespuesta(String texto){
		texto_respuesta=texto;
	}
	public int getSiguiente(){
		return siguiente_pregunta;
	}
	public void setSiguiente(int id_sig){
		siguiente_pregunta = id_sig;
	}
	public String getTextoError(){
		return texto_error;
	}
	public void setTextoError(String error){
		texto_error=error;
	}	
	public float getValor(){
		return valor;
	}
	public void setValor(Float float1){
		valor = float1;
	}
}
