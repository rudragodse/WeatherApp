package com.example.rudra.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText cityText;
    TextView typeTextView;
    TextView descriptorTextView;
    public void weather(View view)
    {
        Log.i("button","pressed successful");
        Log.i("cityname:",cityText.getText().toString());

        InputMethodManager mgr=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityText.getWindowToken(),0);

        try {
            String encodedCityName= URLEncoder.encode(cityText.getText().toString(),"UTF8");
            DownloadContent task=new DownloadContent();
            String result=null;
            result=task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=2b8842a1fd05bf059330a23ebea35e86").get();
        } catch (UnsupportedEncodingException e) {

            Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        } catch (ExecutionException e) {
            Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }


    }

    public class DownloadContent extends AsyncTask<String,Void,String>
    {
        URL url;
        HttpURLConnection connection=null;
        String result="";

        @Override
        protected String doInBackground(String... urls) {
            try {
                url=new URL(urls[0]);
                connection=(HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                InputStreamReader in=new InputStreamReader(inputStream);
                int data=in.read();
                while (data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=in.read();
                }
                return result;
            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String message="";
            try {
                JSONObject jsonObject=new JSONObject(result);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("weather info:",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                for (int i=0;i<arr.length(); i++)
                {
                    JSONObject jsonPart=arr.getJSONObject(i);
                    String main="";
                    String description="";
                    main=jsonPart.getString("main");
                    description=jsonPart.getString("description");
                    message+=main+": "+description+"\r\n";


                }
                if(message!="")
                {
                    typeTextView.setText(message);
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         button=(Button)findViewById(R.id.button);
        cityText =(EditText) findViewById(R.id.cityeditText);
         typeTextView=(TextView)findViewById(R.id.typeTextView);
         descriptorTextView=(TextView)findViewById(R.id.descriptorTextView);



    }
}
