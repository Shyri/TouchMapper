package es.shyri.touchmapper.output;

import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import es.shyri.touchmapper.EventInput;
import es.shyri.touchmapper.log.Log;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_INDEX_MASK;
import static android.view.MotionEvent.ACTION_POINTER_INDEX_SHIFT;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.TOOL_TYPE_FINGER;

/**
 * Created by shyri on 08/09/17.
 */

public class TouchSimulator {
    EventInput eventInput = new EventInput();

    ArrayList<MotionEvent.PointerCoords> pointerCoordses;
    ArrayList<MotionEvent.PointerProperties> pointerPropertieses;

    int pointers = 0;

    public TouchSimulator() throws Exception {
        pointerCoordses = new ArrayList<>();
        pointerPropertieses = new ArrayList<>();
    }

    private int addPointer(int mapId) {

        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();

        pointerProperties.id = mapId;

        pointerCoordses.add(pointerCoords);
        pointerPropertieses.add(pointerProperties);

        return pointerCoordses.size() - 1;
    }

    private void removePointer(int pointerIndex) {
        pointerCoordses.remove(pointerIndex);
        pointerPropertieses.remove(pointerIndex);
    }

    public void simulateTouch(int mapId, int action, int x, int y) throws InvocationTargetException, IllegalAccessException {
        int pointerIndex;

        if (action == ACTION_DOWN) {
            pointers++;
            pointerIndex = addPointer(mapId);
            if (pointers > 1) {
                //                action = ACTION_POINTER_DOWN;
                action = getUnmaskedAction(ACTION_POINTER_DOWN, pointerIndex);
            }
        } else if (action == ACTION_UP) {
            pointers--;
            pointerIndex = getPointerIndex(mapId);
            if (pointers > 0) {
                //                action = ACTION_POINTER_UP;
                action = getUnmaskedAction(ACTION_POINTER_UP, pointerIndex);
            }
        } else {
            pointerIndex = getPointerIndex(mapId);
        }

        Log.l("Simulating touch " + action + " in " + x + "," + y + " pointers: " + pointers);

        //        eventInput.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, action, SystemClock.uptimeMillis(),
        // pointers, x,
        //                                     y);
        MotionEvent.PointerCoords pointerCoords = pointerCoordses.get(pointerIndex);
        MotionEvent.PointerProperties pointerProperties = pointerPropertieses.get(pointerIndex);

        pointerCoords.x = x;
        pointerCoords.y = y;

        pointerProperties.clear();
        pointerProperties.id = mapId;
        pointerProperties.toolType = TOOL_TYPE_FINGER;

        pointerCoords.clear();
        pointerCoords.x = x;
        pointerCoords.y = y;
        pointerCoords.pressure = 1.0f;

        eventInput.injectTouch(action,
                               pointerPropertieses.toArray(new MotionEvent.PointerProperties[pointerPropertieses.size()]),
                               pointerCoordses.toArray(new MotionEvent.PointerCoords[pointerCoordses.size()]));

        if (action == ACTION_UP || action == ACTION_POINTER_UP) {
            removePointer(pointerIndex);
        }
    }

    private int getPointerIndex(int pointerId) {
        int pointerIndex = -1;

        for (MotionEvent.PointerProperties pointerProperties : pointerPropertieses) {
            pointerIndex++;
            if (pointerProperties.id == pointerId) {
                break;
            }
        }

        return pointerIndex;
    }

    private int getUnmaskedAction(int action, int pointerIndex) {
        return action | ((pointerIndex << ACTION_POINTER_INDEX_SHIFT) & ACTION_POINTER_INDEX_MASK);
    }
}
