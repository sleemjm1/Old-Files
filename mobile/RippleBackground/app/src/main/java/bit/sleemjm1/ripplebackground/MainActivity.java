package bit.sleemjm1.ripplebackground;

//Name: Joe Sleeman
//Class: Design and Dev for Mobile
//Description: This is the final lab for Design and Dev for mobile
//Instructions: Click the image to see the ripple effect. Click any of the buttons at the top to see other effects

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ExplodeAnimation;
import com.skyfishjy.library.RippleBackground;


public class MainActivity extends ActionBarActivity
{
    ImageView btnRipple;
    boolean clicked;
    Button btnExplode;
    Button btnTransform;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       clicked = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRipple = (ImageView) findViewById(R.id.ivPicture);
        btnExplode = (Button) findViewById(R.id.btnExplode);
        btnTransform = (Button) findViewById(R.id.btnTransform);

        btnRipple.setOnClickListener(new rippleHandler());
        btnExplode.setOnClickListener(new explodeHandler());
        btnTransform.setOnClickListener(new standUpHandler());
    }

    public class rippleHandler implements View.OnClickListener
    {
        final RippleBackground rippleBackground =(RippleBackground)findViewById(R.id.content);
        @Override
        public void onClick(View v)
        {
            if (!clicked) {
                rippleBackground.startRippleAnimation();
                clicked=true;
            }
            else
            {
                rippleBackground.stopRippleAnimation();
                clicked=false;
            }

        }
    }

    public class explodeHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            new ExplodeAnimation(btnRipple)
                    .setDuration(2000)
                    .setListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            btnRipple.setVisibility(View.VISIBLE);
                        }
                    })
                    .animate();
        }
    }

    public class standUpHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            YoYo.with(Techniques.StandUp)
                    .duration(2000)
                    .playOn(btnRipple);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
