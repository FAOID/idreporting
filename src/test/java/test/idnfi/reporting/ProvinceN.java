package test.idnfi.reporting;

import java.util.ArrayList;
import java.util.HashMap;

public class ProvinceN {
	private int code;
	private String title;
	private HashMap<Integer, ArrayList<Double>> hashN20, hashN30, hashN40, hashN50, hashN60, hashN70, hashN80;
	private HashMap<Integer, ArrayList<Double>> hashV20, hashV30, hashV40, hashV50, hashV60, hashV70, hashV80;


	public ProvinceN(int code, String title) {
		this.code = code;
		this.title = title;

		hashN20 = new HashMap<Integer, ArrayList<Double>>();
		hashN30 = new HashMap<Integer, ArrayList<Double>>();
		hashN40 = new HashMap<Integer, ArrayList<Double>>();
		hashN50 = new HashMap<Integer, ArrayList<Double>>();
		hashN60 = new HashMap<Integer, ArrayList<Double>>();
		hashN70 = new HashMap<Integer, ArrayList<Double>>();
		hashN80 = new HashMap<Integer, ArrayList<Double>>();
		
		hashV20 = new HashMap<Integer, ArrayList<Double>>();
		hashV30 = new HashMap<Integer, ArrayList<Double>>();
		hashV40 = new HashMap<Integer, ArrayList<Double>>();
		hashV50 = new HashMap<Integer, ArrayList<Double>>();
		hashV60 = new HashMap<Integer, ArrayList<Double>>();
		hashV70 = new HashMap<Integer, ArrayList<Double>>();
		hashV80 = new HashMap<Integer, ArrayList<Double>>();
		
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

		public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public void addV(String diameterRange, Integer year, double d, double bole_height) {
		double newV = (0.25 * 3.14 * d * d * bole_height * 0.56) / 10000;
		ArrayList<Double> lv = null;
		if ("20".equals(diameterRange)) {			
			lv = getHashV("20", year);
		} else if ("30".equals(diameterRange)) {
			lv = getHashV("30", year);
		} else if ("40".equals(diameterRange)) {
			lv = getHashV("40", year);
		} else if ("50".equals(diameterRange)) {
			lv = getHashV("50", year);
		} else if ("60".equals(diameterRange)) {
			lv = getHashV("60", year);
		} else if ("70".equals(diameterRange)) {
			lv = getHashV("70", year);
		} else if ("80".equals(diameterRange)) {
			lv = getHashV("80", year);
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

	public ArrayList<Double> getHashV(String diameterRange, Integer year) {
		HashMap<Integer,ArrayList<Double>> result = null;
		if("20".equals(diameterRange))
		{
			if(hashV20.get(year)==null)
			{
				hashV20.put(year, new ArrayList<Double>());
			}
			result = hashV20;
		} else if("30".equals(diameterRange))
		{
			if(hashV30.get(year)==null)
			{
				hashV30.put(year, new ArrayList<Double>());
			}
			result = hashV30;
		} else if("40".equals(diameterRange))
		{
			if(hashV40.get(year)==null)
			{
				hashV40.put(year, new ArrayList<Double>());
			}
			result = hashV40;
		} else if("50".equals(diameterRange))
		{
			if(hashV50.get(year)==null)
			{
				hashV50.put(year, new ArrayList<Double>());
			}
			result = hashV50;
		} else if("60".equals(diameterRange))
		{
			if(hashV60.get(year)==null)
			{
				hashV60.put(year, new ArrayList<Double>());
			}
			result = hashV60;
		} else if("70".equals(diameterRange))
		{
			if(hashV70.get(year)==null)
			{
				hashV70.put(year, new ArrayList<Double>());
			}
			result = hashV70;
		} else if("80".equals(diameterRange))
		{
			if(hashV80.get(year)==null)
			{
				hashV80.put(year, new ArrayList<Double>());
			}
			result = hashV80;
		}
		return result.get(year);
	}
	
	public ArrayList<Double> getHashN(String diameterRange, Integer year) {
		HashMap<Integer,ArrayList<Double>> result = null;
		if("20".equals(diameterRange))
		{
			if(hashN20.get(year)==null)
			{
				hashN20.put(year, new ArrayList<Double>());
			}
			result = hashN20;
		} else if("30".equals(diameterRange))
		{
			if(hashN30.get(year)==null)
			{
				hashN30.put(year, new ArrayList<Double>());
			}
			result = hashN30;
		} else if("40".equals(diameterRange))
		{
			if(hashN40.get(year)==null)
			{
				hashN40.put(year, new ArrayList<Double>());
			}
			result = hashN40;
		} else if("50".equals(diameterRange))
		{
			if(hashN50.get(year)==null)
			{
				hashN50.put(year, new ArrayList<Double>());
			}
			result = hashN50;
		} else if("60".equals(diameterRange))
		{
			if(hashN60.get(year)==null)
			{
				hashN60.put(year, new ArrayList<Double>());
			}
			result = hashN60;
		} else if("70".equals(diameterRange))
		{
			if(hashN70.get(year)==null)
			{
				hashN70.put(year, new ArrayList<Double>());
			}
			result = hashN70;
		} else if("80".equals(diameterRange))
		{
			if(hashN80.get(year)==null)
			{
				hashN80.put(year, new ArrayList<Double>());
			}
			result = hashN80;
		}
		return result.get(year);
	}

	public  ArrayList<Double> getN(String diameterRange, Integer year) {
		ArrayList<Double> result = null;
		if ("20".equals(diameterRange)) {			
			result = getHashN("20", year);
		} else if ("30".equals(diameterRange)) {
			result = getHashN("30", year);
		} else if ("40".equals(diameterRange)) {
			result = getHashN("40", year);
		} else if ("50".equals(diameterRange)) {
			result = getHashN("50", year);
		} else if ("60".equals(diameterRange)) {
			result = getHashN("60", year);
		} else if ("70".equals(diameterRange)) {
			result = getHashN("70", year);
		} else if ("80".equals(diameterRange)) {
			result = getHashN("80", year);
		}
		return result;
	}

	public HashMap<Integer, ArrayList<Double>> getHashN20() {
		return hashN20;
	}


}
