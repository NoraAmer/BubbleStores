package com.example.nora.bubblestores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ShopProfileFragment extends Fragment {

    private int id;
    ImageView shopImage;
    TextView shopName, shopDesc, shopPhone, shopEmail, shopAddress,Shop_Profile_Short_Desc_tv;

    private RelativeLayout mView;
    private ProgressBar mProgressBar;

    Core core;
    private Picasso picasso;

    EditText input;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_profile_3, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);


        core = new Core(getActivity());
        picasso = Picasso.with(getActivity());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).toolbar.setTitle("Buuble Stores");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        shopImage = (ImageView) view.findViewById(R.id.shop_image);
        shopName = (TextView) view.findViewById(R.id.shop_name);
        shopDesc = (TextView) view.findViewById(R.id.shop_desc);
        shopPhone = (TextView) view.findViewById(R.id.shop_phone);
        shopEmail = (TextView) view.findViewById(R.id.shop_email);
        shopAddress = (TextView) view.findViewById(R.id.shop_address);
        Shop_Profile_Short_Desc_tv = (TextView) view.findViewById(R.id.Shop_Profile_Short_Desc_tv);

        mView = (RelativeLayout) view.findViewById(R.id.layout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mView.setVisibility(View.GONE);

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID", 0);
        if (id != 0) {
            new getShopTask().execute();
        }

        Shop_Profile_Short_Desc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop("Slogan", Shop_Profile_Short_Desc_tv.getText().toString());

            }
        });

        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopImage();
            }

        });

        shopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop("Name", shopName.getText().toString());
            }

        });

        shopDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop("Description", shopDesc.getText().toString());
            }

        });

        shopPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop("Phone", shopPhone.getText().toString());
            }

        });

        shopEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop("Email", shopEmail.getText().toString());
            }

        });

        shopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop("Address", shopAddress.getText().toString());
            }

        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void editShop(final String key, String value) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Edit" + key);
        alertDialog.setMessage("Enter new: " + key);

        input = new EditText(getActivity());
        input.setText(value);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_item_name);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new editShopTask(key).execute();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private final int SELECT_FILE = 1;
    private final int REQUEST_CAMERA = 0;

    private void editShopImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if (thumbnail != null) {
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                }

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new editShopImageTask(destination.getPath()).execute();

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

//                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
//                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                new editShopImageTask(selectedImagePath).execute();
            }
        }
    }

    private class editShopTask extends AsyncTask {

        String text, key;

        @Override
        protected void onPreExecute() {
            text = input.getText().toString();
            super.onPreExecute();
        }

        public editShopTask(String key) {
            this.key = key;
        }

        @Override
        protected void onPostExecute(Object o) {
            new getShopTask().execute();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (key.equals("Name")) {
                core.editShop(id, null, null, null, null, null, text, null, null, null, null, null, null, null, null);
            }
            if (key.equals("Description")) {
                core.editShop(id, null, null, null, null, null, null, null, null, null, null, text, null, null, null);
            }
            if (key.equals("Phone")) {
                core.editShop(id, null, null, null, null, null, null, null, null, text, null, null, null, null, null);
            }
            if (key.equals("Email")) {
                core.editShop(id, null, text, null, null, null, null, null, null, null, null, null, null, null, null);
            }
            if (key.equals("Address")) {
                core.editShop(id, null, null, null, null, null, null, null, null, null, null, null, null, null, text);
            }if (key.equals("Slogan")) {
                core.editShop(id, null, null, null, null, null, null, null, null, null, text, null, null, null, null);
            }
            return null;
        }
    }

    private class editShopImageTask extends AsyncTask {

        String path, pathUploaded;

        public editShopImageTask(String path) {
            this.path = path;
        }

        @Override
        protected void onPostExecute(Object o) {
            new getShopTask().execute();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            pathUploaded = core.uploadImage(path);
            core.editShop(id, null, null, null, null, null, null, pathUploaded, null, null, null, null, null, null, null);
            return null;
        }
    }

    private class getShopTask extends AsyncTask {

        JSONObject jsonObject = null;

        @Override
        protected void onPostExecute(Object o) {
            try {
                if (jsonObject != null) {
                    if (!jsonObject.isNull("profile_pic")) {
                        picasso.load("http://zeowls.com/bubble/uploads/" + jsonObject.getString("profile_pic")).fit().centerCrop().into(shopImage);
                    }
                    if (jsonObject.isNull("name")) {
                        shopName.setText("Add Name");

                    } else {
                        shopName.setText(jsonObject.getString("name"));
                    }

                    if (jsonObject.isNull("description")) {
                        shopDesc.setText("Add Description");
                    } else {
                        shopDesc.setText(jsonObject.getString("description"));
                    }

                    if (jsonObject.isNull("short_description")) {
                        Shop_Profile_Short_Desc_tv.setText("Add Slogan");
                    } else {
                        Shop_Profile_Short_Desc_tv.setText(jsonObject.getString("short_description"));
                    }

                    if (jsonObject.isNull("mobile")) {
                        shopPhone.setText("Add Mobile");
                    } else {
                        shopPhone.setText(jsonObject.getString("mobile"));
                    }

                    if (jsonObject.isNull("shop_address")) {
                        shopAddress.setText("Add Shop Address");
                    } else {
                        shopAddress.setText(jsonObject.getString("shop_address"));
                    }

                    if (jsonObject.isNull("owner_email")) {
                        shopEmail.setText("Add Email");
                    } else {
                        shopEmail.setText(jsonObject.getString("owner_email"));
                    }
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


    @Override
    public void onPause() {
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        super.onPause();
    }
}
