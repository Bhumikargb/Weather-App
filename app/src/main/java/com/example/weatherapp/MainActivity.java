package com.example.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    EditText etcity, etcountry;
    TextView tvResult;
    private final String url="https://api.openweathermap.org/data/2.5/weather";
            private final String appid="b2beb17abb1073e57447ace0f1614f6d";
            DecimalFormat df=new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etcity=findViewById(R.id.etcity);
        etcountry=findViewById(R.id.etcountry);
        tvResult=findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String tempurl="";
        String city= etcity.getText().toString().trim();
        String country=etcountry.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("City field can not be blank...");
        }
        else{
            if(!country.equals("")){
                tempurl=url+"?q="+city+ ","+country+ "&appid="+appid;
            }
            else{
                tempurl=url+ "?q="+city+"&appid="+ appid;
            }
            StringRequest stringRequest=new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d("response",response);
                    String output="";
                    try{
                        JSONObject jsonResponse =new JSONObject(response);
                        JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                        String description=jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain=jsonResponse.getJSONObject("main");
                        double temp=jsonObjectMain.getDouble("temp")-273.15;
                        double feels_like=jsonObjectMain.getDouble("feels_like")-273.15;
                        float pressure=jsonObjectMain.getInt("pressure");
                        int humidity=jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind=jsonResponse.getJSONObject("wind");
                        String wind=jsonObjectWind.getString("speed");
                        JSONObject jsonObjectSys=jsonResponse.getJSONObject("sys");
                        String countryName=jsonObjectSys.getString("country");
                        String cityName=jsonResponse.getString("name");
                        tvResult.setTextColor(Color.rgb(68,134,199));
                        output+="Current weather of "+ cityName+"( "+ countryName+" )"
                                +"\n Temp: "+ df.format(temp)+ "°C"
                                +"\n feels likes: "+ df.format(feels_like)+ "°C"
                                +"\n Humidity: "+ humidity+ "%"
                                +"\n Description: "+ description
                                +"\n Wind Speed: "+ wind+ "m/s"
                                +"\n Pressure: "+ pressure+ "hPa";
                        tvResult.setText(output);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString().trim() ,Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    }
}