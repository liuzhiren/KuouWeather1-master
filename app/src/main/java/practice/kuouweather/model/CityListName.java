package practice.kuouweather.model;

/**
 * Created by a312689543 on 2016/4/30.
 */
public class CityListName {
    private int id;
    private String cityName;
    private String cityCode;

    public String getCityName() {
        return cityName;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode){this.cityCode = cityCode;}
    public String getCityCode(){return cityCode ;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


