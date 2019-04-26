# step_count_using_sensor
count steps you walk using sensor.

This Android application tutorial is for counting the number of steps using inbuilt sensors in device.

To use Sensors in android we need to implement ```SensorEventListener``` in MainActivity.java class of our project.
After implementing, we need to override it's methods. Here for ```SensorEventListener``` there are two methods.
1. ```OnSensorChanged()```
2. ```OnAccuracyChanged()```

To get Sensors, use SensorManager, SensorManager is used for getting services of sensors to do that create SensorManager variable inside your class.
```SensorManager sm;```
Then copy below line of code in your ```OnCreate()``` method.
```
sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
```
Now create Sensor variables in your class that you want to use in your peoject In my case I have two sensors.
```
Sensor countSensor, AccelSensor;
```
Copy below two lines in your ```OnCreate()``` Method
```
countSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
AccelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
```

In ```OnResume()``` Method register both sensors. Accelerometer sensor is mostly available in all devices but Step Counter is not so I have used condition for that.
Copy below code in your ```OnRegister()``` method.
```
sm.registerListener(this,AccelSensor,SensorManager.SENSOR_DELAY_NORMAL);
if(countSensor != null){
            running = true;
            sm.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this,"Sensor Found",Toast.LENGTH_LONG).show();
}
else {
            Toast.makeText(this,"Sensor Not available",Toast.LENGTH_LONG).show();
     }
```

In ```OuPause()``` in we will unregister Sensor. Copy below line in ```OnPause()``` Method.
```
sm.unregisterListener(this,AccelSensor);
```
Now you can put your own code in ```OnSensorChanged()``` Method. See the code of ```MainActivity.java``` to count your steps and when you shake your phone fast step counter will be ste to zero and it will start again when you start walking again.
We get the result in form of array while using the ```OnSensorChanged()``` method.
