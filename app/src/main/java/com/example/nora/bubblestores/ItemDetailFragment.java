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


public class ItemDetailFragment extends Fragment {

    private int shop_id, item_id;
    ImageView item_image;
    TextView item_name, item_desc, item_price;

    private LinearLayout mView;
    private ProgressBar mProgressBar;

    Core core;
    private Picasso picasso;

    EditText input;

    public void setId(int id) {
        this.item_id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).toolbar.setTitle("Profile");
        core = new Core(getActivity());
        picasso = Picasso.with(getActivity());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        item_image = (ImageView) view.findViewById(R.id.shop_image);
        item_name = (TextView) view.findViewById(R.id.shop_name);
        item_desc = (TextView) view.findViewById(R.id.shop_desc);
        item_price = (TextView) view.findViewById(R.id.shop_phone);

        mView = (LinearLayout) view.findViewById(R.id.layout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mView.setVisibility(View.GONE);

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        shop_id = preferences.getInt("shopID",0);
        if (item_id != 0){
            new get_item_task().execute();
        }

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_item_image();
            }

        });

        item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_item("Name", item_name.getText().toString());
            }

        });

        item_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_item("Description", item_desc.getText().toString());
            }

        });

        item_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_item("Price", item_price.getText().toString());
            }

        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void edit_item(final String key, String value) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Edit"+key);
        alertDialog.setMessage("Enter new: "+key);

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

    private void edit_item_image() {
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
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                new edit_item_image_task(destination.getPath()).execute();

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

                new edit_item_image_task(selectedImagePath).execute();
            }
        }
    }

    private class editShopTask extends AsyncTask{

        String text,key;

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
            new get_item_task().execute();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (key.equals("Name")){
                core.editItem(shop_id, item_id, text, null, null, null, null, null, 1);
            }
            if (key.equals("Description")){
                core.editItem(shop_id, item_id, null, text, null, null, null, null, 1);
            }
            if (key.equals("Price")){
                core.editItem(shop_id, item_id, null, null, null, null, text, null, 1);
            }
            return null;
        }
    }

    private class edit_item_image_task extends AsyncTask{

        String path,pathUploaded;

        public edit_item_image_task(String path) {
            this.path = path;
        }

        @Override
        protected void onPostExecute(Object o) {
            new get_item_task().execute();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            pathUploaded = core.uploadImage(path);
            core.editItem(shop_id, item_id, null, null, null, null, null, pathUploaded, 1);
            return null;
        }
    }

    private class get_item_task extends AsyncTask{

        JSONObject jsonObject = null;

        @Override
        protected void onPostExecute(Object o) {
            try {
                if (jsonObject != null) {
                    if (!jsonObject.isNull("image")) {
                        picasso.load("http://zeowls.com/bubble/uploads/" + jsonObject.getString("image")).fit().centerCrop().into(item_image);
                    }
                    if (jsonObject.isNull("name")) {
                        item_name.setText("Add Name");

                    } else {
                        item_name.setText(jsonObject.getString("name"));
                    }

                    if (jsonObject.isNull("description")) {
                        item_desc.setText("Add Description");
                    } else {
                        item_desc.setText(jsonObject.getString("description"));
                    }

                    if (jsonObject.isNull("price")) {
                        item_price.setText("Add price");
                    } else {
                        item_price.setText("Price: "+ jsonObject.getString("price")+"$");
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
            jsonObject = core.getItem(item_id);
            return null;
        }
    }
}
