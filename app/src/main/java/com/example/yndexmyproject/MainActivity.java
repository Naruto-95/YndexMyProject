package com.example.yndexmyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText User;
    private Button button1;
    private TextView result_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Инитилизация текста,кнопки и тд.
        User = findViewById(R.id.User);
        button1 = findViewById(R.id.Buttan1);
        result_info = findViewById(R.id.result_info);
//Повесил нажатие на кнопку
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this,R.string.Text_You,Toast.LENGTH_LONG).show();
                else {
                    // Выдает инфу из интернета.Ключ с сайта.
                    String city = User.getText().toString();
                    String Key = "b2d1c602564d082d308b933b63191c70";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q= " + city + "&appid=" + Key +
                            "&units=metric&lang=ru";
                    new GetURLData().execute(url);

                }
            }
        });
    }


    private class GetURLData extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText(" Температура "  +  jsonObject.getJSONObject("main").getDouble("temp") +
                        ("\n"+"Ощущается как " + jsonObject.getJSONObject("main").getDouble("feels_like")) );
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

    }
}
//42 min