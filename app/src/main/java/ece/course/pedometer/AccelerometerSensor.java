package ece.course.pedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by mingzheliu on 2017/9/23.
 */

public class AccelerometerSensor implements SensorEventListener {

    public final static String TAG_VALUE_DX = "tagValueDx";
    public final static String TAG_VALUE_DY = "tagValueDy";
    public final static String TAG_VALUE_DZ = "tagValueDz";

    private boolean isStarted = false;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Handler handler;


    public AccelerometerSensor(Context context, Handler handler)
    {
        this.handler = handler;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onAccuracyChanged(Sensor sensor, int accurary)
    {

    }

    public void startListening()
    {
        if(isStarted) return;

        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_UI);
        isStarted = true;
    }

    public void stopListening()
    {
        if(!isStarted) return;

        sensorManager.unregisterListener(this);
        isStarted = false;
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if(sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float dx = sensorEvent.values[0];
        float dy = sensorEvent.values[1];
        float dz = sensorEvent.values[2];

        if (handler != null)
        {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();

            bundle.putFloat(TAG_VALUE_DX,dx);
            bundle.putFloat(TAG_VALUE_DY,dy);
            bundle.putFloat(TAG_VALUE_DZ,dz);

            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
