package com.example.nora.bubblestores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nezar on 4/17/16.
 */
public class Shop_Order_Fragment extends Fragment {


    static ArrayList<ItemDataMode> items = new ArrayList<>();
    RecyclerView recyclerView;
    ContentAdapter adapter;
    Context context;
    int id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new ContentAdapter();

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID", 0);
        if (id!=0){
            new loadingData().execute();

        }


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return recyclerView;
    }


    public void setId(int id) {
        this.id = id;
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            items.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getShopOrdersByShopId(id);
                if (core.getAllItems(id) != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    Log.d("json", core.getAllItems(id).toString());
                    for (int i = 0; i < itemsJSON.getJSONArray("Items").length(); i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode item1 = new ItemDataMode();
                        item1.setCatId(item.getInt("cat_id"));
                        item1.setDesc(item.getString("description"));
                        item1.setId(item.getInt("id"));
                        item1.setName(item.getString("name"));
                        item1.setPrice(item.getString("price"));
                        item1.setShopId(item.getInt("shop_id"));
                        item1.setShortDesc(item.getString("short_description"));
                        item1.setImgUrl(item.getString("image"));
                        items.add(item1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.shop_order_item_card, parent, false));

        }
    }


    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        Picasso picasso;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            picasso = Picasso.with(context);
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // no-op
            if (items.size() != 0) {
                ItemDataMode item = items.get(position);
                ImageView image = (ImageView) holder.itemView.findViewById(R.id.imageView);
                TextView name = (TextView) holder.itemView.findViewById(R.id.name);
                TextView desc = (TextView) holder.itemView.findViewById(R.id.description);
                TextView price = (TextView) holder.itemView.findViewById(R.id.price);
                if (item.getName().equals("null") || item.getName().isEmpty()) {
                    name.setText("No Name");
                } else {
                    name.setText(item.getName());
                }
                if (item.getDesc().equals("null") || item.getDesc().isEmpty()) {
                    desc.setText("No Description");
                } else {
                    desc.setText(item.getDesc());
                }
                if (item.getPrice().equals("null") || item.getPrice().isEmpty()) {
                    price.setText("NA");
                } else {
                    price.setText("$" + item.getPrice());
                }
                picasso.load(item.getImgUrl()).into(image);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = getFragmentManager();
                    ItemDetailFragment endFragment = new ItemDetailFragment();
                    endFragment.setId(items.get(position).getId());
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_main, endFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
