package practice.kuouweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import practice.kuouweather.R;
import practice.kuouweather.db.CoolWeatherOpenHelper;
import practice.kuouweather.model.City;
import practice.kuouweather.model.CityListName;
import practice.kuouweather.model.CoolWeatherDB;
import practice.kuouweather.model.County;
import practice.kuouweather.model.Province;
import practice.kuouweather.util.HttpCallbackListener;
import practice.kuouweather.util.HttpUtil;
import practice.kuouweather.util.Utility;

public class ChooseAreaActivity extends Activity {
    private static final int LEVEL_PROVINCE=0;
    private static final int LEVEL_CITY=1;
    private static final int LEVEL_COUNTY=2;
    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private TextView mTextView;
    private CoolWeatherDB mCoolWeatherDB;
    private List<String> datalist=new ArrayList<String>();
    /**
     * 省列表
     */
    private List<Province> mProvinceList;
    /**
     * 市列表
     */
    private List<City> mCityList;
    /**
     * 县列表
     */
    private List<County> mCountyList;
    /*
    * 当前选中的省
    * */
    private Province selectProvince;
     /*
    * 当前选中的市
    * */
    private City selectCity;
    /*
    * 当前选中的级别
    * */
    private int currentLevel;
    //是否从weatherActivity中跳转过来的标识
    private boolean isFromWeatherActivity;
    //是否从ListCityActivity中跳转过来的标识
    private boolean isFromListCityActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity",false);
        isFromListCityActivity = getIntent().getBooleanExtra("from_list_city_activity",false);
        Log.d("haha","isFromListCityActivity is"+isFromListCityActivity);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("city_selected",false)&& !isFromWeatherActivity && !isFromListCityActivity){
            Intent intent=new Intent(this,WeatherLActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.choose_area);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mListView=(ListView)findViewById(R.id.list_view);
        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        mTextView=(TextView)findViewById(R.id.title_text);
        mListView.setAdapter(mAdapter);
        mCoolWeatherDB=CoolWeatherDB.getInstance(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectProvince=mProvinceList.get(position);
                    queryCities();//选中某个省份点击后显示出该省份所有的市
                }
                else if (currentLevel==LEVEL_CITY){
                    selectCity=mCityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY && isFromListCityActivity ){
                    String countyName=mCountyList.get(position).getCountyName();
                    String countyCode = mCountyList.get(position).getCountyCode();
                    Intent intent=new Intent(ChooseAreaActivity.this,ListCityActivity.class);
                    intent.putExtra("county_name", countyName);
                    intent.putExtra("from_chosse_city_activity", true);
                    Log.d("addCity", countyName);
                    CityListName cityListName = new CityListName();
                    cityListName.setCityName(countyName);
                    cityListName.setCityCode(countyCode);
                    Log.d("addCity ","cityListName is null?"+cityListName);
                    mCoolWeatherDB.saveCityListName(cityListName);

                    startActivity(intent);
                    finish();
                }
                else if(currentLevel==LEVEL_COUNTY && isFromWeatherActivity ) {
                    String countyCode = mCountyList.get(position).getCountyCode();
                    String countyName = mCountyList.get(position).getCountyName();
                    Log.d("Main", countyCode);
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherLActivity.class);
                    intent.putExtra("county_code", countyCode);
                    CityListName cityListName = new CityListName();
                    cityListName.setCityName(countyName);
                    cityListName.setCityCode(countyCode);
                    mCoolWeatherDB.saveCityListName(cityListName);
                    startActivity(intent);
                    finish();
                }
                else if(currentLevel==LEVEL_COUNTY  ) {
                    String countyCode = mCountyList.get(position).getCountyCode();
                    String countyName = mCountyList.get(position).getCountyName();
                    Log.d("Main", countyCode);
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherLActivity.class);
                    intent.putExtra("county_code", countyCode);
                    CityListName cityListName = new CityListName();
                    cityListName.setCityName(countyName);
                    mCoolWeatherDB.saveCityListName(cityListName);
                    startActivity(intent);
                    finish();
                }

            }
        });queryProvinces();
    }
    //优先从数据库获取所有省份数据如果没有数据从服务器上获取

    private void queryProvinces() {
        mProvinceList=mCoolWeatherDB.LoadProvinces();
        if(mProvinceList.size()>0){
            datalist.clear();
            for(Province c:mProvinceList){
                datalist.add(c.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);//表示将列表移动到指定的Position处。
            mTextView.setText("中国");
            currentLevel=LEVEL_PROVINCE;

        }else {
            queryFromServe(null, "province");
        }
    }
    //优先从数据库获取某个省份所有市的数据信息，否则从服务器获取
    private void queryCities() {
        mCityList=mCoolWeatherDB.LoadCities(selectProvince.getId());
        if(mCityList.size()>0){
            datalist.clear();
            for(City c:mCityList){
                datalist.add(c.getCityName());
            }
            currentLevel=LEVEL_CITY;
            mAdapter.notifyDataSetChanged();//// 适配器的内容改变时需要强制调用getView来刷新每个Item的内容
            mListView.setSelection(0);
            mTextView.setText(selectProvince.getProvinceName());

        }else {
            queryFromServe(selectProvince.getProvinceCode(),"city");
        }
    }
    private void queryCounties() {
        mCountyList=mCoolWeatherDB.LoadCounties(selectCity.getId());
        if(mCountyList.size()>0){
            datalist.clear();
            for(County c:mCountyList){
                datalist.add(c.getCountyName());
            }
            currentLevel=LEVEL_COUNTY;
            mAdapter.notifyDataSetChanged();//// 适配器的内容改变时需要强制调用getView来刷新每个Item的内容
            mListView.setSelection(0);
            mTextView.setText(selectCity.getCityName());

        }else {
            queryFromServe(selectCity.getCityCode(),"county");
        }

    }

    //从服务器获取 数据
    private void queryFromServe(final String code, final String type) {
        String adress;
        if(!TextUtils.isEmpty(code)){
            adress="http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else {
            adress="http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendRequestHttp(adress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result=false;
                if("province".equals(type)){
                    result=Utility.handleProvincesResponse(mCoolWeatherDB,response);
                }else if("city".equals(type)){
                    result=Utility.handleCitiesResponse(mCoolWeatherDB,response,selectProvince.getId());
                }else if("county".equals(type)){
                    result=Utility.handleCountiesResponse(mCoolWeatherDB,response,selectCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        // 通过runOnUiThread()方法回到主线程处理逻辑
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }

                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败...",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private void showProgressDialog() {;
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }

    }
    @Override
    public void onBackPressed(){
        if(currentLevel==LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel==LEVEL_CITY){
            queryProvinces();
        }else {
            if(isFromWeatherActivity){
                Intent intent=new Intent(this,WeatherLActivity.class);
                startActivity(intent);

            }
            finish();
        }
    }



}
