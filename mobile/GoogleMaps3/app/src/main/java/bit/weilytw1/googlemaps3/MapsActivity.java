package bit.weilytw1.googlemaps3;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.internal.zzc;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzi;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; //Might be null if Google Play services APK is not available.

    FTPClient client;
    Pic mPic;
    Button btnMarker;
    String JSONInput;
    ArrayList<Pic> picData;
    ImageView ivSelectedPic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnMarker = (Button) findViewById(R.id.btnMarker);
        ivSelectedPic = (ImageView) findViewById(R.id.ivSelectedPic);
        btnMarker.setOnClickListener(new makeMarkerHandler());
        picData = new ArrayList<Pic>();

        setUpMapIfNeeded();
        //Populate picData array with JSON Data
        createCustomMarker cm = new createCustomMarker();
        cm.execute();
        //DownloadImage downloadImage = new DownloadImage();
        //File pic = downloadImage.doInBackground()
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    ///////////////////////////////
    //When a map marker is clicked
    ///////////////////////////////
    public class markerClickHandler implements GoogleMap.OnMarkerClickListener
    {
        @Override
        public boolean onMarkerClick(Marker marker)
        {
            //Gets selected markers id
            int id = Integer.parseInt(marker.getId().substring(1));
            //Get image associated with selected marker
            String imageString = picData.get(id).getImageString();
            //Pulls image from server and sets to image view
            //new ImageLoadTask("http://webthree.ict.op.ac.nz/sleemjm1/Test/" + imageString, ivSelectedPic).execute();
            Bundle bundle = new Bundle();
            bundle.putString("imageString", imageString);

            //Launch picMapFragment
            Fragment picFrag = new PicFragment();
            picFrag.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //Make fragment changes
            ft.replace(R.id.picview, picFrag);
            ft.commit();
            return false;
        }
    }

    public class makeMarkerHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            //Populate picData array with JSON Data
            createCustomMarker cm = new createCustomMarker();
            cm.execute();
        }
    }

    public static class picMappFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.picmapp_fragment,
                    container, false);
            return view;
        }
    }


    //Populate the map with data from picData
    public void populateMap()
    {
        int nPics = picData.size();
        for(int i=0; i<nPics; i++)
        {
            String comment = picData.get(i).comment;
            double lat = picData.get(i).latitude;
            double lng = picData.get(i).longitude;
            LatLng location = new LatLng(lat, lng);
            Marker currentMarker = mMap.addMarker(new MarkerOptions().position(location).title(comment));
        }
    }

    //Fetch and read JSON file and store data in picData
    class createCustomMarker extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            String JSONString = null;
            try
            {
                //Hard coded
                String urlString = "http://webthree.ict.op.ac.nz/sleemjm1/JSONText.json";

                //Convert url to URLObject
                URL URLObject = new URL(urlString);

                //Create HttpUrlConnection object via openConnection command of URLObject
                HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();

                //Send the URL
                connection.connect();

                //If it doesn't return 200, you don't have data
                int responseCode = connection.getResponseCode();

                //Get InputStream
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //Read the input
                String responseString;
                StringBuilder stringBuilder = new StringBuilder();
                while((responseString = bufferedReader.readLine()) != null)
                {
                    stringBuilder = stringBuilder.append(responseString);
                }

                //Get the string from the stringBuilder.
                JSONString = stringBuilder.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return JSONString;
        }

        @Override
        protected void onPostExecute(String fetchedString)
        {
            try
            {
                //Convert fetched string to JSONObject
                JSONObject picturesObject = new JSONObject(fetchedString);
                JSONArray pictures = picturesObject.getJSONArray("pictures");
                //JSONObject currentPictureObject = pictures.getJSONObject(0);
                int nPics = pictures.length();
                for(int index=0; index<nPics; index++)
                {
                    //Access elements data
                    JSONObject currentPictureObject = pictures.getJSONObject(index);
                    String imageString = currentPictureObject.getString("imageString");
                    String comment = currentPictureObject.getString("commentString");
                    double latitude = currentPictureObject.getDouble("latitude");
                    double longitude = currentPictureObject.getDouble("longitude");
                    int rating = currentPictureObject.getInt("rating");
                    Pic currentPic = new Pic(imageString, comment, latitude, longitude, rating);
                    picData.add(currentPic);
                }
                populateMap();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    //Download image from file
    public class DownloadImage extends AsyncTask<Void, Void, File>
    {
        @Override
        protected File doInBackground(Void... params)
        {
            //Fetch system image folder
            File imageRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            //Make subdirectory
            File imageStorageDirectory = new File(imageRootPath, "CameraLab");
            if(!imageStorageDirectory.exists())
            {
                //Create subdirectory
                imageStorageDirectory.mkdirs();
            }

            //Download image from server
            //user name and password for server
            String username = "webthree.ict.op.ac.nz|sleemjm1";
            String password = "joetommobile";

            //This is the image we want to fetch from server
            String imageString = "IMG_20150520_165514.jpg";
            File output = null;

            //Create the FTPClient
            client = new FTPClient();
            try
            {
                //Configure server connection
                client.connect("webthree.ict.op.ac.nz");

                //Login
                client.login(username, password);

                //Configure file type and authorisation
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();

                FileOutputStream fos = new FileOutputStream(imageString);

                client.retrieveFile("Test/" + imageString, fos);

                output = new File(imageStorageDirectory.getPath() + File.separator + fos);

                fos.close();
                client.disconnect();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return output;
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap()
    {
        mMap.setMyLocationEnabled(true);

        //If the user changes location, centers map on that location
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(newLatLng).title("You are here"));
                CameraUpdate newCentre = CameraUpdateFactory.newLatLngZoom(newLatLng, 16);
                //Move camera to the users current location
                mMap.moveCamera(newCentre);
            }
        });
        mMap.setOnMarkerClickListener(new markerClickHandler());
    }
}
