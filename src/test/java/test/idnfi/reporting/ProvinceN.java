package test.idnfi.reporting;

import java.util.ArrayList;
import java.util.HashMap;

public class ProvinceN {
	private int code;
	private String title;
	private HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> hashClusterV, hashClusterN;

	public ProvinceN(int code, String title) {
		this.code = code;
		this.title = title;

		hashClusterV = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>>();
		hashClusterN = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>>();
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

	
	public void addV(String clusterKey, Integer year, String diameterRange, double d, double bole_height) {
		double newV = (0.25 * 3.14 * d * d * bole_height * 0.56) / 10000;
		ArrayList<Double> lv = null;
		if ("20".equals(diameterRange)) {			
			lv = getHashV(clusterKey, year, "20");
		} else if ("30".equals(diameterRange)) {
			lv = getHashV(clusterKey, year, "30");
		} else if ("40".equals(diameterRange)) {
			lv = getHashV(clusterKey, year, "40");
		} else if ("50".equals(diameterRange)) {
			lv = getHashV(clusterKey, year, "50");
		} else if ("60".equals(diameterRange)) {
			lv = getHashV(clusterKey, year, "60");
		} else if ("70".equals(diameterRange)) {
			lv = getHashV(clusterKey, year, "70");
		} else if ("80".equals(diameterRange)) {
			lv = getHashV(clusterKey, year, "80");
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

	public ArrayList<Double> getHashV(String clusterKey, Integer year, String diameterRange) {
		HashMap<Integer,ArrayList<Double>> result = null;		
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> hashV;
		
		
		if(hashClusterV.get(clusterKey)==null)
		{
			hashClusterV.put(clusterKey, new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>());
		}
		hashV = hashClusterV.get(clusterKey);
		
		if("20".equals(diameterRange))
		{
			if(hashV.get(20)==null)
			{
				hashV.put(20, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashV.get(20);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		} else if("30".equals(diameterRange))
		{
			if(hashV.get(30)==null)
			{
				hashV.put(30, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashV.get(30);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		} else if("40".equals(diameterRange))
		{
			if(hashV.get(40)==null)
			{
				hashV.put(40, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashV.get(40);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("50".equals(diameterRange))
		{
			if(hashV.get(50)==null)
			{
				hashV.put(50, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashV.get(50);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("60".equals(diameterRange))
		{
			if(hashV.get(60)==null)
			{
				hashV.put(60, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashV.get(60);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("70".equals(diameterRange))
		{
			if(hashV.get(70)==null)
			{
				hashV.put(70, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashV.get(70);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("80".equals(diameterRange))
		{
			if(hashV.get(80)==null)
			{
				hashV.put(80, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hashYearly = hashV.get(80);
			
			if(hashYearly.get(year)==null)
			{
				hashYearly.put(year, new ArrayList<Double>());
			}
			result = hashYearly;
		}
		
		return result.get(year);
	}
	
	public ArrayList<Double> getHashN(String clusterKey, Integer year, String diameterRange) {
		HashMap<Integer,ArrayList<Double>> result = null;		
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> hashN;
		
		
		if(hashClusterN.get(clusterKey)==null)
		{
			hashClusterN.put(clusterKey, new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>());
		}
		hashN = hashClusterN.get(clusterKey);
		
		if("20".equals(diameterRange))
		{
			if(hashN.get(20)==null)
			{
				hashN.put(20, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashN.get(20);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		} else if("30".equals(diameterRange))
		{
			if(hashN.get(30)==null)
			{
				hashN.put(30, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashN.get(30);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		} else if("40".equals(diameterRange))
		{
			if(hashN.get(40)==null)
			{
				hashN.put(40, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashN.get(40);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("50".equals(diameterRange))
		{
			if(hashN.get(50)==null)
			{
				hashN.put(50, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashN.get(50);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("60".equals(diameterRange))
		{
			if(hashN.get(60)==null)
			{
				hashN.put(60, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashN.get(60);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("70".equals(diameterRange))
		{
			if(hashN.get(70)==null)
			{
				hashN.put(70, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hash20 = hashN.get(70);
			
			if(hash20.get(year)==null)
			{
				hash20.put(year, new ArrayList<Double>());
			}
			result = hash20;
		}else if("80".equals(diameterRange))
		{
			if(hashN.get(80)==null)
			{
				hashN.put(80, new HashMap<Integer, ArrayList<Double>>());
			}
			HashMap<Integer, ArrayList<Double>> hashYearly = hashN.get(80);
			
			if(hashYearly.get(year)==null)
			{
				hashYearly.put(year, new ArrayList<Double>());
			}
			result = hashYearly;
		}
		
		return result.get(year);
	}
	
	

	public ArrayList<Double> getN(String clusterKey, Integer year, String diameterRange) {
		ArrayList<Double> result = null;
		if ("20".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "20");
		} else if ("30".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "30");
		} else if ("40".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "40");
		} else if ("50".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "50");
		} else if ("60".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "60");
		} else if ("70".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "70");
		} else if ("80".equals(diameterRange)) {
			result = getHashN(clusterKey, year, "80");
		}
		return result;
	}

	public HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> getHashClusterV() {
		return hashClusterV;
	}
	
	public HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> getHashClusterN() {
		return hashClusterN;
	}

}
