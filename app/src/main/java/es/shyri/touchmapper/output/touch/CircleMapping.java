package es.shyri.touchmapper.output.touch;

import android.view.InputDevice;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import es.shyri.touchmapper.log.Log;
import es.shyri.touchmapper.output.TouchSimulator;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by shyri on 09/09/17.
 */

public class CircleMapping {
    private final TouchSimulator touchSimulator;
    int id;
    int x;
    int y;
    int radius;

    int status = 0;

    public CircleMapping(int id, int x, int y, int radius, TouchSimulator touchSimulator) {
        this.id = id;
        this.touchSimulator = touchSimulator;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void processEvent(MotionEvent event) {
        Log.l("Processing Event" + event);
        InputDevice mInputDevice = event.getDevice();

        float axis_x = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_X);

        float max = mInputDevice.getMotionRange(MotionEvent.AXIS_X)
                                .getMax();
        float x1 = radius * (axis_x / max);

        float axis_y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Y);

        float maxY = mInputDevice.getMotionRange(MotionEvent.AXIS_Y)
                                 .getMax();
        float y1 = radius * (axis_y / maxY);
        try {

            switch (status) {
                case 0:
                    touchSimulator.simulateTouch(id, ACTION_DOWN, (int) (x + x1), (int) (y + y1));
                    status = 1;
                    break;
                case 1:
                    if (x1 == 0 && y == 0) {
                        touchSimulator.simulateTouch(id, ACTION_UP, (int) (x + x1), (int) (y + y1));
                        status = 0;
                    } else {
                        touchSimulator.simulateTouch(id, event.getAction(), (int) (x + x1), (int) (y + y1));
                    }
                    break;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //        float axis_y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Y);

        //        if (event.getKeyCode() == keyCode) {
        //            try {
        //                touchSimulator.simulateTouch(event.getAction(), x, y);
        //            } catch (InvocationTargetException | IllegalAccessException e) {
        //                e.printStackTrace();
        //            }
        //        }
    }

    private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis) {
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
