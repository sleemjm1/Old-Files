package sleemjm1.bit.firstjson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity 
{
	Button ButtonClick;
	String JSONInput;
	ArrayList<String> eventDataArray;
	ArrayList<String> eventDescriptionArray;
	ListView lstEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButtonClick = (Button) findViewById(R.id.btnShowToast);
        ButtonClick.setOnClickListener(new ClickHandler());
        
        lstEvents = (ListView) findViewById(R.id.lstEvents);
        lstEvents.setOnItemClickListener(new onItemClickHandler());
        
        eventDataArray  = new ArrayList<String>();
        eventDescriptionArray = new ArrayList<String>();
       
    }


   public void readFile() throws IOException, JSONException 
   {
	   String assetFileName = "dunedin_events.json";
 	   
 	   
 	   //get an asset manager and create an input stream from the JSON file
 	   AssetManager am = getAssets();
 	   InputStream inputStream = am.open(assetFileName);
 	   
 	   //Determine number of bytes in file, and prepare buffer array for read
 	   int fileSizeInBytes = inputStream.available();
 	   byte[] JSONBuffer = new byte[fileSizeInBytes];
 	   
 	   //read the stream into the buffer, and close it
 	   inputStream.read(JSONBuffer);
 	   inputStream.close();
 	   
 	   //create a string from the byte[]
 	   String JSONInput = new String(JSONBuffer);
 	   
 	   //convert file string to JSON Object
 	   JSONObject eventData = new JSONObject(JSONInput);
 	   JSONObject events = eventData.getJSONObject("events");
 	   
 	   //grab the value part of the data
 	   JSONArray eventArray = events.getJSONArray("event");
 	   
 	   int numEvents = eventArray.length();
 	   
 	   for (int i = 0; i<numEvents; i++)
 	   {
 		   //Get an element from the array
 		   JSONObject currentEventObject = eventArray.getJSONObject(i);
 		   //Access the title value
 		   String currentEventTitle = currentEventObject.getString("title");
 		   String currentDescription = currentEventObject.getString("description");
 		   //have we found it?
 		   eventDataArray.add(currentEventTitle);
 		   eventDescriptionArray.add(currentDescription);
 	   }
 	   
 	   ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_list_view, eventDataArray);
 	   lstEvents.setAdapter(adapter);
 	   
 	   
 	   //Toast.makeText(MainActivity.this, JSONInput, Toast.LENGTH_LONG).show();
   }
   
   public class onItemClickHandler implements OnItemClickListener
   {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) 
	{
		String description = eventDescriptionArray.get(position).toString();
		Toast.makeText(MainActivity.this, description, Toast.LENGTH_LONG).show();
		
	}
	   
   }
   
   public class ClickHandler implements OnClickListener
   {

	@Override
	public void onClick(View v) 
	{
		try {
			readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	   
   }
}
