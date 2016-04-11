package com.example.nora.bubblestores;

/**
 * Created by Nezar Saleh on 4/10/2016.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Core {

    Context context;

    public Core(Context context) {
        this.context = context;
    }

    private String Domain = "http://bubble-zeowls.herokuapp.com";
    private String ImageUploadUrl = "http://zeowls.com/bubble/upload.php";

    private String getRequest(String url) throws IOException {
        String data;
        BufferedReader reader;
        URL url1 = new URL(Domain + url);
        Log.d("url", url1.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(2000);
        httpURLConnection.connect();

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        return data;
    }


    private String postRequest(String url, JSONObject params) throws IOException {
        URL url1 = new URL(Domain + url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream stream = connection.getOutputStream();
        DataOutputStream writer = new DataOutputStream(stream);

        Log.d("WARN", params.toString());
        // The LogCat prints out data like:
        // ID:test,Email:test@gmail.com,Pwd:test
        writer.writeBytes(params.toString());
        writer.flush();
        writer.close();
        stream.close();

        String data;
        BufferedReader reader;
        InputStream inputStream = connection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        Log.d("data", data);
        return data;
    }

    public String uploadImage(String sourceFileUri) {
        HttpURLConnection conn;
        DataOutputStream dos;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + sourceFileUri);
            return "not exist";
        } else {
            String image_url = null;
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(ImageUploadUrl);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", sourceFileUri);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\"" + sourceFileUri + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                final String data;
                BufferedReader reader;
                InputStream inputStream = conn.getInputStream();
                StringBuilder stringBuffer = new StringBuilder();
                assert inputStream != null;
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                data = stringBuffer.toString();
                Log.d("data", data);

                JSONObject jsonData = new JSONObject(data);
                image_url = jsonData.getString("image");
                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    Toast.makeText(context, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Toast.makeText(context, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                Toast.makeText(context, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                Log.e("Exception", e.getMessage());
            }
            return image_url;
        } // End else block
    }



}
