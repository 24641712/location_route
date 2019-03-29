package beans;

public class AnalysisData {

	private int tra_id;// 轨迹编号
	private String start_time;// 开始时间（必填）
	private String end_time;// 结束时间（必填）
	private int stay_time;// 停留时间 单位：秒（选填）
	private int stay_radius;// 停留半径 （选填）
	private String process_option;// 纠偏选项（选填）
	private double longitude;// 经度（必填）
	private double latitude;// 纬度（必填）

	public AnalysisData(){

	}

	public int getTra_id() {
		return tra_id;
	}

	public void setTra_id(int tra_id) {
		this.tra_id = tra_id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getStay_time() {
		return stay_time;
	}

	public void setStay_time(int stay_time) {
		this.stay_time = stay_time;
	}

	public int getStay_radius() {
		return stay_radius;
	}

	public void setStay_radius(int stay_radius) {
		this.stay_radius = stay_radius;
	}

	public String getProcess_option() {
		return process_option;
	}

	public void setProcess_option(String process_option) {
		this.process_option = process_option;
	}
}
