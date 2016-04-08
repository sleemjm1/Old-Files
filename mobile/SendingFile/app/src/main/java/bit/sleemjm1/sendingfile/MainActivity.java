package bit.sleemjm1.sendingfile;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity
{
    private static final int BUFFER_SIZE = 4096;
    Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnUpload = (Button) findViewById(R.id.btnWrite);
        btnUpload.setOnClickListener(new clickHandler());
    }

    public class clickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            AsyncUpload UploadThread = new AsyncUpload();
            UploadThread.execute();
        }
    }


    public void uploadFile()
    {
        String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String host = "www.kate.ict.op.ac.nz/~sleemjm1";
        String user = "sleemjm1";
        String pass = "sleemani2tuo";
        String filePath = "H:/string.txt";                      //this is the file path we will upload from
        String uploadPath = "/assignment1";

        ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);

        try
        {
            URL url = new URL(ftpUrl);
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(filePath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void sauronUpload()
    {
        String FTP_HOST="kate.ict.op.ac.nz/~sleemjm1";

        String USERNAME="sleemjm1";

        String PASSWORD="sleemani2tuo";

        FTPClient client


    }

    class AsyncUpload extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... arg0)
        {
            uploadFile();
            return null;
        }
    }

}
