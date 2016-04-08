package bit.sleemjm1.teleportgame;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;


public class MainActivity extends ActionBarActivity
{
    TextView tvLong;
    TextView tvLat;
    TextView tvClosest;
    DecimalFormat threePlaces;
    double myLong;
    double myLat;
    LocationManager locationManager;
    Criteria defaultCriteria;
    String providerName;
    Location currentLocation;
    Button btnTeleport;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLong = (TextView) findViewById(R.id.txtLong);
        tvLat = (TextView) findViewById(R.id.txtLat);
        tvClosest = (TextView) findViewById(R.id.txtLocation);

        btnTeleport = (Button) findViewById(R.id.btnTransport);

        btnTeleport.setOnClickListener(new ClickHandler());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        defaultCriteria = new Criteria();

        providerName = locationManager.getBestProvider(defaultCriteria, false);

        currentLocation = locationManager.getLastKnownLocation(providerName);  //doesn't work on emulator

        locationManager.requestLocationUpdates(providerName, 400, 1, new CustomLocationListener());

    }

public class ClickHandler implements View.OnClickListener
{

    @Override
    public void onClick(View v)
    {
        threePlaces = new DecimalFormat("0.000");


        myLat = currentLocation.getLatitude();
        myLong = currentLocation.getLongitude();
        //myLong = getLong();
        //myLat = getLat();

        Double[] latLongArray = {myLat, myLong};

        CallGeoPlugin geoInstance = new CallGeoPlugin();
        geoInstance.execute(latLongArray);

    }
}
    public class CustomLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            //use them
            tvLat.setText(String.valueOf(lat));
            tvLong.setText(String.valueOf(lng));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled(String provider){

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    public double getLong() //this method will return a random longitude
    {
        Random  rGen = new Random();

        double r =rGen.nextDouble();

        double longBase = r * 180;

        if (rGen.nextInt(2) == 0)
            longBase *= -1;

        return longBase;
    }

    public double getLat()  //this method will return a random latitude
    {
        Random  rGen = new Random();

        double r =rGen.nextDouble();

        double latBase = r * 90;

        if (rGen.nextInt(2) == 0)
            latBase *= -1;

        return latBase;
    }


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

    public class CallGeoPlugin extends AsyncTask<Double, Void, String>
    {

        @Override
        protected String doInBackground(Double... Location)
        {
            String JSONString = null;
            //declare here so it is not local to the try block
            try
            {
                String myLatitude = Location[0].toString();
                String myLongitude = Location[1].toString();

                //hard coded
                String urlString = "http://www.geoplugin.net/extras/location.gp?lat="+ myLatitude + "&long=" + myLongitude +"&format=json";
                //convert urlstring to urlobject
                URL URLObject = new URL(urlString);

                //create httpURLConnection object via openConnection command of URLObject
                HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();

                //send the URL
                connection.connect();

                //if it doesn't return 200, you don't have data
                int responseCode = connection.getResponseCode();

                //get an InputStream from the HttpURLConnection object and set up a bufferedreader
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //read the input may be only one line
                String responseString;
                StringBuilder stringBuilder = new StringBuilder();
                while((responseString = bufferedReader.readLine()) !=null)
                {
                    stringBuilder = stringBuilder.append(responseString);
                }

                //get the string from the stringBuilder. JSONString ready for parsing!
                JSONString = stringBuilder.toString();
            }
            catch(Exception e) {e.printStackTrace();}

            return JSONString;
        }
        protected void onPostExecute(String fetchedString)
        {
            //get what we need here...
            try
            {
                tvLong.setText(String.valueOf(threePlaces.format(myLong)));
                tvLat.setText(String.valueOf(threePlaces.format(myLat)));
                if (!fetchedString.equals("[[]]"))
                {
                    JSONObject root = new JSONObject(fetchedString);
                    String place = root.getString("geoplugin_place");
                    String countryCode = root.getString("geoplugin_countryCode");
                    String closestCity = place + " " + countryCode;
                    tvClosest.setText(closestCity);
                }
                else
                {
                    tvClosest.setText("No city found.");
                }

            }
            catch (Exception  e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


}
