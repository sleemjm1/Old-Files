package bit.sleemjm1.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity 
{
	Button btnMakeFragment;
	Button btnMakeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnMakeFragment = (Button)findViewById(R.id.btnShowImageFragment);
        btnMakeFragment.setOnClickListener(new ImageClickHandler());
    }
  
    
    public class ImageClickHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) 
		{
			Fragment dynamicFragment = new FragmentShowImage();
    		FragmentManager fm = getFragmentManager();
    		
    		FragmentTransaction ft = fm.beginTransaction();
    		
    		//make all your fragment changes here..
    		
    		ft.replace(R.id.fragment_container, dynamicFragment);
    		
    		ft.commit();
			
		}
    	
    }
    
    public class ListClickHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) 
		{
			Fragment dynamicFragment = new FragmentShowList();
			FragmentManager fm = getFragmentManager();
			
			FragmentTransaction ft = fm.beginTransaction();
			
			//make all your fragment changes here..
			
			ft.replace(R.id.fragment_container, dynamicFragment);
			
			ft.commit();			
		}
    	
    }
}
