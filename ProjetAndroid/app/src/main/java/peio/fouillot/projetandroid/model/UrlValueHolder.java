package peio.fouillot.projetandroid.model;

import java.io.Serializable;
import java.util.List;

public class UrlValueHolder implements Serializable{

    private int distance = 500;

    private List<Float> values; // values[minRoom, maxRoom] store the room segment between 1-8

    private float longitude;
    private float latitude;

    private String type_local; //House or Apartment
    private String jsonHolder;

    private String jsonDataPath; //the path to the json file on storage

    public UrlValueHolder(int distance, String type_local, float longitude, float latitude) {
        this.distance = distance;
        this.type_local = type_local;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getFinalUrl() {
        //ex : "http://api.cquest.org/dvf?lat=37.02&lon=-122.08&dist=500";
        return "http://api.cquest.org/dvf?lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude) + "&dist=" + String.valueOf(distance);
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public UrlValueHolder() {
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getType_local() {
        return type_local;
    }

    public void setType_local(String type_local) {
        this.type_local = type_local;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getJsonHolder() {
        return jsonHolder;
    }

    public void setJsonHolder(String jsonHolder) {
        this.jsonHolder = jsonHolder;
    }

    public String getJsonDataPath() {
        return jsonDataPath;
    }

    public void setJsonDataPath(String jsonDataPath) {
        this.jsonDataPath = jsonDataPath;
    }


    @Override
    public String toString() {
        return "UrlValueHolder{" +
                "distance='" + distance + " m " + '\'' +
                ", type_local='" + type_local + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
