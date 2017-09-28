package es.shyri.touchmapper.output;

import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import es.shyri.touchmapper.EventInput;
import es.shyri.touchmapper.log.Log;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.TOOL_TYPE_FINGER;

/**
 * Created by shyri on 08/09/17.
 */

public class TouchSimulator {
    EventInput eventInput = new EventInput();
    MotionEvent.PointerCoords[] pointerCoordses;
    MotionEvent.PointerProperties[] pointerProperties;
    int pointers = 0;

    public TouchSimulator(int pointerCount) throws Exception {
        pointerCoordses = new MotionEvent.PointerCoords[pointerCount];
        pointerProperties = new MotionEvent.PointerProperties[pointerCount];

        pointerCoordses[0] = new MotionEvent.PointerCoords();
        pointerCoordses[1] = new MotionEvent.PointerCoords();
        pointerCoordses[2] = new MotionEvent.PointerCoords();

        pointerProperties[0] = new MotionEvent.PointerProperties();
        pointerProperties[0].id = 0;
        pointerProperties[1] = new MotionEvent.PointerProperties();
        pointerProperties[1].id = 1;
        pointerProperties[2] = new MotionEvent.PointerProperties();
        pointerProperties[2].id = 2;
    }

    public void simulateTouch(int id, int action, int x, int y) throws InvocationTargetException, IllegalAccessException {
        if (action == ACTION_DOWN) {
            pointers++;
            if (pointers > 1) {
                action = ACTION_POINTER_DOWN;
            }
        } else if (action == ACTION_UP) {
            pointers--;
            if (pointers > 0) {
                action = ACTION_POINTER_UP;
            }
        }

        Log.l("Simulating touch " + action + " in " + x + "," + y + " pointers: " + pointers);

        //        eventInput.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, action, SystemClock.uptimeMillis(),
        // pointers, x,
        //                                     y);
        pointerCoordses[id].x = x;
        pointerCoordses[id].y = y;

        pointerProperties[id].clear();
        pointerProperties[id].id = id;
        pointerProperties[id].toolType = TOOL_TYPE_FINGER;

        pointerCoordses[id].clear();
        pointerCoordses[id].x = x;
        pointerCoordses[id].y = y;
        pointerCoordses[id].pressure = 1.0f;

        MotionEvent.PointerCoords[] tmp = new MotionEvent.PointerCoords[1];
        MotionEvent.PointerProperties[] tmpp = new MotionEvent.PointerProperties[1];
        tmp[0] = pointerCoordses[id];
        tmpp[0] = pointerProperties[id];

        eventInput.injectTouch(action, tmpp, tmp);
    }
}
