package practice.kuouweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import practice.kuouweather.db.CoolWeatherOpenHelper;

/**
 * Created by a312689543 on 2016/4/13.
 */
public class CoolWeatherDB {
    /*
    * 把常用的数据库操作封装起来
    * *//*
    * 设置数据库名称
    * */
    private static final String DB_name="cool_weather2";
    /*设置数据库版本
    * */
    private static final int version=7;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;
    /**
     * 将构造方法私有化
     */
    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,DB_name,null,version);
        db=dbHelper.getWritableDatabase();
    }
    /**
     * 获取CoolWeatherDB的实例。
     */
    public synchronized static CoolWeatherDB getInstance(Context context){// synchronizedJava语言的关键字，当它用来修饰一个方法或者一个代码块的时候，能够保证在同一时刻最多只有一个线程执行该段代码。
        if(coolWeatherDB==null){
            coolWeatherDB=new CoolWeatherDB(context);

        }
        return coolWeatherDB;
    }
    /**
     *  将Province实例存储到数据库。
     */
    public void saveProvince(Province province){
        if(province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }

    }
    /**
     * 从数据库读取全国所有的省份信息。
     *//*
     * query() 方法参数         对应 SQL部分                     描述
                table       from table_name                 指定查询的表名
                columns     select column1, column2         指定查询的列名
                selection   where column = value            指定 where的约束条件
                selectionArgs       -                       为 where中的占位符提供具体的值
                groupBy     group by column                 指定需要 group by 的列
                having      having column = value           对 group by 后的结果进一步约束
                orderBy o   rder by column1, column2        指定查询结果的排序方式
               查询出来的cursor的初始位置是指向第一条记录的前一个位置的
                cursor.moveToFirst（）指向查询结果的第一个位置。
                一般通过判断cursor.moveToFirst()的值为true或false来确定查询结果是否为空。
                cursor.moveToNext()是用来做循环的，一般这样来用：while(cursor.moveToNext()){ }
                cursor.moveToPrevious()是指向当前记录的上一个记录，是和moveToNext相对应的；
                cursor.moveToLast()指向查询结果的最后一条记录
                使用cursor可以很方便的处理查询结果以便得到想要的数据 */
    public List<Province> LoadProvinces(){
        List<Province> list=new ArrayList<Province>();//对象数组
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /**
     *  将City实例存储到数据库。
     */
    public void saveCity(City city){
        if(city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }
    /**
     * 从数据库读取某个省份所有城市的信息。
     */
    public List<City> LoadCities(int provinceId){
        List<City> list=new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /*
    * 将county实例储存刀数据库
    * */
    public void saveCounty(County county){
        if(county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values);
        }
    }
    /*
    读取某个城市下所有的县的信息；
    */
    public List<County> LoadCounties(int cityId){
        List<County> list=new ArrayList<County>();
        Cursor cursor=db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                list.add(county);
            }while (cursor.moveToNext());
        }
        return list;
    }
    public void saveCityListName(CityListName cityListName){
        boolean flag = false;
        if(cityListName!=null){
            Cursor cursor = db.query("CityListName",null,"city_list_name=?",new String[]{cityListName.getCityName()},null,null,null);
            if(cursor.moveToFirst()){
                do{
                    flag = true;
                }while (cursor.moveToNext());

            }if(!flag){
                ContentValues values=new ContentValues();
                Log.d("addCity",cityListName.getCityName());
                values.put("city_list_name",cityListName.getCityName());
                values.put("city_list_code",cityListName.getCityCode());
                db.insert("CityListName", null, values);
            }
        }
    }
    public List<CityListName> LoadCityNameList(){
        List<CityListName> list=new ArrayList<CityListName>();//对象数组
        Log.d("addCitySize：",""+list.size());
        Cursor cursor=db.query("CityListName",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                CityListName cityListName = new CityListName();
                cityListName.setId(cursor.getInt(cursor.getColumnIndex("id")));
                cityListName.setCityName(cursor.getString(cursor.getColumnIndex("city_list_name")));
                cityListName.setCityCode(cursor.getString(cursor.getColumnIndex("city_list_code")));
                list.add(cityListName);
            }while (cursor.moveToNext());
        }Log.d("addCity",""+list.size());
        return list;
    }
    public void deleteCityListName(String cityname){

            db.delete("CityListName", "city_list_name = ?", new String[]{cityname});

    }




}
