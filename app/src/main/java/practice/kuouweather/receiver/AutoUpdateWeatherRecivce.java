package practice.kuouweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import practice.kuouweather.service.AutoUpdateWeatherService;

/**
 * Created by a312689543 on 2016/4/16.
 */
public class AutoUpdateWeatherRecivce extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, AutoUpdateWeatherService.class);
        context.startService(i);

    }
}
