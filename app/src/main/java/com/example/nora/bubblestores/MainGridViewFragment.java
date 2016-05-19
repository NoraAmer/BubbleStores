package com.example.nora.bubblestores;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by nezar on 5/17/16.
 */
public class MainGridViewFragment extends Fragment {

    int Shop_ID = 0;

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


    public void setShopId(int id) {
        this.Shop_ID = id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_grid_view, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new HomePageGridAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                switch (position) {
                    case 0:
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        shopHomePage = new ShopHomePage();
//                        shopProfileFragment = new ShopProfileFragment();
//                        fragmentTransaction.replace(R.id.fragment_main, shopProfileFragment);
//                        fragmentTransaction.commit();

                        shopProfileFragment = new ShopProfileFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .hide(getFragmentManager().findFragmentByTag("MainGridViewFragment"))
                                .add(R.id.fragment_main, shopProfileFragment, "shopProfileFragment")
                                .addToBackStack(null)
                                .commit();

                        break;

                    case 1:
                        addItemFragment = new AddItemFragment();
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .hide(getFragmentManager().findFragmentByTag("MainGridViewFragment"))
                                .add(R.id.fragment_main, addItemFragment, "AddItemFragment")
                                .addToBackStack(null)
                                .commit();
                        break;


                    case 2:
                        shopItemsFragment = new ShopItemsFragment();
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .hide(getFragmentManager().findFragmentByTag("MainGridViewFragment"))
                                .add(R.id.fragment_main, shopItemsFragment, "ShopItemsFragment")
                                .addToBackStack(null)
                                .commit();

                        break;

                    default:
                        break;
                }


            }
        });
    }
}
