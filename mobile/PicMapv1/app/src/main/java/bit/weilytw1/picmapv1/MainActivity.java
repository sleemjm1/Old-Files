package bit.weilytw1.picmapv1;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//Libraries for apache commons
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPSClient;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity
{
    FTPClient mFTP;
    Button btnUpload;
    Button btnUploadImage;

    File mPhotoFile;
    String mPhotoFileName;
    Uri mPhotoFileUri;
    ImageView userPic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button to run async task
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUploadImage = (Button) findViewById(R.id.btnUploadImage);
        btnUpload.setOnClickListener(new clickHandlerUpload());
        btnUploadImage.setOnClickListener(new uploadPicHandler());

        userPic = (ImageView) findViewById(R.id.imgUserPic);
    }

    public class uploadPicHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            WriteImageFTP writeImageFTP = new WriteImageFTP();
            writeImageFTP.execute();
        }
    }

    public class clickHandlerUpload implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            //Write text file to server
            //WriteFTP writeFTP = new WriteFTP();
            //writeFTP.execute();

            //Create time stamped file to hold the image data
            mPhotoFile = createTimeStampedFile();

            //Generate Uri from the file instance
            mPhotoFileUri = Uri.fromFile(mPhotoFile);

            //Create an intent for the image capture content provider
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Attach your Uri to the intent
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);

            //Launch the intent, waiting for result
            //The user will see the camera app, When they finish, onActivityResult is raised
            startActivityForResult(imageCaptureIntent, 1);
        }
    }

    public File createTimeStampedFile()
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

        //Get timestamp
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date currentTime = new Date();
        String timeStamp = timeStampFormat.format(currentTime);

        //Make file name
        mPhotoFileName = "IMG_" + timeStamp + ".jpg";

        //Make file object from directory and filename
        File photoFile = new File(imageStorageDirectory.getPath() + File.separator + mPhotoFileName);
        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Is this the return we are waiting for?
        if(requestCode == 1)
        {
            //Did we get data?
            if(resultCode == RESULT_OK)
            {
                //File path for bitmapFactory, not the file
                String realFilePath = mPhotoFile.getPath();

                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);

                //Do stuff with bitmap here...
                userPic.setImageBitmap(userPhotoBitmap);
            }
            else
            {
                Toast.makeText(MainActivity.this, "No photo saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    //////////////////////////////
    //Upload image file to server
    //////////////////////////////
    public class WriteImageFTP extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            //user name and password for server
            String username = "webthree.ict.op.ac.nz|sleemjm1";
            String password = "joetommobile";
            //Create the FTPClient
            mFTP = new FTPClient();
            try
            {
                //Configure server connection
                mFTP.connect("webthree.ict.op.ac.nz");

                //Login
                mFTP.login(username, password);

                //Configure file type and authorisation
                mFTP.setFileType(FTP.BINARY_FILE_TYPE);
                mFTP.enterLocalPassiveMode();

                //Create a file for testing
                //Want to send bitmap here
                //Bitmap uploadBitmap = params[0];

                //AssetManager am = getAssets();
                InputStream inputStream = new FileInputStream(mPhotoFile);
                String fileName = mPhotoFileName;

                //Write the file to FTP Server
                mFTP.storeFile("Test/"+fileName, inputStream);
                mFTP.disconnect();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    //////////////////////////////
    //Upload String file to server
    //////////////////////////////
    public class WriteFTP extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            //user name and password for server
            String username = "webthree.ict.op.ac.nz|sleemjm1";
            String password = "joetommobile";

            //Create the FTPClient
            mFTP = new FTPClient();
            try
            {
                //Configure server connection
                mFTP.connect("webthree.ict.op.ac.nz");

                //Login
                mFTP.login(username, password);

                //Configure file type and authorisation
                mFTP.setFileType(FTP.ASCII_FILE_TYPE);
                mFTP.enterLocalPassiveMode();

                //Create a file for testing
                String fileName = "output.txt";
                String outputData = "Here is some output data";

                FileOutputStream outputStream;
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                File outputFile = new File(getFilesDir(), fileName);
                outputStream.write(outputData.getBytes());
                outputStream.close();

                //Open a FileInputStream to read file
                FileInputStream inputStream = new FileInputStream(outputFile);

                //Write the file to FTP Server
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

    public class DownloadFTP extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            //Create FTPClient
            mFTP = new FTPClient();

            return null;
        }
    }

    //////////////////
    //USELESS METHODS
    //////////////////
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
