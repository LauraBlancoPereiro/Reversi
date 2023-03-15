
package com.example.reversi;
public class Cell {
	
	private int estado = 0; //0 -> vacío; 1 -> ficha negra; 2 -> ficha blanca; 8 -> disponible

	public Cell(int state) {
		estado = state;
	}
	
	public int getEstado() {
		return estado;
	}
	
	public void setEstado(int state) {
		estado = state;
	}
}
