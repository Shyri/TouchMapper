package es.shyri.touchmapper.output.touch;

import android.view.InputDevice;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by shyri on 09/09/17.
 */

public class CircleMapping extends TouchMapping {
    public static final String KEY_TYPE = "CIRCLE";

    int axis_x;
    int axis_y;

    int x;
    int y;
    int radius;

    private transient int status = 0;
    private transient long lastSent = 0;

    public CircleMapping(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
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
                    if (Math.abs(centeredAxisX) >= 0.01 || Math.abs(centeredAxisY) >= 0.01) {
                        touchSimulator.simulateTouch(pointerId, ACTION_DOWN, (int) (x + x1), (int) (y + y1));
                        status = 1;
                    }
                    break;
                case 1:
                    if (Math.abs(centeredAxisX) < 0.01 && Math.abs(centeredAxisY) < 0.01) {
                        touchSimulator.simulateTouch(pointerId, ACTION_UP, (int) (x + x1), (int) (y + y1));
                        status = 0;
                    } else {
                        long now = System.currentTimeMillis();
                        if (now - lastSent > 50) {
                            touchSimulator.simulateTouch(pointerId, event.getAction(), (int) (x + x1), (int) (y + y1));
                            lastSent = now;
                        }
                    }

                    break;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
