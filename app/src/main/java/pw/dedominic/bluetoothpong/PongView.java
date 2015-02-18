package pw.dedominic.bluetoothpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;

import java.util.Random;

/**
 * Created by prussian on 2/8/15.
 */
public class PongView extends View
{
    // Constants
    public final int FPS = 60;
    public final int paddle_space = 75;
    public final int paddle_half_height = 100;
    public final int paddle_half_thickness = 8;
    public final int paddle_thickness = paddle_half_thickness*2;
    private static final Random rand = new Random();

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
        public void sleep(long time)
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
            newGame();
        }

        if (ball.getLeft() <= 0
            || ball.getRight() >= getWidth())
        {
            newGame();
        }

        if (ball.getTop() <= 0
            || ball.getBottom() >= getHeight())
        {
            ball.yDeflect();
        }

        if (ball.getX_vel() < 0)
        {
            if (ball.getBottom()>= player_paddle.getTop()  &&
                ball.getLeft()  >= player_paddle.getLeft() &&
                ball.getRight() <= player_paddle.getRight()&&
                ball.getTop()   <= player_paddle.getBottom())
            {
                ball.yDeflect();
                //ball.xDeflect();
            }
            else if (ball.y >= player_paddle.getTop()    &&
                     ball.y <= player_paddle.getBottom() &&
                     ball.getLeft() <= paddle_space+paddle_thickness &&
                     ball.getLeft() >= paddle_space)
            {
                ball.xDeflect();
            }
        }
        else
        {
            if (ball.getBottom()>= enemy_paddle.getTop()  &&
                ball.getLeft()  >= enemy_paddle.getLeft() &&
                ball.getRight() <= enemy_paddle.getRight()&&
                ball.getTop()   <= enemy_paddle.getBottom())
            {
                ball.yDeflect();
                ball.xDeflect();
            }
            else if (ball.y >= enemy_paddle.getTop()    &&
                     ball.y <= enemy_paddle.getBottom() &&
                     ball.getRight() >= getWidth() - (paddle_space+paddle_thickness) &&
                     ball.getRight() <= getWidth() - (paddle_space))
            {
                ball.xDeflect();
            }
        }

        // time till next frame in milliseconds
        redraw.sleep(1000/FPS);
    }

    public void newGame()
    {
        ball = new Ball(getWidth()/2,getHeight()/2);
        ball.setVel(randomAngle(), 6);

        player_paddle = new Paddle(paddle_space,getHeight()/2,paddle_half_height,paddle_thickness);
        enemy_paddle = new Paddle(getWidth()-paddle_space,getHeight()/2,paddle_half_height,paddle_thickness);

        isInit = true;
    }

    // returns random angle in radians
    public double randomAngle()
    {
        int max = 63;
        int min = -max;
        return (rand.nextInt((max - min) + 1) + min) * .1;
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
            if (player_paddle.getTop() >= 0)
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
            if (player_paddle.getBottom() <= getHeight())
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
