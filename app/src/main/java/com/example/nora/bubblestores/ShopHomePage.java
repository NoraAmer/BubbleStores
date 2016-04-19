package com.example.nora.bubblestores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ShopHomePage extends Fragment {

    int Shop_Id;
    ImageView shopImage;
    TextView shopName, shopDesc, shopPhone, shopEmail, shopAddress;

    private LinearLayout mView;
    private ProgressBar mProgressBar;

    Core core;
    private Picasso picasso;

    ShopItemsFragment shopItemsFragment;

    EditText input;
    TabLayout tabLayout;
    ViewPager viewPager;


    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    CoordinatorLayout rootLayout;
    public ActionBar supportActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("On Create View", " On");
        return inflater.inflate(R.layout.fragment_shop_profile_2, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tabLayout = (TabLayout) view.findViewById(R.id.detail_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        Log.d("On View Created"," On ");

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.name, R.string.name);
        drawerLayout.setDrawerListener(drawerToggle);

        supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.main_content);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Bubble");


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        Shop_Id = preferences.getInt("shopID", 0);
        Log.d("Shop ID", String.valueOf(Shop_Id));
        setupViewPager(viewPager);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        Log.d("On Activity Created ","On");

        drawerToggle.syncState();

    }


    @Override
    public void onResume() {

        Log.d("On Resume", " On");
        super.onResume();
    }


    private void setupViewPager(ViewPager viewPager) {
        SamplePagerAdapter adapter = new SamplePagerAdapter(getFragmentManager());
        adapter.addFragment(new ShopProfileFragment(), "Profile");
        shopItemsFragment = new ShopItemsFragment();
        shopItemsFragment.setId(Shop_Id);
        adapter.addFragment(shopItemsFragment, "Items");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);


    }


}
