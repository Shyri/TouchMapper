package es.shyri.touchmapper.output.touch;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by shyri on 09/05/2018.
 */

public class SlideMapping extends TouchMapping {
    public static final String KEY_TYPE = "SLIDE";
    private static final int AXIS_X = 0;
    private static final int AXIS_Y = 0;

    private int axis;
    private int startValue;
    private int endValue;

    @Override
    public void processEvent(KeyEvent keyEvent) {

    }

    @Override
    public void processEvent(MotionEvent keyEvent) {

    }
}
