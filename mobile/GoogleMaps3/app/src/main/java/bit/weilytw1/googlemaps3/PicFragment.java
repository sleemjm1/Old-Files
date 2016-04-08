package bit.weilytw1.googlemaps3;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by weilytw1 on 23/05/2015.
 */
public class PicFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.picmapp_fragment, container, false);
        //Do required setup for the fragment
        ImageView ivUserPic = (ImageView) getView().findViewById(R.id.ivUserPic);
        String imageString = getArguments().getString("imageString");
        new ImageLoadTask("http://webthree.ict.op.ac.nz/sleemjm1/Test/" + imageString, ivUserPic).execute();

        return v;
    }
}
