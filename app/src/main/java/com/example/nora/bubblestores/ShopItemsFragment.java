package com.example.nora.bubblestores;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
public class ShopItemsFragment extends Fragment {


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

        new loadingData().execute();

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
                JSONObject itemsJSON = core.getAllItems(id);
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
            super(inflater.inflate(R.layout.category_item_card, parent, false));

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
                ImageView image = (ImageView) holder.itemView.findViewById(R.id.imageView);
                TextView name = (TextView) holder.itemView.findViewById(R.id.name);
                TextView desc = (TextView) holder.itemView.findViewById(R.id.description);
                TextView price = (TextView) holder.itemView.findViewById(R.id.price);
                name.setText(items.get(position).getName());
                desc.setText(items.get(position).getDesc());
                price.setText("$" + items.get(position).getPrice());
                picasso.load(items.get(position).getImgUrl()).into(image);
            }


//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    FragmentManager fragmentManager = getFragmentManager();
//                    Item_Detail_Fragment endFragment= new Item_Detail_Fragment();
//                    endFragment.setId(items.get(position).getId());
//                    fragmentManager.beginTransaction()
//                            .add(R.id.fragment_main, endFragment)
//                            .addToBackStack(null)
//                            .commit();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
