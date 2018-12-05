package es.shyri.touchmapper.output.touch;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import es.shyri.touchmapper.log.Log;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by shyri on 11/05/2018.
 */

public class FPSJoystick extends TouchMapping {
    public static final String KEY_TYPE = "FPS_JOYSTICK";
    int x;
    int y;

    int radius;

    int axis_x;
    int axis_y;

    private transient int status = 0;

    private transient int lastX;

    @Override
    public void processEvent(KeyEvent keyEvent) {

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
        Log.l("x1: " + x1);

        try {

            switch (status) {
                case 0:
                    if (Math.abs(centeredAxisX) >= 0.01 || Math.abs(centeredAxisY) >= 0.01) {
                        touchSimulator.simulateTouch(pointerId, ACTION_DOWN, (int) (x + x1), y);
                        status = 1;
                        lastX = x;
                    }
                    break;
                case 1:
                    if (Math.abs(centeredAxisX) < 0.01 && Math.abs(centeredAxisY) < 0.01) {
                        touchSimulator.simulateTouch(pointerId, ACTION_UP, lastX, y);
                        status = 0;
                    } else {
                        if (Math.abs(lastX - x) >= radius) {
                            touchSimulator.simulateTouch(pointerId, ACTION_UP, lastX, y);
                            status = 0;
                        } else {
                            lastX = (int) (lastX + 1);
                            touchSimulator.simulateTouch(pointerId, event.getAction(), lastX, y + 1);
                        }
                    }

                    break;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
