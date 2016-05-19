package practice.kuouweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import practice.kuouweather.model.City;
import practice.kuouweather.model.CoolWeatherDB;
import practice.kuouweather.model.County;
import practice.kuouweather.model.Province;

/**
 * Created by a312689543 on 2016/4/13.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String [] allProvinces=response.split(",");
            if(allProvinces!=null && allProvinces.length>0){
                for (String p:allProvinces){
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
            }
                return true;
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的某个省所有市的数据
     */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null && allCities.length>0){
                for (String c:allCities){
                    String[] array=c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的某个市所有县的数据
     */
    public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounties=response.split(",");
            if (allCounties!=null && allCounties.length>0){
                for(String c:allCounties ){
                    String[] array=c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
    /*
    * 解析服务器返回的天气信息*/
    public static void handleWeatherResponse(Context context,String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherinfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherinfo.getString("city");
            String weathercode = weatherinfo.getString("cityid");
            String temp1 = weatherinfo.getString("temp1");
            String temp2 = weatherinfo.getString("temp2");
            String weatherDesp = weatherinfo.getString("weather");
            String publishTime = weatherinfo.getString("ptime");
            //把数据储存到shareprefenerce里去
            saveWeatherInfo(context, cityName, weathercode, temp1, temp2, weatherDesp, publishTime);
            //saveWeatherInfo(context,cityName,citycode,temp1,weatherDesp,publishTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        /**
         *  将服务器返回的所有天气信息存储到SharedPreferences 文件中。
         */


    private static void saveWeatherInfo(Context context, String cityName, String weathercode,
                                        String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor= (SharedPreferences.Editor)PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("cityName", cityName);
        editor.putString("weathercode", weathercode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weatherDesp", weatherDesp);
        editor.putString("publishTime", publishTime);
        editor.putString("currentDate", dateFormat.format(new Date()));
        editor.commit();


    }
}
