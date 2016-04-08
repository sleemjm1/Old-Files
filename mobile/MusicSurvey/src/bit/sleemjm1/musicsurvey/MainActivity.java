package bit.sleemjm1.musicsurvey;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity 
{	
	//CLASS PROPERTY
	Spinner monthSpinner;
	RadioGroup instrumentGroup;
	Button ButtonClick;
	DialogFragment confirmMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ButtonClick = (Button) findViewById(R.id.btnSubmit);	
    	//ButtonClick.setOnClickListener(new ClickHandler()); 
        ButtonClick.setOnClickListener(new CreateFragmentButtonHandler());
    	
    	String[] Months = {"January", "February", "March", "April", "may", "June", "July", "August", "September", "October", "November", "December"};
    	
    	monthSpinner = (Spinner) findViewById(R.id.spnMonths);
    	
    	int layoutID = android.R.layout.simple_spinner_item;
    	ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(this, layoutID, Months);
    	monthSpinner.setAdapter(monthsAdapter);
    	
        
        instrumentGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        instrumentGroup.setOnCheckedChangeListener(new radioGroupListener());
    }
    
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public class radioGroupListener implements RadioGroup.OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) 
		{
			//RadioButton chosenButton = (RadioButton) findViewById(checkedId);
			//String chosenInstrument = chosenButton.getText().toString();
			
			//String feedbackString = "You have enroled for " + chosenInstrument + "lessons";
			//TextView textViewFeedback = (TextView) findViewById(R.id.txtResult);
			//textViewFeedback.setText(feedbackString);
			
		}
    }
    
    public class ClickHandler implements OnClickListener		//Still needs work to get it all right :)
    {

		@Override
		public void onClick(View v) 
		{
			
			
			//RadioGroup instrumentGroup = (RadioGroup) findViewById(R.id.radioGroup1);				
			
			
			//RadioButton chosenButton = (RadioButton) findViewById(instrumentGroup.getCheckedRadioButtonId());
			//String chosenInstrument = chosenButton.getText().toString();
			//String chosenMonth = monthSpinner.getSelectedItem().toString();
			
			//String feedbackString = "You have enroled for " + chosenInstrument + " lessons, starting in " + chosenMonth;
			//TextView textViewFeedback = (TextView) findViewById(R.id.txtResult);
			//textViewFeedback.setText(feedbackString);
			
		}
    }
    
    public class CreateFragmentButtonHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) 
		{
			confirmMusic = new MusicFragment();
			
			FragmentManager fm = getFragmentManager();
			confirmMusic.show(fm, "confirm");			
		}
    	
    }
    
    public void giveMeMyData(boolean orderMusic)
    {
    	confirmMusic.dismiss();
    	
    	TextView textViewFeedback = (TextView) findViewById(R.id.txtResult);
    	
    	if (orderMusic)
    	{
    		enrolment();
    	}
    	else
    	{
    		textViewFeedback.setText("You have not been enrolled");
    	}
    }
    
    public void enrolment()
    {
    	RadioButton chosenButton = (RadioButton) findViewById(instrumentGroup.getCheckedRadioButtonId());
		String chosenInstrument = chosenButton.getText().toString();
		String chosenMonth = monthSpinner.getSelectedItem().toString();
		
		String feedbackString = "You have enroled for " + chosenInstrument + " lessons, starting in " + chosenMonth;
		TextView textViewFeedback = (TextView) findViewById(R.id.txtResult);
		textViewFeedback.setText(feedbackString);
    	
    }
    
    
}
