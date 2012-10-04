package test.idnfi.reporting;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.stat.DescriptiveStatistics;

public class ProvinceN {
	private int code;
	private String title;
	private HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> hashClusterPlotaV, hashClusterPlotaN;
	private HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> hashClusterTract5V, hashClusterTract5N;

	public ProvinceN(int code, String title) {
		this.code = code;
		this.title = title;

		hashClusterPlotaV = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>>();
		hashClusterPlotaN = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>>();
		
		hashClusterTract5V = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>>();
		hashClusterTract5N = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>>();
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

	
	public void addPlotaV(String clusterKey, Integer year, String diameterRange, double d, double bole_height) {
		//double newV = (0.25 * 3.14 * d * d * bole_height * 0.56) / 10000;
		double newV = 0.25 * 3.14 * (d/100) * (d/100) * bole_height * 0.7;
		ArrayList<Double> lv = null;
		if ("20".equals(diameterRange)) {			
			lv = getHashPlotaV(clusterKey, year, "20");
		} else if ("30".equals(diameterRange)) {
			lv = getHashPlotaV(clusterKey, year, "30");
		} else if ("40".equals(diameterRange)) {
			lv = getHashPlotaV(clusterKey, year, "40");
		} else if ("50".equals(diameterRange)) {
			lv = getHashPlotaV(clusterKey, year, "50");
		} else if ("60".equals(diameterRange)) {
			lv = getHashPlotaV(clusterKey, year, "60");
		} else if ("70".equals(diameterRange)) {
			lv = getHashPlotaV(clusterKey, year, "70");
		} else if ("80".equals(diameterRange)) {
			lv = getHashPlotaV(clusterKey, year, "80");
		}
		lv.add(newV);			
	}
	
	public void addTract5V(String clusterKey, Integer year, String diameterRange, double d, double bole_height) {
		//double newV = (0.25 * 3.14 * d * d * bole_height * 0.56) / 10000;
		double newV = 0.25 * 3.14 * (d/100) * (d/100) * bole_height * 0.7;
		ArrayList<Double> lv = null;
		if ("20".equals(diameterRange)) {			
			lv = getHashTract5V(clusterKey, year, "20");
		} else if ("30".equals(diameterRange)) {
			lv = getHashTract5V(clusterKey, year, "30");
		} else if ("40".equals(diameterRange)) {
			lv = getHashTract5V(clusterKey, year, "40");
		} else if ("50".equals(diameterRange)) {
			lv = getHashTract5V(clusterKey, year, "50");
		} else if ("60".equals(diameterRange)) {
			lv = getHashTract5V(clusterKey, year, "60");
		} else if ("70".equals(diameterRange)) {
			lv = getHashTract5V(clusterKey, year, "70");
		} else if ("80".equals(diameterRange)) {
			lv = getHashTract5V(clusterKey, year, "80");
		}
		lv.add(newV);			
	}


	public ArrayList<Double> getHashPlotaV(String clusterKey, Integer year, String diameterRange) {
		HashMap<Integer,ArrayList<Double>> result = null;		
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> hashV;
		
		
		if(hashClusterPlotaV.get(clusterKey)==null)
		{
			hashClusterPlotaV.put(clusterKey, new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>());
		}
		hashV = hashClusterPlotaV.get(clusterKey);
		
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
	
	public ArrayList<Double> getHashTract5V(String clusterKey, Integer year, String diameterRange) {
		HashMap<Integer,ArrayList<Double>> result = null;		
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> hashV;
		
		
		if(hashClusterTract5V.get(clusterKey)==null)
		{
			hashClusterTract5V.put(clusterKey, new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>());
		}
		hashV = hashClusterTract5V.get(clusterKey);
		
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
	
	public ArrayList<Double> getHashPlotaN(String clusterKey, Integer year, String diameterRange) {
		HashMap<Integer,ArrayList<Double>> result = null;		
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> hashN;
		
		
		if(hashClusterPlotaN.get(clusterKey)==null)
		{
			hashClusterPlotaN.put(clusterKey, new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>());
		}
		hashN = hashClusterPlotaN.get(clusterKey);
		
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
	
	public ArrayList<Double> getHashTract5N(String clusterKey, Integer year, String diameterRange) {
		HashMap<Integer,ArrayList<Double>> result = null;		
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> hashN;
		
		
		if(hashClusterTract5N.get(clusterKey)==null)
		{
			hashClusterTract5N.put(clusterKey, new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>());
		}
		hashN = hashClusterTract5N.get(clusterKey);
		
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
	
	

	public ArrayList<Double> getPlotaN(String clusterKey, Integer year, String diameterRange) {
		ArrayList<Double> result = null;
		if ("20".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "20");
		} else if ("30".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "30");
		} else if ("40".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "40");
		} else if ("50".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "50");
		} else if ("60".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "60");
		} else if ("70".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "70");
		} else if ("80".equals(diameterRange)) {
			result = getHashPlotaN(clusterKey, year, "80");
		}
		return result;
	}
	
	public ArrayList<Double> getTract5N(String clusterKey, Integer year, String diameterRange) {
		ArrayList<Double> result = null;
		if ("20".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "20");
		} else if ("30".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "30");
		} else if ("40".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "40");
		} else if ("50".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "50");
		} else if ("60".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "60");
		} else if ("70".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "70");
		} else if ("80".equals(diameterRange)) {
			result = getHashTract5N(clusterKey, year, "80");
		}
		return result;
	}

	public HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> getHashClusterPlotaV() {
		return hashClusterPlotaV;
	}
	
	public HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> getHashClusterPlotaN() {
		return hashClusterPlotaN;
	}
	
	public HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> getHashClusterTract5V() {
		return hashClusterPlotaV;
	}
	
	public HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Double>>>> getHashClusterTract5N() {
		return hashClusterPlotaN;
	}

	// SD per diameter tersedia jika diperlukan
	public VolumeStatistic getVolume(String clusterKey, Integer year, String string) {
		
		DescriptiveStatistics stats = DescriptiveStatistics.newInstance();
		float totalV = 0;
		for(double v : getHashPlotaV(clusterKey, year, "20"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		return new VolumeStatistic(totalV, stats.getStandardDeviation());
	}

	public VolumeStatistic getStandarDeviation(String clusterKey, Integer year) {
		
		DescriptiveStatistics stats = DescriptiveStatistics.newInstance();
		float totalV = 0;
		for(double v : getHashPlotaV(clusterKey, year, "20"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		for(double v : getHashPlotaV(clusterKey, year, "30"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		for(double v : getHashPlotaV(clusterKey, year, "40"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		for(double v : getHashPlotaV(clusterKey, year, "50"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		for(double v : getHashPlotaV(clusterKey, year, "60"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		for(double v : getHashPlotaV(clusterKey, year, "70"))
		{
			stats.addValue(v);
			totalV +=v;
		}
		for(double v : getHashPlotaV(clusterKey, year, "80"))
		{
			stats.addValue(v);
			totalV +=v;
		}		
		return new VolumeStatistic(totalV, stats.getStandardDeviation()); 
	}

}
