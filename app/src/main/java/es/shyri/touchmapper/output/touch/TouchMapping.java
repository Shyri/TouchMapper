package es.shyri.touchmapper.output.touch;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import es.shyri.touchmapper.output.TouchSimulator;

/**
 * Created by shyri on 23/12/2017.
 */

public abstract class TouchMapping {
    public static final String PAYMENT_TYPE_KEY = "type";

    protected transient TouchSimulator touchSimulator;
    protected transient int pointerId;

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public void setTouchSimulator(TouchSimulator touchSimulator) {
        this.touchSimulator = touchSimulator;
    }

    public abstract void processEvent(KeyEvent keyEvent);

    public abstract void processEvent(MotionEvent keyEvent);

    protected float getCenteredAxis(MotionEvent event, InputDevice device, int axis) {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value = event.getAxisValue(axis);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }
}
