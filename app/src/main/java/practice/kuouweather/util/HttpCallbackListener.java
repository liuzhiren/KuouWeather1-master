package practice.kuouweather.util;

/**
 * Created by a312689543 on 2016/4/13.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
