package test.idnfi.reporting;

import java.util.ArrayList;

public class ProvinceN {
	private int code;
	private String title;
	private ArrayList<Double> up20, up30, up40, up50, up60, up70, up80;
	
	public ProvinceN(int code, String title) {
		this.code = code;
		this.title = title;

		up20 = new ArrayList<Double>();
		up30 = new ArrayList<Double>();
		up40 = new ArrayList<Double>();
		up50 = new ArrayList<Double>();
		up60 = new ArrayList<Double>();
		up70 = new ArrayList<Double>();
		up80 = new ArrayList<Double>();
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<Double> getUp20() {
		return up20;
	}
	public void setUp20(ArrayList<Double> up20) {
		this.up20 = up20;
	}
	public ArrayList<Double> getUp30() {
		return up30;
	}
	public void setUp30(ArrayList<Double> up30) {
		this.up30 = up30;
	}
	public ArrayList<Double> getUp40() {
		return up40;
	}
	public void setUp40(ArrayList<Double> up40) {
		this.up40 = up40;
	}
	public ArrayList<Double> getUp50() {
		return up50;
	}
	public void setUp50(ArrayList<Double> up50) {
		this.up50 = up50;
	}
	public ArrayList<Double> getUp60() {
		return up60;
	}
	public void setUp60(ArrayList<Double> up60) {
		this.up60 = up60;
	}
	public ArrayList<Double> getUp70() {
		return up70;
	}
	public void setUp70(ArrayList<Double> up70) {
		this.up70 = up70;
	}
	public ArrayList<Double> getUp80() {
		return up80;
	}
	public void setUp80(ArrayList<Double> up80) {
		this.up80 = up80;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
