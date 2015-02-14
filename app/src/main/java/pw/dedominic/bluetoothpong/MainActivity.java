package pw.dedominic.bluetoothpong;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SensorEventListener
{

    PongView game_field;

    SensorManager senManage;
    Sensor accelerometer;

    float prev_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets accelerometer input device
        senManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = senManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senManage.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        // sets PongView to View field in activity
        game_field = (PongView) findViewById(R.id.view);
        game_field.update(); // starts the view off
    }

    @Override
    public void onSensorChanged(SensorEvent e)
    {
        // only axis needed, z.
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            game_field.tilted(e.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    protected void onPause()
    {
        super.onPause();
        senManage.unregisterListener(this);
    }

    protected void onResume()
    {
        super.onResume();
        senManage.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
