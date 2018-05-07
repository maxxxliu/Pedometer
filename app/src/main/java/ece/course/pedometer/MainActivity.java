package ece.course.pedometer;

import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {

    private final static float MAX_GRAVITY = 9.24f;
    private float mX = -100.0f;
    private float mY = -100.0f;
    private float mZ = -100.0f;
    private float mX1 = -100.0f;
    private float mY1 = -100.0f;
    private float mZ1 = -100.0f;
    private float THRESHOLD = 0.039f;
    private int count = 0;

    private DisplayView displayView;
    private AccelerometerSensor accelerometerSensor;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
        displayView = (DisplayView) findViewById(R.id.DisplayView);
        accelerometerSensor = new AccelerometerSensor(this, new Handler()
        {
            @Override
            public void handleMessage(Message message)
            {
                float tempX = message.getData().getFloat(AccelerometerSensor.TAG_VALUE_DX);
                float tempY = -message.getData().getFloat(AccelerometerSensor.TAG_VALUE_DY);
                float tempZ = message.getData().getFloat(AccelerometerSensor.TAG_VALUE_DZ);

                if(tempX - mX > THRESHOLD || tempX - mX < -THRESHOLD || tempY - mY > THRESHOLD || tempY - mY < -THRESHOLD || tempZ - mZ > THRESHOLD || tempZ - mZ < -THRESHOLD)
                {
                    mX1 = mX;
                    mY1 = mY;
                    mZ1 = mZ;
                    mX = tempX;
                    mY = tempY;
                    mZ = tempZ;
                    TextView tvValueX = (TextView) findViewById(R.id.tvValueX);
                    TextView tvValueY = (TextView) findViewById(R.id.tvValueY);
                    TextView tvValueZ = (TextView) findViewById(R.id.tvValueZ);
                    TextView tvValueTF = (TextView) findViewById(R.id.tvValueTF);
                    TextView tvValueC = (TextView) findViewById(R.id.tvValueC);

                    tvValueX.setText("" + mX);
                    tvValueY.setText("" + mY);
                    tvValueZ.setText("" + mZ);
                    tvValueTF.setText("" + sqrt(mX * mX + mY * mY + mZ * mZ));
                    if (abs(sqrt(mX * mX + mY * mY + mZ * mZ)-sqrt(mX1 * mX1 + mY1 * mY1 + mZ1 * mZ1)) > 5) count++;
                    if (abs(sqrt(mX * mX + mY * mY + mZ * mZ)-sqrt(mX1 * mX1 + mY1 * mY1 + mZ1 * mZ1)) > 6.7) count--;
                    tvValueC.setText("" + count);

                    displayView.setPtr(0.0f , ((float) (sqrt(mX * mX + mY * mY + mZ * mZ)-sqrt(mX1 * mX1 + mY1 * mY1 + mZ1 * mZ1)))/ (2 * MAX_GRAVITY));
                }
            }
        });
    }

    public synchronized void onResume()
    {
        super.onResume();
        if(accelerometerSensor != null) accelerometerSensor.startListening();
        wakeLock.acquire();
    }

    public synchronized void onPause()
    {
        if(accelerometerSensor != null) accelerometerSensor.stopListening();
        wakeLock.release();
        super.onPause();
    }
     public void clear(View view)
     {
         count = 0;
     }
}
