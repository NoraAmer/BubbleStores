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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterShopFragment extends Fragment {

    private final int SELECT_FILE = 1;
    private final int REQUEST_CAMERA = 0;
    Spinner dropdown;
    EditText nameText, mobileText, addressText;
    Button add_Profile_Photo, add_Cover_Photo, registerBtn;
    //    ImageView Profile_Photo_Im, Cover_Photo_Im;
    int GlobalCode = 0;
    private Core core;

    private int id = 0;
    private String name, profile_pic, cover_pic, mobile,
            short_description, description, address;
    private int cat_id;
    private String longitude, latitude;

    private shopRegisterTask mAuthTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_shop_fr_layout_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        dropdown = (Spinner) view.findViewById(R.id.spinner);
        String[] categories = new String[]{"Crafts", "Home", "Accessory", "Artwork", "Clothing"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_layout, categories);
        dropdown.setAdapter(adapter);

        registerBtn = (Button) view.findViewById(R.id.finishBtn);
        nameText = (EditText) view.findViewById(R.id.name_text);
        mobileText = (EditText) view.findViewById(R.id.mobile_text);
        addressText = (EditText) view.findViewById(R.id.addressText);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString();
                mobile = mobileText.getText().toString();
                address = addressText.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(name)) {
                    mobileText.setError(getString(R.string.error_field_required));
                    focusView = mobileText;
                    cancel = true;
                }

                if (TextUtils.isEmpty(mobile)) {
                    mobileText.setError(getString(R.string.error_field_required));
                    focusView = mobileText;
                    cancel = true;
                }

                if (TextUtils.isEmpty(address)) {
                    addressText.setError(getString(R.string.error_field_required));
                    focusView = addressText;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    mAuthTask = new shopRegisterTask(name, null, null, mobile, null, null, address, 0, id
                            , null, null);
                    mAuthTask.execute();
                }
            }
        });

//        add_Profile_Photo = (Button) view.findViewById(R.id.add_photo);
//        add_Profile_Photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage(1);
//            }
//        });
//        Profile_Photo_Im = (ImageView) view.findViewById(R.id.item_image);
//
//        add_Cover_Photo = (Button) view.findViewById(R.id.add_cover_photo);
//        add_Cover_Photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage(2);
//            }
//        });
//        Cover_Photo_Im = (ImageView) view.findViewById(R.id.item_cover_image);
    }

    @Override
    public void onAttach(Context context) {
        core = new Core(context);
        super.onAttach(context);
    }

    public void setId(int id) {
        this.id = id;
    }

    public class shopRegisterTask extends AsyncTask<Void, Void, Void> {

        private final String mName;
        private final String mProfile;
        private final String mCover;
        private final String mMobile;
        private final String mShortDesc;
        private final String mDescription;
        private final String mAddress;
        private final String mLongitude;
        private final String mLatitude;
        private final int mCategoryId;
        private final int mOwnerId;

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        ShopProfileFragment shopProfileFragment;

        shopRegisterTask(String name, String profile_pic, String cover_pic, String mobile,
                         String short_description, String description, String address, int cat_id,
                         int owner_id, String longitude, String latitude) {
            mName = name;
            mProfile = profile_pic;
            mCover = cover_pic;
            mMobile = mobile;
            mShortDesc = short_description;
            mDescription = description;
            mAddress = address;
            mLongitude = longitude;
            mLatitude = latitude;
            mCategoryId = cat_id;
            mOwnerId = owner_id;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (id > 0) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE).edit();
                editor.putInt("shopID", id);
                editor.apply();
                fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction = fragmentManager.beginTransaction();
                shopProfileFragment = new ShopProfileFragment();
//                shopProfileFragment.setId(id);
                fragmentTransaction.replace(R.id.fragment_main, shopProfileFragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Core core = new Core(getActivity());
            int user_id = core.editShop(id, null, null, null, null, null, mName, null, null, null, null, null, null, null, mAddress);
            Log.d("new owner id: ", String.valueOf(user_id));
            return null;
        }
    }


//    private void selectImage(int code) {
//        GlobalCode = code;
//        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, REQUEST_CAMERA);
//                } else if (items[item].equals("Choose from Library")) {
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select File"),
//                            SELECT_FILE);
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_CAMERA) {
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                if (thumbnail != null) {
//                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//                }
//
//                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
//
//                FileOutputStream fo;
//                try {
//                    fo = new FileOutputStream(destination);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (GlobalCode == 1) {
//                    Profile_Photo_Im.setImageBitmap(thumbnail);
//                    core.uploadImage(destination.getPath());
//                } else if (GlobalCode == 2) {
//                    Cover_Photo_Im.setImageBitmap(thumbnail);
//                    core.uploadImage(destination.getPath());
//                }
//
//            } else if (requestCode == SELECT_FILE) {
//                Uri selectedImageUri = data.getData();
//                String[] projection = {MediaStore.MediaColumns.DATA};
//                CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null, null);
//                Cursor cursor = cursorLoader.loadInBackground();
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                cursor.moveToFirst();
//
//                String selectedImagePath = cursor.getString(column_index);
//
//                Bitmap bm;
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(selectedImagePath, options);
//                final int REQUIRED_SIZE = 200;
//                int scale = 1;
//                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                    scale *= 2;
//                options.inSampleSize = scale;
//                options.inJustDecodeBounds = false;
//                bm = BitmapFactory.decodeFile(selectedImagePath, options);
//
//                if (GlobalCode == 1) {
//                    Profile_Photo_Im.setImageBitmap(bm);
//                    core.uploadImage(selectedImagePath);
//                } else if (GlobalCode == 2) {
//                    Cover_Photo_Im.setImageBitmap(bm);
//                    core.uploadImage(selectedImagePath);
//                }
//            }
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
    }

}
