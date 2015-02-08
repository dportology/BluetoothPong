package pw.dedominic.bluetoothpong;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by prussian on 2/8/15.
 */
public class PongView extends View
{
    Paint paint;

    // ball and paddle
    Ball ball;

    public PongView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(0xf6666666); // solid gray

        ball = new Ball(100,200);
        ball.setVel(1,1);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        ball.update(canvas, paint);
    }
}
