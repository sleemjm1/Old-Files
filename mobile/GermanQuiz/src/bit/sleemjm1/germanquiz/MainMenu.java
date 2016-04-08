package bit.sleemjm1.germanquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity 
{
	Button ButtonClick;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		ButtonClick = (Button) findViewById(R.id.btnStart);
        
        ButtonClick.setOnClickListener(new ClickHandler());
	}
	
	public class ClickHandler implements OnClickListener
	{

		@Override
		public void onClick(View v) 
		{
			Intent changeActivityIntent = new Intent(MainMenu.this, Questions.class);
			startActivity(changeActivityIntent);			
		}
		
	}

	
}
