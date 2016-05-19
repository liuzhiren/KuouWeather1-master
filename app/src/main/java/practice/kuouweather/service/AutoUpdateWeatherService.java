package practice.kuouweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import practice.kuouweather.receiver.AutoUpdateWeatherRecivce;
import practice.kuouweather.util.HttpCallbackListener;
import practice.kuouweather.util.HttpUtil;
import practice.kuouweather.util.Utility;

/**
 * Created by a312689543 on 2016/4/16.
 */
public class AutoUpdateWeatherService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateWeather();
            }
        }).start();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;
        long tirgetTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateWeatherRecivce.class);
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,tirgetTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    private void UpdateWeather() {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode=pref.getString("weathercode", "");
        String adress= "http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        HttpUtil.sendRequestHttp(adress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateWeatherService.this,response);

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
