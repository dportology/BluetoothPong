package pw.dedominic.bluetoothpong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends ActionBarActivity implements SensorEventListener
{
    // Constants
    public static final int BLUETOOTH_INIT_SERVER = 0;
    public static final int BLUETOOTH_CONN_TO_SERVER = 1;
    private PongView mPongView;
    private BluetoothPongService mBtPongService;

    private int bluetoothState = Consts.STATE_DOING_NOTHING;
    private int paddleSide = Consts.PLAYER_PADDLE_LEFT;

    private SensorManager senManage;
    private Sensor accelerometer;

    private BluetoothAdapter mBluetoothAdapter;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Consts.READING:
                    // read from opponent
                    return;
                case Consts.SYNCHRONIZE:
                    // send/receive periodic state info to keep things in check
                    return;
                case Consts.CONNECT_STATE_CHANGE:
                    // handle changes in connection state
                    return;
            }
        }

        @Override
        public void close()
        {

        }

        @Override
        public void flush()
        {

        }

        @Override
        public void publish(LogRecord record)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // keeps screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Sensor change handler
    // called when accelerometer detects a tilt
    @Override
    public void onSensorChanged(SensorEvent e)
    {
        // only rotation around y axis needed.
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mPongView.player_tilt(e.values[0]);

            // above value in bytes to be written out to opponent
            byte[] writeFloat = ByteBuffer.allocate(4).putFloat(e.values[0]).array();

            mBtPongService.write(writeFloat);
        }
    }

    public void onOpponentSensorChange(SensorEvent e)
    {
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mPongView.player_tilt(e.values[0]);

        }
    }

    // ignores this
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    protected void connectToEnemy(int type)
    {
        int REQUEST_ENABLE_BT = 1;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (type == BLUETOOTH_INIT_SERVER)
        {
            mBtPongService.listen();
        }
        else
        {
            Intent getBtDevice = new Intent(this, PairedBluetoothActivity.class);
            startActivity(getBtDevice);
            String MAC_ADDRESS = getBtDevice.getStringExtra("device");

            BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(MAC_ADDRESS);

            mBtPongService.connectToHost(mDevice);
        }
    }

    // when connection is set, call this
    protected void gameStart()
    {
        // sets accelerometer input device
        senManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = senManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senManage.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_GAME);

        // sets PongView to View field in activity
        mPongView = (PongView) findViewById(R.id.view);
        mPongView.update(); // starts the view off
    }

    // app out of focus
    protected void onPause()
    {
        super.onPause();
        senManage.unregisterListener(this);
    }

    // app focus returns
    protected void onResume()
    {
        super.onResume();
        senManage.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
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
        switch (id)
        {
            case R.id.action_connect:
                connectToEnemy(BLUETOOTH_CONN_TO_SERVER);
                return true;
            case R.id.action_listen:
                connectToEnemy(BLUETOOTH_INIT_SERVER);
                return true;
            case R.id.action_start_game:
                if (bluetoothState == Consts.STATE_IS_CONNECTED)
                {
                    gameStart();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
