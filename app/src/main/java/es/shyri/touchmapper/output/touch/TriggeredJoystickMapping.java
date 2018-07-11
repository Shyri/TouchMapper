package es.shyri.touchmapper.output.touch;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by shyri on 11/05/2018.
 */

public class TriggeredJoystickMapping extends TouchMapping {
    private transient int status = 0;
    private transient long lastSent = 0;

    int axis_x;
    int axis_y;

    int x;
    int y;
    int radius;
    int triggerKey;

    private transient int lastX;
    private transient int lastY;

    public void processEvent(KeyEvent event) {
        //Log.l("Processing Event: " + event.getScanCode());

        if (event.getKeyCode() == triggerKey) {
            try {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (status == 0) {
                        lastX = x;
                        lastY = y;
                        touchSimulator.simulateTouch(pointerId, event.getAction(), x, y);
                        status = 1;
                    }

                } else if (status == 1) {
                    status = 0;
                    touchSimulator.simulateTouch(pointerId, event.getAction(), lastX, lastY);
                }

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void processEvent(MotionEvent event) {

        //Log.l("Processing Event: " + event);
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
                    // ignore
                    break;
                case 1:

                    if (Math.abs(centeredAxisX) <= 0.01 && Math.abs(centeredAxisY) <= 0.01 && Math.abs(lastX) > 0.01 &&
                        Math.abs(lastY) > 0.01) {
                        touchSimulator.simulateTouch(pointerId, ACTION_UP, (int) (x + x1), (int) (y + y1));
                        status = 0;
                    } else {

                        long now = System.currentTimeMillis();
                        if (now - lastSent > 50) {
                            touchSimulator.simulateTouch(pointerId, event.getAction(), (int) (x + x1), (int) (y + y1));
                            lastSent = now;
                        }
                    }
                    lastX = (int) (x + x1);
                    lastY = (int) (y + y1);

                    break;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
