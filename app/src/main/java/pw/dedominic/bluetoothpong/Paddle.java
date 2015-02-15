package pw.dedominic.bluetoothpong;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by prussian on 1/31/15.
 */
public class Paddle
{
    private float x;
    private float y;
    private int paddle_half_width;
    private int paddle_half_height;
    private float paddle_vel;

    public Paddle(float pos1, float pos2, int h, int w)
    {
        x = pos1;
        y = pos2;

        paddle_half_width = w;

        paddle_half_height = h;

        paddle_vel = 0;
    }

    public void paddleMove(float tilt_val)
    {
        paddle_vel = tilt_val;
    }

    public float getY()
    {
        return y;
    }

    public float getX()
    {
        return x;
    }

    public void update(Canvas screen, Paint color)
    {
        screen.drawRect(x-paddle_half_width,
                        y-paddle_half_height,
                        x+paddle_half_width,
                        y+paddle_half_height,
                        color);
        y += paddle_vel;
    }
}
