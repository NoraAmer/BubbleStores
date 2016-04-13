package com.example.nora.bubblestores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class ShopProfileFragment extends Fragment {

    private int id;
    ImageView shopImage;
    TextView shopName, shopDesc, shopPhone, shopEmail, shopAddress;

    private LinearLayout mView;
    private ProgressBar mProgressBar;

//    public void setId(int id) {
//        this.id = id;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).toolbar.setTitle("Profile");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        shopImage = (ImageView) view.findViewById(R.id.shop_image);
        shopName = (TextView) view.findViewById(R.id.shop_name);
        shopDesc = (TextView) view.findViewById(R.id.shop_desc);
        shopPhone = (TextView) view.findViewById(R.id.shop_phone);
        shopEmail = (TextView) view.findViewById(R.id.shop_email);
        shopAddress = (TextView) view.findViewById(R.id.shop_address);

        mView = (LinearLayout) view.findViewById(R.id.layout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID",0);
        if (id != 0){
            new getShopTask().execute();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private class getShopTask extends AsyncTask{

        JSONObject jsonObject = null;

        @Override
        protected void onPostExecute(Object o) {
            try {
                if (jsonObject.isNull("name")) {
                    shopName.setText("Add Name");

                }else {
                    shopName.setText(jsonObject.getString("name"));
                }

                if (jsonObject.isNull("description")) {
                    shopDesc.setText("Add Description");
                }else {
                    shopDesc.setText(jsonObject.getString("description"));
                }

                if (jsonObject.isNull("mobile")) {
                    shopPhone.setText("Add Mobile");
                }else {
                    shopPhone.setText(jsonObject.getString("mobile"));
                }

                if (jsonObject.isNull("shop_address")) {
                    shopAddress.setText("Add Shop Address");
                }else {
                    shopAddress.setText(jsonObject.getString("shop_address"));
                }

                if (jsonObject.isNull("owner_email")) {
                    shopEmail.setText("Add Email");
                }else {
                    shopEmail.setText(jsonObject.getString("owner_email"));
                }

                mView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Core core = new Core(getActivity());
            jsonObject = core.getMyShop(id);
            return null;
        }
    }
}
