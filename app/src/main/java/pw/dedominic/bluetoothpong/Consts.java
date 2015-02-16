package pw.dedominic.bluetoothpong;

import android.graphics.Paint;

/**
 * Created by prussian on 2/16/15.
 */
public interface Consts
{
    // messages
    public static final int READING = 0;
    public static final int SYNCHRONIZE = 1;
    public static final int CONNECT_STATE_CHANGE = 2;

    // game constants
    public static final int PLAYER_PADDLE_LEFT  = 1;
    public static final int PLAYER_PADDLE_RIGHT =-1;
    public static final int FRAMES_PER_SECOND   =60;

    public static final double PADDLE_SPACE_FRACT  = 1 / 16;
    public static final double PADDLE_WIDTH_FRACT  = 1 / 16;
    public static final double PADDLE_HEIGHT_FRACT = 1 / 5;

    public static final int PAINT_COLOR = 0xff666666; // grey

    // bluetooth services
    public static final int STATE_DOING_NOTHING = 0;
    public static final int STATE_IS_LISTENING  = 1;
    public static final int STATE_IS_CONNECTING = 2;
    public static final int STATE_IS_CONNECTED  = 3;
}
