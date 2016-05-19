package practice.kuouweather.activity;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import practice.kuouweather.R;
import practice.kuouweather.db.CoolWeatherOpenHelper;
import practice.kuouweather.model.City;
import practice.kuouweather.model.CityListName;
import practice.kuouweather.model.CoolWeatherDB;
import practice.kuouweather.model.Province;
import practice.kuouweather.util.CityListNameAdapter;

/**
 * Created by a312689543 on 2016/4/29.
 */


public class ListCityActivity extends Activity implements View.OnClickListener {
    private CityListNameAdapter ladapter;
    private List<Boolean> boolList = new ArrayList<Boolean>();
    boolean visflag = false;
    private CheckBox cb;

    /*
 初始化ListCity
  */
    private ListView listcity;
    /*
    储存城市
     */


    private List<String> datalist = new ArrayList<String>();
    //private CityListNameAdapter madapter;
    /*
    * 添加城市
     */
    private Button addCity;
    /*
    删除城市
     */
    private Button deleteCity;


    private ArrayAdapter<String> mAdapter;

    private CoolWeatherDB mCoolWeatherDB;
    private RelativeLayout relation;
    private Button cancle,delete;
    private TextView txtcount ;
    private List<CityListName> cityNameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_city);

        relation = (RelativeLayout)findViewById(R.id.relative);
        delete = (Button)findViewById(R.id.delete);
        cancle = (Button)findViewById(R.id.cancle);
        txtcount = (TextView)findViewById(R.id.txtcount);
        addCity = (Button)findViewById(R.id.add_city);
        deleteCity = (Button)findViewById(R.id.delete_city);
        listcity = (ListView)findViewById(R.id.list_city);
        mCoolWeatherDB=CoolWeatherDB.getInstance(this);
        cityNameList = mCoolWeatherDB.LoadCityNameList();
        ladapter = new CityListNameAdapter(this,cityNameList,boolList,visflag);
        listcity.setAdapter(ladapter);
        addCity.setOnClickListener(this);
        deleteCity.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancle.setOnClickListener(this);
        listcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(visflag)
                {
                    CityListNameAdapter.ViewHolder viewHolder = (CityListNameAdapter.ViewHolder) view.getTag();
                    viewHolder.cb.toggle();
                    if(viewHolder.cb.isChecked())
                    {

                        boolList.set(position, true);

                    }else{
                        boolList.set(position, false);
                    }
                }else {
                    Intent intent = new Intent(ListCityActivity.this,WeatherLActivity.class);
                    intent.putExtra("county_code",cityNameList.get(position).getCityCode());
                    startActivity(intent);
                    finish();

                }

            }
        }); initCity();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_city:
                Intent intent = new Intent(ListCityActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_list_city_activity", true);
                startActivity(intent);
                break;
            case R.id.delete_city:
                    visflag = true;
                    relation.setVisibility(View.VISIBLE);
                    ladapter = new CityListNameAdapter(this,cityNameList,boolList,visflag);
                    listcity.setAdapter(ladapter);

                break;
            case R.id.cancle:
                boolList.clear();
                listcity.setAdapter(ladapter);
                relation.setVisibility(View.INVISIBLE);
                break;
            case R.id.delete:
                if(boolList.size()>0)
                {
                    if(visflag)
                    {
                        for(int location=0; location<boolList.size(); )
                        {
                            if(boolList.get(location))
                            {
                                boolList.remove(location);
                                mCoolWeatherDB.deleteCityListName(cityNameList.get(location).getCityName());
                                cityNameList.remove(location);
                                continue;
                            }
                            location++;
                        }
                    }

                }
                this.ladapter.notifyDataSetChanged();
                relation.setVisibility(View.INVISIBLE);
                visflag = false;
                ladapter = new CityListNameAdapter(this,cityNameList,boolList,visflag);
                listcity.setAdapter(ladapter);
                break;

        default:
        }
    }
    private void initCity() {
        try {
            //initCitys();
            Log.d("addCity","是否进了TRY");
            cityNameList=mCoolWeatherDB.LoadCityNameList();
            Log.d("addCity","size"+cityNameList.size());
            if(cityNameList.size()>0){
                for(CityListName c:cityNameList){
                    boolList.add(false);
                }
                ladapter.notifyDataSetChanged();//// 适配器的内容改变时需要强制调用getView来刷新每个Item的内容
                listcity.setSelection(0);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("addCity","异常");
        }

    }




}


