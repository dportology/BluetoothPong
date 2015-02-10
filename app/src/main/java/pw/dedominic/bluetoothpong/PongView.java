package pw.dedominic.bluetoothpong;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;

/**
 * Created by prussian on 2/8/15.
 */
public class PongView extends View
{
    // fps and redrawing the view
    public final int FPS = 60;
    private DrawHandler redraw = new DrawHandler();

    // color of ball
    private Paint paint;

    // ball and paddle
    private Ball ball;

    // creates a thread that will update and draw the view
    // based on delay set by call to sleep in PongView.update() function
    private class DrawHandler extends Handler
    {
        // once message is received, calls update
        // and invalidates the view
        // this effectively redraws scene
        @Override
        public void handleMessage(Message msg)
        {
            PongView.this.update();
            PongView.this.invalidate(); // force redraw
        }

        // time in milliseconds to draw next frame
        public void sleep(int time)
        {
            this.removeMessages(0);
            this.sendMessageDelayed(obtainMessage(0), time);
        }
    }

    public PongView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(0xff666666); // solid gray

        ball = new Ball(100,100);
        ball.setVel(6,3);
    }


    // currently just makes ball bounce around view
    public void update()
    {
        if (ball.x <= ball.getRad()
            || ball.x >= getWidth() - ball.getRad())
        {
            ball.xDeflect();
        }

        if (ball.y <= ball.getRad()
            || ball.y >= getHeight() - ball.getRad())
        {
            ball.yDeflect();
        }

        // time till next frame in milliseconds
        redraw.sleep(1000/FPS);
    }

    public void TEST_TILT()
    {
        ball.yDeflect();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        ball.update(canvas, paint);
    }
}
