package es.shyri.touchmapper.output.touch;

import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;

import es.shyri.touchmapper.log.Log;
import es.shyri.touchmapper.output.TouchSimulator;

import static android.view.KeyEvent.ACTION_UP;

/**
 * Created by shyri on 09/09/17.
 */

public class TapMapping extends TouchMapping {
    private int keyCode;
    private int x;
    private int y;
    private int lastAction = ACTION_UP;
    private TouchSimulator touchSimulator;

    public TapMapping(int keyCode, int x, int y, TouchSimulator touchSimulator) {
        this.keyCode = keyCode;
        this.x = x;
        this.y = y;
        this.touchSimulator = touchSimulator;
        pointerId = 1;
    }

    public void processEvent(KeyEvent event) {
        Log.l("Processing Event: " + event.getScanCode());

        if (
            //                deviceDescriptor.equals(event.getDevice()
            //                                         .getDescriptor()) &&
                event.getKeyCode() == keyCode && lastAction != event.getAction()) {

            try {
                lastAction = event.getAction();
                touchSimulator.simulateTouch(pointerId, event.getAction(), x, y);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
