package bit.sleemjm1.activitylaunching;

import bit.sleemjm1.activitylaunching.ActivityA.ClickHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityB extends Activity 
{
	String activityText;
	Button ButtonClick;
	TextView ScreenLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        
        ButtonClick = (Button) findViewById(R.id.btnA);
        
        ButtonClick.setOnClickListener(new ClickHandler());
        
        ScreenLabel = (TextView) findViewById(R.id.lblText);
        
        ScreenLabel.setText("Activity B");
        
        
    }
    
    public class ClickHandler implements OnClickListener
	{

		@Override
		public void onClick(View v) 
		{
			Intent changeActivityIntent = new Intent(ActivityB.this, ActivityC.class);
			startActivity(changeActivityIntent);
			
			
		}
    	
	} 
    
}

