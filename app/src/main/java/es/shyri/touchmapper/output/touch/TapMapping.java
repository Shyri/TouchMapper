package es.shyri.touchmapper.output.touch;

import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;

import es.shyri.touchmapper.log.Log;
import es.shyri.touchmapper.output.TouchSimulator;

import static android.view.KeyEvent.ACTION_UP;

/**
 * Created by shyri on 09/09/17.
 */

public class TapMapping {
    private int id;
    private final TouchSimulator touchSimulator;
    private int keyCode;
    private int x;
    private int y;
    private int lastAction = ACTION_UP;

    public TapMapping(int id, int keyCode, int x, int y, TouchSimulator touchSimulator) {
        this.id = id;
        this.touchSimulator = touchSimulator;
        this.keyCode = keyCode;
        this.x = x;
        this.y = y;
    }

    public void processEvent(KeyEvent event) {
        Log.l("Processing Event" + event.getScanCode());

        if (event.getKeyCode() == keyCode && lastAction != event.getAction()) {
            try {
                lastAction = event.getAction();
                touchSimulator.simulateTouch(id, event.getAction(), x, y);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
