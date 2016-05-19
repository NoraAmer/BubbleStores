package com.example.nora.bubblestores;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nezar on 5/17/16.
 */
public class HomePageGridAdapter extends BaseAdapter {
    private Context mContext;


    ArrayList<HomeDataModel> List;


    public HomePageGridAdapter(Context c) {
        mContext = c;
        List = new ArrayList<HomeDataModel>();
        Resources res = c.getResources();
        String[] HomeNames = res.getStringArray(R.array.Home_Array);
        int[] HomeImages = {
                R.drawable.shop_home, R.drawable.shop_add_order,
                R.drawable.shop_all_items, R.drawable.shop_orders,

        };

        for (int i = 0; i < 4; i++) {
            HomeDataModel homeDataModel = new HomeDataModel(HomeImages[i], HomeNames[i]);
            List.add(homeDataModel);
        }


    }

    public int getCount() {
        return List.size();
    }

    public Object getItem(int position) {
        return List.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;
        ViewHolder holder=null;
        if (view==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  inflater.inflate(R.layout.grid_view_item,parent,false);
            holder = new ViewHolder(view);
            view.setTag(holder);


        }else {
           holder = (ViewHolder) view.getTag();

        }

        HomeDataModel homeDataModel  = List.get(position);
        holder.Grid_Image.setImageResource(homeDataModel.ImageId);
        holder.Grid_Name.setText(homeDataModel.Itemname);
        holder.Grid_Name.setTag(homeDataModel);





        return view;
    }

    class ViewHolder{

        ImageView Grid_Image ;
        TextView Grid_Name;

        ViewHolder(View v){
            Grid_Image = (ImageView) v.findViewById(R.id.Grid_Image);
            Grid_Name = (TextView) v.findViewById(R.id.Grid_Name);


       }
    }


}
