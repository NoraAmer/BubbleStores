package com.example.nora.bubblestores;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public ActionBar supportActionBar;
    public AppBarLayout appBarLayout;
    public Toolbar toolbar;
    NavigationView navigationView;

    TextView shopNameNav;
    CircularImageView imageViewNav;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ShopProfileFragment shopProfileFragment;
    LoginFragment2 loginFragment;
    RegistrationFragment registrationFragment;
    AddItemFragment addItemFragment;
    RegisterShopFragment registerShopFragment;
    MainIntroFragment mainIntroFragment;
    ShopItemsFragment shopItemsFragment;
    ShopHomePage shopHomePage;
    MainGridViewFragment mainGridViewFragment;

    int id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID", 0);

        configureToolbar();
        configureNavigationView();
        configureDrawer();

        if (id != 0) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            shopHomePage = new ShopHomePage();
            shopProfileFragment = new ShopProfileFragment();
            mainGridViewFragment = new MainGridViewFragment();
            fragmentTransaction.replace(R.id.fragment_main, mainGridViewFragment,"MainGridViewFragment");
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();




        } else {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            mainIntroFragment = new MainIntroFragment();
            fragmentTransaction.replace(R.id.fragment_main, mainIntroFragment);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }


    }

    private void configureToolbar() {
        // Adding Toolbar to Main screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        // Create Navigation drawer and inlfate layout
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Adding menu icon to Toolbar
        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void configureNavigationView() {
        // Set behavior of Navigation drawer
        assert navigationView != null;
        View header = navigationView.getHeaderView(0);
        shopNameNav = (TextView) header.findViewById(R.id.nameNavText);
        imageViewNav = (CircularImageView) header.findViewById(R.id.imageNav);

        SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID", 0);

        MenuItem logout = navigationView.getMenu().findItem(R.id.navLogoutBTN);

            shopNameNav.setVisibility(View.VISIBLE);
            imageViewNav.setVisibility(View.VISIBLE);
            logout.setVisible(true);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        if (menuItem.getItemId() == R.id.navLogoutBTN) {
                            SharedPreferences.Editor editor = getSharedPreferences("Credentials", Context.MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            MainActivity.this.configureNavigationView();
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            mainIntroFragment = new MainIntroFragment();
                            fragmentTransaction.replace(R.id.fragment_main, mainIntroFragment);
                            fragmentTransaction.commit();
                            mDrawerLayout.closeDrawers();
                            return true;
                        }


                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void configureDrawer() {
        // Configure drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

//        if (id == android.R.id.home) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
