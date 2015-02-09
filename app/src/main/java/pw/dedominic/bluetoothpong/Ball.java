package pw.dedominic.bluetoothpong;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by prussian on 2/8/15.
 * The ping pong ball object
 */
public class Ball
{
    //defines ball size
    private int radius;

    // x and y coords
    public double x;
    public double y;

    // amount balls move along x and y every frame
    private double x_vel;
    private double y_vel;
    private float speed_mult;

    public Ball()
    {
        x = 0;
        y = 0;
        radius = 15;
        speed_mult = 1;
    }

    // takes x y starting coords, likely screen width / 2 = x, screen height / 2 = y.
    public Ball(double x_, double y_)
    {
        x = x_;
        y = y_;
        radius = 15;
        speed_mult = 1;
    }

    // takes radius val too
    public Ball(double x_, double y_, int rad)
    {
        x = x_;
        y = y_;
        radius = rad;
        speed_mult = 1;
    }

    // to set velocity
    public void setVel(double vel_x, double vel_y)
    {
        x_vel = vel_x;
        y_vel = vel_y;
    }

    public int getRad()
    {
        return radius;
    }

    // when bounces off ceiling/floor
    public void yDeflect()
    {
        y_vel = -y_vel;
    }

    // when bounces off paddle
    public void xDeflect()
    {
        //speed_mult += .1;
        //x_vel *= speed_mult;
        x_vel = -x_vel;
    }

    public void update(Canvas screen, Paint color)
    {
        screen.drawCircle((float)x, (float)y, radius, color);
        x += x_vel;
        y += y_vel;
    }
}
