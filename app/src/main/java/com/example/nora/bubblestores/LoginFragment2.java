package com.example.nora.bubblestores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginFragment2 extends Fragment {

    EditText email_text, password_text;
    Button sign_in;
    TextView register_text;

    String email,password;

    SignInTask mAuthTask;
    View focusView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        email_text = (EditText) view.findViewById(R.id.email);
        password_text = (EditText) view.findViewById(R.id.password);
        sign_in = (Button) view.findViewById(R.id.email_sign_in_button);
        register_text = (TextView) view.findViewById(R.id.register);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_text.getText().toString();
                password = password_text.getText().toString();

                boolean cancel = false;
                focusView = null;

                if (TextUtils.isEmpty(email)) {
                    email_text.setError(getString(R.string.error_field_required));
                    focusView = email_text;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)) {
                    password_text.setError(getString(R.string.error_field_required));
                    focusView = password_text;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    mAuthTask = new SignInTask(email, password);
                    mAuthTask.execute();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public class SignInTask extends AsyncTask<Void, Void, Void> {

        private final String mEmail;
        private final String mPassword;
        int response = 0;

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        ShopProfileFragment shopProfileFragment;
        ShopHomePage shopHomePage;

        SignInTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (response == 0) {
                Toast.makeText(getActivity(), "Error try again", Toast.LENGTH_SHORT).show();
            }else if (response == -1){
                password_text.setError(getActivity().getString(R.string.wrong_password));
                focusView = password_text;
                focusView.requestFocus();
            }else if (response == -2){
                email_text.setError(getActivity().getString(R.string.no_matching_email));
                focusView = email_text;
                focusView.requestFocus();
            } else {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE).edit();
                editor.putInt("shopID", response);
                editor.apply();
                ((MainActivity) getActivity()).configureNavigationView();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = fragmentManager.beginTransaction();
                shopHomePage = new ShopHomePage();
                shopProfileFragment =  new ShopProfileFragment();
                fragmentTransaction.replace(R.id.fragment_main, shopProfileFragment);
                fragmentTransaction.commit();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Core core = new Core(getActivity());
            response = core.logIn(mEmail,mPassword);
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
    }
}
