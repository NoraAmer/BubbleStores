package com.example.nora.bubblestores;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ButtonBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by nezar on 4/14/16.
 */
public class MainIntroFragment extends Fragment {


    Button GetStartedBtn;
    TextView Sign_In_text;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LoginFragment2 loginFragment2;
    LoginFragment loginFragment1;
    RegistrationFragment registrationFragment;

    LoginAndREgisterContainer loginAndREgisterContainer;
    RegisterShopFragment registerShopFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_intro, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);

        GetStartedBtn = (Button) view.findViewById(R.id.GetStartedBtn);
        Sign_In_text = (TextView) view.findViewById(R.id.Sign_In_text);

        Sign_In_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                loginFragment2 = new LoginFragment2();
                loginFragment1 = new LoginFragment();
                fragmentTransaction.replace(R.id.fragment_main, loginFragment2);;
                fragmentTransaction.commit();
            }
        });

        GetStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                registrationFragment = new RegistrationFragment();
                fragmentTransaction.replace(R.id.fragment_main, registrationFragment);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
    }
}
