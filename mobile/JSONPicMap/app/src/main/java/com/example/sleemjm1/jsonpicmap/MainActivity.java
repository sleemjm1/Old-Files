package com.example.sleemjm1.jsonpicmap;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Import these libraries to use the Apache FTPClient class.
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


public class MainActivity extends ActionBarActivity
{
    Button btnSubmit;
    EditText myImageID;
    EditText myImageString;
    EditText myComment;
    EditText myLatitude;
    EditText myLongitude;
    EditText myRating;
    TextView myJSONOutput;
    String myJSONString;
    

    //file stuff
    FTPClient mFTP;
    File mPhotoFile;
    String mPhotoFileName;
    Uri mPhotoFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new onClickListener());
        myImageID = (EditText) findViewById(R.id.etImageID);
        myImageString = (EditText) findViewById(R.id.etImageString);
        myComment = (EditText) findViewById(R.id.etComment);
        myLatitude = (EditText) findViewById(R.id.etLatitude);
        myLongitude = (EditText)findViewById(R.id.etLongitude);
        myRating = (EditText) findViewById(R.id.etRating);
        myJSONOutput = (TextView) findViewById(R.id.tvJSONOutput);
    }

    public class onClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            Image newImage = new Image();
            newImage.setImageID(myImageID.getText().toString());
            newImage.setImageString(myImageString.getText().toString());
            newImage.setCommentString(myComment.getText().toString());
            newImage.setLatitude(Double.parseDouble(myLatitude.getText().toString()));
            newImage.setLongitude(Double.parseDouble(myLongitude.getText().toString()));
            newImage.setRating(Integer.parseInt(myRating.getText().toString()));
            convertToJSON conversion = new convertToJSON();
            myJSONOutput.setText(conversion.toJSON(newImage).toString());
            myJSONString = conversion.toJSON(newImage).toString();

            WriteFTP writeFTP = new WriteFTP();
            writeFTP.execute();
        }
    }
    public class Image
        {
            String imageID;
            String imageString;
            String commentString;
            double longitude;
            double latitude;
            int rating;

            //gets & sets

            //public String getImageID(){return imageID;}
            public String getImageString(){return imageString;}
            public String getCommentString() {return commentString;}
            public double getLongitude(){return longitude;}
            public double getLatitude(){return latitude;}
            public int getRating(){return rating;}

            public void setImageID(String imageID){this.imageID = imageID;}
            public void setImageString(String imageString){this.imageString = imageString;}
            public void setCommentString(String commentString){this.commentString = commentString;}
            public void setLongitude(double longitude){this.longitude = longitude;}
            public void setLatitude(double latitude){this.latitude = latitude;}
            public void setRating(int rating){this.rating = rating;}
        }

    public class convertToJSON
        {
            public JSONObject toJSON(Image image)
            {
                JSONObject baseObject = new JSONObject();
               // String previousJSON = null;

//                try
//                {
//                    String urlString = "http://webthree.ict.op.ac.nz/sleemjm1/ourJSON.json";
//                    //convert url string to url object
//
//                    URL URLObject = new URL(urlString);
//
//                    //create httpURLConnection object via openConnection command of URLObject
//                    HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();
//
//                    //send the url
//                    connection.connect();
//
//                    //if it doesnt return 200, you don't have data
//                    int responseCode = connection.getResponseCode();
//
//                    //get an inputstream from the httpurlconnection object and set up bufferedreader
//                    InputStream inputStream = connection.getInputStream();
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                    //read input, may only be one line
//                    String responseString;
//                    StringBuilder stringBuilder = new StringBuilder();
//                    while ((responseString = bufferedReader.readLine()) !=null)
//                    {
//                        stringBuilder = stringBuilder.append(responseString);
//                    }
//
//                    //get the string from the stringbuilder. json string ready for parsing
//                    previousJSON = stringBuilder.toString();
//
//                }
//                catch (Exception e){e.printStackTrace();}



                try
                {
                    //here i am trying to fetch the previous JSON from the above try/catch...
                    //JSONObject previousObject = new JSONObject(previousJSON);
                    //JSONObject previousPictures = previousObject.getJSONObject("pictures");
                    //here we convert Java Object to JSON
                    JSONArray jsonPicArray = new JSONArray();

                    JSONObject jsonObject = new JSONObject();
                    //set the first name/pair:
                    //jsonObject.put("imageID", image.getImageID());
                    jsonObject.put("imageString", image.getImageString());

                    //JSONObject dataObject = new JSONObject();
                    jsonObject.put("commentString", image.getCommentString());
                    jsonObject.put("longitude", image.getLongitude());
                    jsonObject.put("latitude", image.getLatitude());
                    jsonObject.put("rating", image.getRating());

                    jsonPicArray.put(jsonObject);
                    //here I am trying to put the previousPictures object into jsonPicArray
                    //jsonPicArray.put(previousPictures);

                    baseObject.put("pictures", jsonPicArray);

                   // return jsonObject.toString();
                }
                catch(JSONException ex)
                {
                    ex.printStackTrace();
                }
                return baseObject;
            }

        }

    //Do the ftp on another thread with AsyncTask
     public class WriteFTP extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {


            //username/password for server
            String username = "webthree.ict.op.ac.nz|sleemjm1";
            String password = "joetommobile";
            //create the ftp client
            mFTP = new FTPClient();
            try
            {
                //configure server connection
                mFTP.connect("webthree.ict.op.ac.nz");

                //login
                mFTP.login(username, password);

                //file type config
                mFTP.setFileType(FTP.ASCII_FILE_TYPE);
                mFTP.enterLocalPassiveMode();
                String fileName = "newJSON.json";
                String outputData = myJSONString;
                FileOutputStream outputStream;
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                File outputFile = new File (getFilesDir(), fileName);
                outputStream.write(outputData.getBytes());
                outputStream.close();

                //Open FileInputStream to read your little file
                FileInputStream inputStream = new FileInputStream(outputFile);

                //write the file to FTP server

                mFTP.storeFile(fileName, inputStream);
                mFTP.disconnect();


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

    }

    public class jsonShit extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params)
        {
            String previousJSON = null;
            try
            {
                String urlString = "http://webthree.ict.op.ac.nz/sleemjm1/ourJSON.json";
                //convert url string to url object

                URL URLObject = new URL(urlString);

                //create httpURLConnection object via openConnection command of URLObject
                HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();

                //send the url
                connection.connect();

                //if it doesnt return 200, you don't have data
                int responseCode = connection.getResponseCode();

                //get an inputstream from the httpurlconnection object and set up bufferedreader
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //read input, may only be one line
                String responseString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((responseString = bufferedReader.readLine()) !=null)
                {
                    stringBuilder = stringBuilder.append(responseString);
                }

                //get the string from the stringbuilder. json string ready for parsing
                previousJSON = stringBuilder.toString();

            }
            catch (Exception e){e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String JSONString)
        {

        }
    }


    ////---useless shit-----

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
