package com.example.nora.bubblestores;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class RegistrationFragment extends Fragment {

    private OwnerRegisterTask mAuthTask = null;
    Button birthday, next;
    EditText name, email, password, confirmPassword;
    static int year, month, day;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            RegistrationFragment.year = year;
            RegistrationFragment.month = month + 1;
            RegistrationFragment.day = day;
            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.getDatePicker().setMinDate(883692000000L);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = (EditText) view.findViewById(R.id.nameText);
        email = (EditText) view.findViewById(R.id.emailText);
        password = (EditText) view.findViewById(R.id.passwordText);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPasswordText);

        birthday = (Button) view.findViewById(R.id.birthday);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        next = (Button) view.findViewById(R.id.nextBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(nameString)) {
                    name.setError(getString(R.string.error_field_required));
                    focusView = name;
                    cancel = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(emailString)) {
                    email.setError(getString(R.string.error_field_required));
                    focusView = email;
                    cancel = true;
                } else if (!isEmailValid(emailString)) {
                    email.setError(getString(R.string.error_invalid_email));
                    focusView = email;
                    cancel = true;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    password.setError(getString(R.string.error_field_required));
                    focusView = password;
                    cancel = true;
                } else if (!isPasswordValid(passwordString)) {
                    password.setError(getString(R.string.error_invalid_password));
                    focusView = password;
                    cancel = true;
                }

                if (TextUtils.isEmpty(confirmPasswordString)) {
                    confirmPassword.setError(getString(R.string.error_field_required));
                    focusView = confirmPassword;
                    cancel = true;
                } else if (!isPasswordValid(confirmPasswordString)) {
                    confirmPassword.setError(getString(R.string.error_invalid_password));
                    focusView = confirmPassword;
                    cancel = true;
                } else if ( !passwordString.equals(confirmPasswordString) ){
                    confirmPassword.setError(getString(R.string.error_confirm_passord_not_identical));
                    focusView = confirmPassword;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    mAuthTask = new OwnerRegisterTask(nameString, emailString, passwordString);
                    mAuthTask.execute();
                }

            }
        });
    }

    public class OwnerRegisterTask extends AsyncTask<Void, Void, Void> {

        private final String mName;
        private final String mEmail;
        private final String mPassword;

        OwnerRegisterTask(String name, String email, String password) {
            mName = name;
            mEmail = email;
            mPassword = password;
        }


        @Override
        protected Void doInBackground(Void... params) {
            int state = 0;
            Core core = new Core(getActivity());
            int user_id = core.registerOwner(mName, mPassword, mEmail, null, null, null);
            Log.d("new owner id: ", String.valueOf(user_id));
            return null;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
