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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddItemFragment extends Fragment {

    private final int SELECT_FILE = 1;
    private final int REQUEST_CAMERA = 0;
    Spinner dropdown;
    Button addPhoto;
    ImageView imageView;

    EditText Add_Item_Name, Add_Item_Price, Add_Item_Qty;
    String Item_Name, Item_Price, Item_Qty, Image_name;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Button add_item_button;
    Core core;

    ShopItemsFragment shopItemsFragment;

    int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        core = new Core(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Add_Item_Name = (EditText) view.findViewById(R.id.Add_Item_Name);
        Add_Item_Price = (EditText) view.findViewById(R.id.Add_Item_Price);
        Add_Item_Qty = (EditText) view.findViewById(R.id.Add_Item_Qty);
        add_item_button = (Button) view.findViewById(R.id.add_item_button);

        dropdown = (Spinner) view.findViewById(R.id.spinner);
        String[] categories = new String[]{"Crafts", "Home", "Accessory", "Artwork", "Clothing"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_layout, categories);
        dropdown.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID", 0);


        addPhoto = (Button) view.findViewById(R.id.add_photo);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        imageView = (ImageView) view.findViewById(R.id.item_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        add_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id != 0) {

                    if (Add_Item_Name.getText() != null && !Add_Item_Name.getText().toString().isEmpty()) {

                        Item_Name = Add_Item_Name.getText().toString();

                    }

                    if (Add_Item_Price.getText() != null && !Add_Item_Price.getText().toString().isEmpty()) {

                        Item_Price = Add_Item_Price.getText().toString();

                    }
                    if (Add_Item_Qty.getText() != null && !Add_Item_Qty.getText().toString().isEmpty()) {

                        Item_Qty = Add_Item_Qty.getText().toString();

                    }


                    if (Image_name != null) {
                        new Add_Item_Service(id, Item_Name, Item_Qty, Item_Price, null, null, Image_name, 1).execute();
                    } else {
                        Toast.makeText(getActivity(), "Please Upload Image First", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    private void selectImage() {
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
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
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
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //   Image_name = core.uploadImage(destination.getPath());

                new UploadImageService(destination.getPath()).execute();

                imageView.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
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
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
//                Image_name = core.uploadImage(selectedImagePath);
                new UploadImageService(selectedImagePath).execute();

                imageView.setImageBitmap(bm);
            }
        }

    }


    private class Add_Item_Service extends AsyncTask {

        String text, key;
        int shop_id, Cat_Id, Item_ID;
        String name, quantity, price, description, short_description, image;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        public Add_Item_Service(int shop_id, String name, String quantity, String price, String description, String short_description, String image, int cat_id) {
            this.shop_id = shop_id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.description = description;
            this.short_description = short_description;
            this.image = image;
            this.Cat_Id = cat_id;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (Item_ID != -1) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                shopItemsFragment = new ShopItemsFragment();
                shopItemsFragment.setId(shop_id);
                ShopHomePage shopHomePage =  new ShopHomePage();
                fragmentTransaction.replace(R.id.fragment_main, shopHomePage);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected Object doInBackground(Object[] params) {
            Item_ID = core.addItem(id, Item_Name, Item_Qty, Item_Price, null, null, Image_name, Cat_Id);
            return null;
        }
    }


    private class UploadImageService extends AsyncTask {

        String path;

        public UploadImageService(String path) {
            this.path = path;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Image_name = core.uploadImage(path);
            return null;
        }
    }


}
