package bit.sleemjm1.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity
{
    String mPhotoFileName;
    File mPhotoFile;
    Uri mPhotoFileUri;
    Button btnCapture;
    ImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = (Button)findViewById(R.id.btnCapture);
        ivPic = (ImageView) findViewById(R.id.ivPic);

        btnCapture.setOnClickListener(new ButtonHandler());
    }

    public class ButtonHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            //Creates time stamped file to hold the image data
            mPhotoFile = createTimeStampFile();

            //Generate Uri from the File instance
            mPhotoFileUri = Uri.fromFile(mPhotoFile);

            //Create an intent for the image capture content provider
            Intent  imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Attach your Uri to the intent
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);

            //launch the intent, waiting for result.
            //the user will see the camera app. When they finish, onActivityResult is raised
            startActivityForResult(imageCaptureIntent, 1);
        }
    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Is this the return we are waiting for?
        if (requestCode ==1)
        {
            //Did we get data?
            if (resultCode == RESULT_OK)    //NB: The actual value of RESULT_OK is -1. Don't be alarmed.
            {
                //We need the file path for BitmapFactory, not the file
                String realFilePath = mPhotoFile.getPath(); //Safe way to do this to avoid typos

                Bitmap userPhotoBitmap = BitmapFactory.decodeFile(realFilePath);

                //Do whatever you want with the bitmap here....
                ivPic.setImageBitmap(userPhotoBitmap);
            }
            else
            {
                Toast.makeText(this, "No photo, IDIOT", Toast.LENGTH_LONG).show();
            }
        }
    }

    public File createTimeStampFile()
    {
        //Fetch system image folder
        File imageRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //Make subdirectory
        File imageStorageDirectory = new File(imageRootPath, "CameraDemo1");
        if (!imageStorageDirectory.exists())
        {
            imageStorageDirectory.mkdirs();     //creates parent directory as required
        }

        //Get timestamp
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date currentTime = new Date();
        String timeStamp = timeStampFormat.format(currentTime);

        //Make file name
        mPhotoFileName = "IMG_" + timeStamp + ".jpg";

        //Make file object fro, directory and filename
        File photoFile = new File(imageStorageDirectory.getPath() + File.separator + mPhotoFileName);
        return photoFile;
    }
}
