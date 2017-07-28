package com.google.android.gms.samples.vision.barcodereader;



import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class Splash extends Activity {



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();


        setContentView(R.layout.activity_splash);



        new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(3000);
                    Intent i= new Intent(Splash.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                catch(InterruptedException e)
                {
                }

            }
        }.start();
    }


}