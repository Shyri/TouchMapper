package es.shyri.touchmapper.output.touch;

import android.view.InputDevice;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import es.shyri.touchmapper.log.Log;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by shyri on 09/09/17.
 */

public class CircleMapping extends TouchMapping {
    private transient int status = 0;
    int axis_x;
    int axis_y;

    int x;
    int y;
    int radius;

    public CircleMapping(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void processEvent(MotionEvent event) {

        Log.l("Processing Event: " + event);
        InputDevice inputDevice = event.getDevice();

        float centeredAxisX = getCenteredAxis(event, inputDevice, axis_x);

        float maxX = inputDevice.getMotionRange(axis_x)
                                .getMax();
        float x1 = radius * (centeredAxisX / maxX);

        float centeredAxisY = getCenteredAxis(event, inputDevice, axis_y);

        float maxY = inputDevice.getMotionRange(axis_y)
                                .getMax();
        float y1 = radius * (centeredAxisY / maxY);

        try {

            switch (status) {
                case 0:
                    touchSimulator.simulateTouch(pointerId, ACTION_DOWN, (int) (x + x1), (int) (y + y1));
                    status = 1;
                    break;
                case 1:
                    if (centeredAxisX == 0 && centeredAxisY == 0) {
                        touchSimulator.simulateTouch(pointerId, ACTION_UP, (int) (x + x1), (int) (y + y1));
                        status = 0;
                    } else {
                        touchSimulator.simulateTouch(pointerId, event.getAction(), (int) (x + x1), (int) (y + y1));
                    }

                    break;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
