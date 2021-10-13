package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.myapplication.adapter.FlickrAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    FlickrImage[] myFlickrImage;

    EditText searchText;
    Button searchButton;
    Gallery photoBar;
    final String DEFAULT_SEARCH = "new_york";
    String FlickrQuery_url = "http://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=10";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=";
    String FlickrApiKey = "55884827d9b757165d0af3bc4dbf1098";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Thread handling
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        searchText = (EditText)findViewById(R.id.searchtext);
        searchText.setText(DEFAULT_SEARCH);
        searchButton = (Button)findViewById(R.id.searchbutton);
        photoBar = (Gallery)findViewById(R.id.photobar);
        searchButton.setOnClickListener(searchButtonOnClickListener);
    }

    private Button.OnClickListener searchButtonOnClickListener = new Button.OnClickListener(){

        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            String searchQ = searchText.getText().toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String searchResult = QueryFlickr(searchQ);
                    System.out.println("JSON DATA: "+searchResult);
                    myFlickrImage = ParseJSON(searchResult);
                    photoBar.setAdapter(new FlickrAdapter(MainActivity.this, myFlickrImage));
                }
            });
        }};


    private String QueryFlickr(String q){

        String qResult = null;

        String qString =
                FlickrQuery_url
                        + FlickrQuery_per_page
                        + FlickrQuery_nojsoncallback
                        + FlickrQuery_format
                        + FlickrQuery_tag + q
                        + FlickrQuery_key + FlickrApiKey;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String stringReadLine = null;

                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "n");
                }

                qResult = stringBuilder.toString();
                inputStream.close();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return qResult;
    }
    Bitmap bmFlickr;
    private FlickrImage[] ParseJSON(String json) {

        FlickrImage[] flickrImage = null;
        bmFlickr = null;
        String flickrId;
        String flickrOwner;
        String flickrSecret;
        String flickrServer;
        String flickrFarm;
        String flickrTitle;

        try {
            JSONObject JsonObject = new JSONObject(json);
            JSONObject Json_photos = JsonObject.getJSONObject("photos");
            JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");

            flickrImage = new FlickrImage[JsonArray_photo.length()];
            for (int i = 0; i < JsonArray_photo.length(); i++){
                JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(i);
                flickrId = FlickrPhoto.getString("id");
                flickrOwner = FlickrPhoto.getString("owner");
                flickrSecret = FlickrPhoto.getString("secret");
                flickrServer = FlickrPhoto.getString("server");
                flickrFarm = FlickrPhoto.getString("farm");
                flickrTitle = FlickrPhoto.getString("title");
                flickrImage[i] = new FlickrImage(flickrId, flickrOwner, flickrSecret,
                        flickrServer, flickrFarm, flickrTitle);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return flickrImage;
    }

}

