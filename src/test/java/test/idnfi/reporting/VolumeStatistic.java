package test.idnfi.reporting;

public class VolumeStatistic {

	private float totalV;
	public float getTotalV() {
		return totalV;
	}

	public void setTotalV(float totalV) {
		this.totalV = totalV;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	private double standardDeviation;

	public VolumeStatistic(float totalV, double standardDeviation) {
		this.totalV = totalV;
		this.standardDeviation = standardDeviation;
	}

}
