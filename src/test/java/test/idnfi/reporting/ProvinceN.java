package test.idnfi.reporting;

import java.util.ArrayList;

public class ProvinceN {
	private int code;
	private String title;
	private ArrayList<Double> up20, up30, up40, up50, up60, up70, up80;
	private ArrayList<Double> v20, v30, v40, v50, v60, v70, v80;

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

		setV20(new ArrayList<Double>());
		setV30(new ArrayList<Double>());
		setV40(new ArrayList<Double>());
		setV50(new ArrayList<Double>());
		setV60(new ArrayList<Double>());
		setV70(new ArrayList<Double>());
		setV80(new ArrayList<Double>());
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

	public ArrayList<Double> getV20() {
		return v20;
	}

	public void setV20(ArrayList<Double> v20) {
		this.v20 = v20;
	}

	public ArrayList<Double> getV30() {
		return v30;
	}

	public void setV30(ArrayList<Double> v30) {
		this.v30 = v30;
	}

	public ArrayList<Double> getV40() {
		return v40;
	}

	public void setV40(ArrayList<Double> v40) {
		this.v40 = v40;
	}

	public ArrayList<Double> getV50() {
		return v50;
	}

	public void setV50(ArrayList<Double> v50) {
		this.v50 = v50;
	}

	public ArrayList<Double> getV60() {
		return v60;
	}

	public void setV60(ArrayList<Double> v60) {
		this.v60 = v60;
	}

	public ArrayList<Double> getV70() {
		return v70;
	}

	public void setV70(ArrayList<Double> v70) {
		this.v70 = v70;
	}

	public ArrayList<Double> getV80() {
		return v80;
	}

	public void setV80(ArrayList<Double> v80) {
		this.v80 = v80;
	}

	public void addToV(String v, double d, double bole_height) {
		double newV = (0.25 * 3.14 * d * d * bole_height * 0.56) / 10000;
		ArrayList<Double> lv = null;
		if ("20".equals(v)) {
			lv = v20;
		} else if ("30".equals(v)) {
			lv = v30;
		} else if ("40".equals(v)) {
			lv = v40;
		} else if ("50".equals(v)) {
			lv = v50;
		} else if ("60".equals(v)) {
			lv = v60;
		} else if ("70".equals(v)) {
			lv = v70;
		} else if ("80".equals(v)) {
			lv = v80;
		}

		if (lv.size() == 0) {
			lv.add(newV);
		} else {
			Double oldV = lv.get(0);
			newV += oldV;
			lv.clear();
			lv.add(newV);

		}
	}

}
