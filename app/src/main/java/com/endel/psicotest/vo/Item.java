package com.endel.psicotest.vo;

import java.util.ArrayList;

public class Item {
	
	private String texto_pregunta;
	private int id;
	private int idTipo;
	private ArrayList<RespuestaRellenada> respuestas;
		
	public String getTextoPregunta(){
		return texto_pregunta;
	}
	public void setTextoPregunta(String texto){
		texto_pregunta=texto;
	}
	public int getIdPregunta(){
		return id;
	}
	public int getIdTipo(){
		return idTipo;
	}

	public void setIdTipo(int idTipo){
		this.idTipo=idTipo;
	}

	public void setIdPregunta(int id_pregunta){
		id=id_pregunta;
	}
	
	public String getRespuestaTexto(int i ){
		return respuestas.get(i).getTextoRespuesta();
	}
	
	public ArrayList<RespuestaRellenada> getRespuestas(){
		return respuestas;
	}
	public void setRespuestas(ArrayList<RespuestaRellenada> resp){
		respuestas=resp;
	}
	
}
