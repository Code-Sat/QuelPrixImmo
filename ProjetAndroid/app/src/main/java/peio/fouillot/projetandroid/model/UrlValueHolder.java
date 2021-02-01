package peio.fouillot.projetandroid.model;

import java.io.Serializable;

public class UrlValueHolder implements Serializable{

    private int distance = 500;

    private float longitude;
    private float latitude;

    private String type_local; //Maison - appartement
    private String jsonHolder; //To keep the url response

    private String resultList0;
    private String resultList1;
    private String resultList2;

    public String getResultList0() {
        return resultList0;
    }

    public void setResultList0(String resultList0) {
        this.resultList0 = resultList0;
    }

    public String getResultList1() {
        return resultList1;
    }

    public void setResultList1(String resultList1) {
        this.resultList1 = resultList1;
    }

    public String getResultList2() {
        return resultList2;
    }

    public void setResultList2(String resultList2) {
        this.resultList2 = resultList2;
    }

    public UrlValueHolder(int distance, String type_local, float longitude, float latitude) {
        this.distance = distance;
        this.type_local = type_local;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getFinalUrl(int distance, String type_local, float longitude, float latitude) {
        String final_url;
        //ex : "http://api.cquest.org/dvf?lat=44.441&lon=0.322&dist=600";
        final_url = "http://api.cquest.org/dvf?lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude) + "&dist=" + distance + "&type_local=" + type_local;

        return final_url;
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
