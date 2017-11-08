package byteofpi.com.medicus;

/**
 * Created by root on 3/5/17.
 */

public class earthquakes {
    private String region;
    private String datetime;
    private String magnitude;
    private String latitude;
    private String longitude;

    public earthquakes() {
    }

    public earthquakes(String region,String datetime,String magnitude, String latitude,String longitude) {
        this.region = region;
        this.datetime=datetime;
        this.magnitude=magnitude;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region =region;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime=datetime;
    }

    public String getMagnitude(){
        return magnitude;
    }
    public void setMagnitude(String magnitude){
        this.magnitude=magnitude;
    }

    public String getLatitude(){
        return latitude;
    }
    public void setLatitude(String latitude){
        this.latitude=latitude;
    }

    public String getLongitude(){
        return longitude;
    }
    public void setLongitude(String longitude){this.longitude=longitude; }

}
