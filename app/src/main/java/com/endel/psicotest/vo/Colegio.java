package com.endel.psicotest.vo;

public class Colegio {

	
	private String nombreColegio = new String(""); 
	private Integer ID = new Integer(-1);
			
	
	public Colegio(int int1, String string) {
		nombreColegio = string;
		ID = int1;
	}
	
	public String getName(){
		return nombreColegio;
	}
	
	public int getId(){
		return ID;
	}
}
