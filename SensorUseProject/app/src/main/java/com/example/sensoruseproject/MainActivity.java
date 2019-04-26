package com.example.sensoruseproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sm;
    TextView mySteps;
    boolean running = false;
    Sensor countSensor, AccelSensor;
    private int steps = 0;
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast;
    private float mAccel;
    float current_steps = 0.0f;
    boolean shake = false;
    int system_steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySteps = (TextView) findViewById(R.id.steps);
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        countSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        AccelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this,AccelSensor,SensorManager.SENSOR_DELAY_NORMAL);
        if(countSensor != null){
            running = true;
            sm.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this,"Sensor Found",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Sensor Not available",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sm.unregisterListener(this,AccelSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 12) {
                Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
                toast.show();
                mySteps.setText("0");
                shake = true;
            }
        }
        else if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(shake){
                shake = false;
                system_steps = (int)event.values[0];
            }
            if (running) {
                mySteps.setText(String.valueOf((int)event.values[0]-system_steps));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
