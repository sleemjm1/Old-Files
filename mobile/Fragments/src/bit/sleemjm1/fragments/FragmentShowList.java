package bit.sleemjm1.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentShowList  extends Fragment
{	
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.show_a_list_fragment, container, false);
			
			//do any required setup for the fragment view here
			
			return v;
			
		}

	

}
