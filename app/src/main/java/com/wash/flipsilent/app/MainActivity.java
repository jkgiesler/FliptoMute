package com.wash.flipsilent.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private boolean vibDown = false;
    private boolean vibUp = false;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private double oriCount=0;
    private double eventSum=0;
    private double detectionTime=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the accelerometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        final CheckBox box1=(CheckBox)findViewById(R.id.checkBox2);
        final CheckBox box2=(CheckBox)findViewById(R.id.checkBox);
        Button button = (Button)findViewById(R.id.button);
        if (box1.isChecked()) {
            vibDown = true;
            } else {
            vibDown = false;
            }
        if (box2.isChecked()) {
            vibUp = true;
            } else {
            vibUp = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //what do I do with this!
    }

    public void onSensorChanged(SensorEvent event) {
        //code to calculate change in acceleration in XYZ
        double z = event.values[2];
        AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if(oriCount<detectionTime){
           oriCount++;
           eventSum+=z;
        } else {
            if (eventSum < -(8 * detectionTime)) {

                double ans = oriCount / detectionTime;
                String strAns = Double.toString(ans);
                if(vibDown){
                    audio.setRingerMode(1);
                } else {
                    //if user does not want phone to vibrate face down
                    audio.setRingerMode(0);
                }


                oriCount = 0;
                eventSum = 0;

            }
            else if (eventSum > -(8 * detectionTime)) {

                String strAns = Double.toString(eventSum);

                if(vibUp) {
                    audio.setRingerMode(1);
                }else{
                    //if user wants phone to ring face up
                    audio.setRingerMode(2);
                }
                oriCount = 0;
                eventSum = 0;
            }
        }

    }
}
