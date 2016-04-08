package bit.sleemjm1.musicsurvey;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class MusicFragment extends DialogFragment
{
	public MusicFragment() {}	//empty constructor again
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("You sure, bro?");
		builder.setPositiveButton("yes", new YesButtonHandler());
		builder.setNegativeButton("no", new NoButtonHandler());
		
		Dialog customDialog = builder.create();
		
		return customDialog;
		
		
	}
	
	public class YesButtonHandler implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int whichButton)
		{
			MainActivity myActivity = (MainActivity) getActivity();
			myActivity.giveMeMyData(true);
		}
	}
	
	public class NoButtonHandler implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int whichButton)
		{
			MainActivity myActivity = (MainActivity) getActivity();
			myActivity.giveMeMyData(false);
		}
	}

}
