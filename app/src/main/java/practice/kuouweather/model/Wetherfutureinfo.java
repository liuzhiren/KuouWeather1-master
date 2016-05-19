package practice.kuouweather.model;

/**
 * Created by a312689543 on 2016/4/21.
 */
public class Wetherfutureinfo {
    private int id;
    private int weatherTemp;
    private String weatherDesp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(int weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public String getWeatherDesp() {
        return weatherDesp;
    }

    public void setWeatherDesp(String weatherDesp) {
        this.weatherDesp = weatherDesp;
    }
}
