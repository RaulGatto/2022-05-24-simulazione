package it.polito.tdp.itunes.model;

public class Adiacenza {

	private Track t1;
	private Track t2;
	private double peso;

	public Adiacenza(Track t1, Track t2, double peso) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.peso = peso;
	}

	public Track getT1() {
		return t1;
	}

	public void setT1(Track t1) {
		this.t1 = t1;
	}

	public Track getT2() {
		return t2;
	}

	public void setT2(Track t2) {
		this.t2 = t2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

}
