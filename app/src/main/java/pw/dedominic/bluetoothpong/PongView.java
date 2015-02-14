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
    public Paddle player_paddle;
    public Paddle enemy_paddle;

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

        player_paddle = new Paddle(50,500,100,10);
        enemy_paddle = new Paddle(getWidth()-50,500,100,10);
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

    public void tilted(float tilt_val)
    {
        if (player_paddle.getY()-player_paddle.getPaddle_half_height() > 0
            && player_paddle.getY() + player_paddle.getPaddle_half_height() < getHeight())
        {
            player_paddle.paddleMove(tilt_val);
            enemy_paddle.paddleMove(tilt_val);
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        ball.update(canvas, paint);
        player_paddle.update(canvas, paint);
        enemy_paddle.update(canvas, paint);
    }
}
