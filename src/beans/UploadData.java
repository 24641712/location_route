package beans;

/**
 * 上传轨迹到鹰眼
 * 
 * @author Administrator
 * 
 */
public class UploadData {
	private String entity_name; // entity唯一标识（必填）

	private double longitude;// 经度（必填）
	private double latitude;// 纬度（必填）
	private String loc_time;// 定位时设备的时间（必填）
	private String coord_type_input;// 坐标类型（必填）
	private double speed;// 速度（选填）
	private int direction;// 方向（选填）
	private double height;// 高度（选填）
	private double radius;// 定位精度（选填）
	private String object_name;// 对象数据名称（选填）

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

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public String getLoc_time() {
		return loc_time;
	}

	public void setLoc_time(String loc_time) {
		this.loc_time = loc_time;
	}

	public String getCoord_type_input() {
		return coord_type_input;
	}

	public void setCoord_type_input(String coord_type_input) {
		this.coord_type_input = coord_type_input;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getObject_name() {
		return object_name;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

}
