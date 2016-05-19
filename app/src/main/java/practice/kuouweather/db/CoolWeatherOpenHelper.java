package practice.kuouweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a312689543 on 2016/4/13.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVINCE="create table Province("
            +"id integer primary key  autoincrement,"
            +"province_name text ,"
            +"province_code text)";
    /**
     * City表建表语句
     */
    public static final String CREATE_CITY="create table City("
            +"id integer primary key  autoincrement,"
            +"city_name text ,"
            +"city_code text,"
            +"province_id integer)";
    /**
     * County表建表语句
     */
    public static final String CREATE_COUNTY="create table County("
            +"id integer primary key  autoincrement,"
            +"county_name text ,"
            +"county_code text,"
            +"city_id integer)";
    /*
    添加的城市名称列表
     */
    public static final String CREATE_CITYLIST="create table CityListName("
            +"id integer primary key  autoincrement,"
            +"city_list_code text,"
            +"city_list_name text )";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);//创建Province表
        db.execSQL(CREATE_CITY);//创建City表
        db.execSQL(CREATE_COUNTY);//创建County表
        db.execSQL(CREATE_CITYLIST);//创建添加的城市名称列表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Province");
        db.execSQL("drop table if exists City");
        db.execSQL("drop table if exists County");
        db.execSQL("drop table if exists CityListName");
        onCreate(db);

    }
}
