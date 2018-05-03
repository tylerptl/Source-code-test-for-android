package com.example.tylerptl.sourcecodetest;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    TextView et;
    Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (TextView) findViewById(R.id.textView);
        et.setText(getWebsite("https://www.walmart.com/search/?query=water&cat_id=0"));
        et.setText(price.toString());
        // et.setText(getWebsite("http://www.radefffactory.free.bg"));
    }

    public String getWebsite(String webSite){
        String str = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpCLient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(webSite);
        try{
            HttpResponse response;
            response = httpCLient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1251"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            str = sb.toString();

            //Search string for price
            String s = str.substring(str.indexOf("offerPrice\":"+1));
            s = s.substring(s.indexOf(":")+1);
            s = s.substring(0, s.indexOf(","));
            price = Double.parseDouble(s);

            // Write to file
            writeFileOnInternalStorage(MainActivity.this, "test.txt", str);

        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
        return str;
    }
//    public void writeToFile(String data, Context context){
//        try{
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("test.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//
//        }catch (IOException e){
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }

    public void writeFileOnInternalStorage(Context mcoContext,String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }
}
