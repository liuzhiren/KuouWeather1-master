package practice.kuouweather.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import practice.kuouweather.R;
import practice.kuouweather.model.City;
import practice.kuouweather.model.CityListName;

/**
 * Created by a312689543 on 2016/4/30.
 */
public class CityListNameAdapter extends BaseAdapter {


        Context context;
        LayoutInflater mInflater ;
        private List<CityListName> strList = new ArrayList<CityListName>();
        private List<Boolean> boolList = new ArrayList<Boolean>();
        private boolean visflag;
    public CityListNameAdapter(Context c, List<CityListName> strList, List<Boolean> boolList, boolean visflag){
            context = c;
            mInflater = LayoutInflater.from(context);
            this.strList = strList;
            this.boolList = boolList;
            this.visflag = visflag;

        }
        public int getCount()
        {
            return strList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return strList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }


        @Override
        public View getView(final int position, View convertView,
            ViewGroup parent)
        {
            ViewHolder holder = null ;
            if(convertView == null)
            {

                holder = new ViewHolder();
                convertView  = mInflater.inflate(R.layout.city_name_list, null);
                holder.tv = (TextView)convertView.findViewById(R.id.textView);
                holder.cb = (CheckBox)convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(strList.get(position).getCityName());
            holder.cb.setChecked(boolList.get(position));

            if(visflag)
            {
                holder.cb.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.cb.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }




   public class ViewHolder
        {
            public TextView tv;
            public CheckBox cb;
        }

}

