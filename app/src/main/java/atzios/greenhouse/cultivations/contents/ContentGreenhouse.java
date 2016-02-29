package atzios.greenhouse.cultivations.contents;

import com.google.android.gms.maps.model.LatLng;

/**
 * Content class Greenhouse
 * Contains all greenhouse's properties
 * Created by Atzios Vasilis on 16/12/2014.
 */
public class ContentGreenhouse {
    private int id; //Greenhouse id
    private int userId; //User id
    private String name; //Greenhouse name
    private double area; //Greenhouse area in square meters
    private LatLng location; //Greenhouse location (Google maps Loc point)
    private String address; //Greenhouse address
    private String imagePath; //Greenhouse image preview path

    public ContentGreenhouse() {
        id = -1;
        userId = -1;
        name = null;
        area = 0;
        location = null;
        address = null;
        imagePath = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
    public void setLocation(double latitude,double longitude) {
        location = new LatLng(latitude,longitude);
    }
    public String getLatitudeString() {
        if(location == null)
            return "";
        else
            return Double.toString(location.latitude);
    }
    public String getLongitudeString() {
        if(location == null)
            return "";
        else
            return Double.toString(location.longitude);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
