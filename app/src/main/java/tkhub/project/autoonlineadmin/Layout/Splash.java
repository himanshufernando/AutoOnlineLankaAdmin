package tkhub.project.autoonlineadmin.Layout;

import android.app.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import tkhub.project.autoonlineadmin.R;


/**
 * Created by Himanshu on 11/8/2016.
 */

public class Splash extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);



        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(Splash.this, Login.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                finish();
                startActivity(i, bndlanimation);
            }
        }, 3000);
    }

}
