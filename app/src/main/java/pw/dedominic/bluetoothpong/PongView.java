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
    // Constants
    public final int FPS = 60;
    public final int paddle_space = 100;
    public final int paddle_half_height = 100;
    public final int paddle_half_thickness = 5;
    public final int paddle_thickness = paddle_half_thickness*2;

    // redraws view
    private DrawHandler redraw = new DrawHandler();

    // color of ball
    private Paint paint;

    // ball and paddle
    private Ball ball;
    private Paddle player_paddle;
    private Paddle enemy_paddle;

    // if initialized
    private boolean isInit = false;

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
    }

    // currently just makes ball bounce around view
    public void update()
    {
        if (getHeight() == 0 || getWidth() == 0)
        {
            redraw.sleep(1);
            return;
        }

        if (!isInit)
        {
            ball = new Ball(getWidth()/2,getHeight()/2);
            ball.setVel(6,3);

            player_paddle = new Paddle(paddle_space,getHeight()/2,paddle_half_height,paddle_thickness);
            enemy_paddle = new Paddle(getWidth()-paddle_space,getHeight()/2,paddle_half_height,paddle_thickness);

            isInit = true;
        }

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

        // checks for paddle collision
        if (ball.getX_vel() < 0)
        {
            if (ball.x <= ball.getRad() + paddle_space + paddle_thickness)
            {
                if (player_paddle.getY()-paddle_half_height <= ball.y
                    && ball.y <= player_paddle.getY()+paddle_half_height)
                {
                    ball.xDeflect();
                }
            }
        }
        else
        {
            if (ball.x >= (getWidth()-(paddle_space+paddle_thickness)) -  ball.getRad())
            {
                if (enemy_paddle.getY()-paddle_half_height <= ball.y
                    && ball.y <= enemy_paddle.getY()+paddle_half_height)
                {
                    ball.xDeflect();
                }
            }
        }

        // time till next frame in milliseconds
        redraw.sleep(1000/FPS);
    }

    public void tilted(float tilt_val)
    {
        tilt_val *= 2;

        if (!isInit)
        {
            return;
        }

        // makes sure paddles don't go off screen
        if (tilt_val < 0)
        {
            if (player_paddle.getY() >= paddle_half_height)
            {
                player_paddle.paddleMove(tilt_val);
                enemy_paddle.paddleMove(tilt_val);
            }
            else
            {
                player_paddle.paddleMove(0);
                enemy_paddle.paddleMove(0);
            }
        }
        else
        {
            if (player_paddle.getY() <= getHeight() - paddle_half_height)
            {
                player_paddle.paddleMove(tilt_val);
                enemy_paddle.paddleMove(tilt_val);
            }
            else
            {
                player_paddle.paddleMove(0);
                enemy_paddle.paddleMove(0);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (!isInit)
        {
            return;
        }

        ball.update(canvas, paint);
        player_paddle.update(canvas, paint);
        enemy_paddle.update(canvas, paint);
    }
}
