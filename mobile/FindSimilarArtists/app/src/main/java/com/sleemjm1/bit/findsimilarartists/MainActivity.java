package com.sleemjm1.bit.findsimilarartists;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{
    ListView lvArtists;
    String JSONInput;
    ArrayList<String> similarArtists;
    Button btnFindSimilar;
    EditText etSimilarArtist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvArtists = (ListView) findViewById(R.id.lvDisplayArtists);
        btnFindSimilar = (Button) findViewById(R.id.btnFindSimilar);
        etSimilarArtist = (EditText) findViewById(R.id.tvSimilarArtist);
        similarArtists = new ArrayList<String>();

        btnFindSimilar.setOnClickListener(new ClickHandler());
    }
    public class ClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            String similarArtist = etSimilarArtist.getText().toString();
            AsyncAPIShowSimilar similarInstance = new AsyncAPIShowSimilar();
            similarInstance.execute(similarArtist);
        }
    }

    public class AsyncAPIShowSimilar extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... Artist) //have to use String... even though not passing in array, it thinks we are.
        {
            {
                String JSONString = null;
                try
                {
                    String myArtist = Artist[0];    //string will be in slot 0 of array

                    //Hard coded
                    String urlString = "Http://ws.audioscrobbler.com/2.0?method=artist.getSimilar&artist=" + myArtist + "&autocorrect=1&api_key=58384a2141a4b9737eacb9d0989b8a8c&limit=10&format=json";

                    //convert urlstring to urlobject
                    URL URLObject = new URL(urlString);

                    //create httpURLConnection object via openConnection command of URLObject
                    HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();

                    //send the url
                    connection.connect();

                    //if it doesn't return 200, you don't have the data
                    int responseCode = connection.getResponseCode();

                    //get an InputStream from the HttpUrlConnection object and set up a bufferedreader
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    //read the input - may be only one line
                    String responseString;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((responseString = bufferedReader.readLine()) != null)
                    {
                        stringBuilder = stringBuilder.append(responseString);
                    }

                    //get the string from the stringBuilder. JSONString ready for parsing!!
                    JSONString = stringBuilder.toString();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return JSONString;
            }
        }

        protected void onPostExecute(String fetchedString)
        {
            try
            {
                JSONObject artistObject = new JSONObject(fetchedString);
                JSONObject artists = artistObject.getJSONObject("similarartists");

                JSONArray artistArray = artists.getJSONArray("artist");

                int numArtists = artistArray.length();

                for (int i = 0; i<numArtists; i++)
                {
                    JSONObject currentArtistObject = artistArray.getJSONObject(i);

                    String currentArtistName = currentArtistObject.getString("name");
                    similarArtists.add(currentArtistName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_layout, similarArtists);
                lvArtists.setAdapter(adapter);
            }
            catch(Exception e) {e.printStackTrace();}
        }
    }






















    //====================================USELESS STUFF=============================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
