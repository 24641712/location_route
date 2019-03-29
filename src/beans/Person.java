package beans;

import java.util.ArrayList;

public class Person {
	
private String name;
private int Sid;
private ArrayList<Address> pointsAddress;

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getSid() {
	return Sid;
}
public void setSid(int sid) {
	Sid = sid;
}
public ArrayList<Address> getPointsAddress() {
	return pointsAddress;
}
public void setPointsAddress(ArrayList<Address> pointsAddress) {
	this.pointsAddress = pointsAddress;
}

}
