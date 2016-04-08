package bit.sleemjm1.firstandroid;

import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button ButtonClick = (Button) findViewById(R.id.button1);					//finding the id of button1 and storing it in ButtonClick variable
        
        EditText UserName = (EditText) findViewById(R.id.editText1);
        
        ClickHandlerWithToast handler = new ClickHandlerWithToast();				//creating a new instance of the ClickHandler method
        LongClickHandlerWithToast longHandler = new LongClickHandlerWithToast();
        
        KeyHandlerWithToast keyHandler = new KeyHandlerWithToast();
        
        
        ButtonClick.setOnClickListener(handler);
        ButtonClick.setOnLongClickListener(longHandler);
        
        UserName.setOnKeyListener(keyHandler);
        
        //randomStringGen();
        //loopArray();
        //Resources resourceResolver = getResources();
        //int datesArray[] = resourceResolver.getIntArray(R.array.FebFridays);
    }	//end onCreate
    void randomStringGen()
    {
    	//pick a string
    	
    	TextView txtRandom = (TextView)findViewById(R.id.txtRandom);
    	String dogBreed ="";
    	
    	Random rGen = new Random();
    	int dogValue = rGen.nextInt(4);
    	
    	switch(dogValue)
    	{
    	case 0:
    		dogBreed = "Hey";
    		break;
    	case 1:
    		dogBreed = "Hiya";
    		break;
    	case 2:
    		dogBreed = "Kia Ora";
    		break;
    	case 3:
    		dogBreed = "Greetings";
    		break;
    	}
    	txtRandom.setText(dogBreed);
    }
    
    void loopArray()
    {
    	TextView txtFebFridays = (TextView)findViewById(R.id.txtRandom);
    	
    	Resources resourceResolver = getResources();
    	int datesArray[] = resourceResolver.getIntArray(R.array.FebFridays);
    	
    	String txtDates = "February Fridays on: ";
    	
    	for(int i = 0; i < datesArray.length; i++)
    	{
    		txtDates += datesArray[i] + " ";
    	}
    	
    	txtFebFridays.setText(txtDates);
    }
    
    public class ClickHandlerWithToast implements OnClickListener
    {
		@Override
		public void onClick(View v) 
		{
			Toast.makeText(MainActivity.this, "you clicked the button normally", Toast.LENGTH_SHORT).show();
			
		}
    }
		
	public class LongClickHandlerWithToast implements OnLongClickListener
	{
		@Override
		public boolean onLongClick(View v) 
		{
			Toast.makeText(MainActivity.this, "you held down the button ", Toast.LENGTH_SHORT).show();
			return false;
		}
    }
	
	public class KeyHandlerWithToast implements OnKeyListener
	{
		
		EditText UserName = (EditText) findViewById(R.id.editText1);			//have to do this again because otherwise cant put it into variable
		Editable stringLength = UserName.getText();								//making editable called stringLength which will store UserNames text

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
			boolean b = false;								//boolean value which will be returned at the end 
			
			if (keyCode == KeyEvent.KEYCODE_AT)
			{
				Toast.makeText(MainActivity.this, "DONT TYPE @", Toast.LENGTH_SHORT).show();
				b = false;
			}
			
			if (keyCode == KeyEvent.KEYCODE_ENTER)
			{				
				if (stringLength.length() == 8)		//checking to see if stringLengths length is 8 characters long
				{
					Toast.makeText(MainActivity.this, "Thank You " + stringLength, Toast.LENGTH_SHORT).show();
				}
				else								//else if it isn't 8 chars long there will be an error
				{
					Toast.makeText(MainActivity.this, "Usernames must be 8 characters long " + stringLength , Toast.LENGTH_LONG).show();
				}
				b = true;
			}
			return b;
		}
	}
	
}	//end activity
