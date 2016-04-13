package com.example.nora.bubblestores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddItemFragment extends Fragment {

//    private final int SELECT_FILE = 1;
//    private final int REQUEST_CAMERA = 0;
    Spinner dropdown;
//    Button addPhoto;
//    ImageView imageView;

    int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).toolbar.setTitle("Add Item");
        super.onActivityCreated(savedInstanceState);
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
        dropdown = (Spinner) view.findViewById(R.id.spinner);
        String[] categories = new String[]{"Crafts", "Home", "Accessory", "Artwork", "Clothing"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        dropdown.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = preferences.getInt("shopID",0);
//        addPhoto = (Button) view.findViewById(R.id.add_photo);
//        addPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });
//        imageView = (ImageView) view.findViewById(R.id.item_image);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });
    }

//        private void selectImage() {
//            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setTitle("Add Photo!");
//            builder.setItems(items, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int item) {
//                    if (items[item].equals("Take Photo")) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, REQUEST_CAMERA);
//                    } else if (items[item].equals("Choose from Library")) {
//                        Intent intent = new Intent(
//                                Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                        startActivityForResult(
//                                Intent.createChooser(intent, "Select File"),
//                                SELECT_FILE);
//                    } else if (items[item].equals("Cancel")) {
//                        dialog.dismiss();
//                    }
//                }
//            });
//            builder.show();
//        }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_CAMERA) {
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//                File destination = new File(Environment.getExternalStorageDirectory(),
//                        System.currentTimeMillis() + ".jpg");
//
//                FileOutputStream fo;
//                try {
//                    destination.createNewFile();
//                    fo = new FileOutputStream(destination);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
////                imageView.setImageBitmap(thumbnail);
//
//            } else if (requestCode == SELECT_FILE) {
//                Uri selectedImageUri = data.getData();
//                String[] projection = {MediaStore.MediaColumns.DATA};
//                CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null,
//                        null);
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
////                imageView.setImageBitmap(bm);
//            }
//        }
//
//    }
}
