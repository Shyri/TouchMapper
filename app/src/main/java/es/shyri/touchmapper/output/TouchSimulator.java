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

    public void simulateTouch(int mapId,
                              int originalAction,
                              int x,
                              int y) throws InvocationTargetException, IllegalAccessException {
        int pointerIndex;
        int unmaskedAction = originalAction;

        if (originalAction == ACTION_DOWN) {
            pointers++;
            pointerIndex = addPointer(mapId);
            if (pointers > 1) {
                //                action = ACTION_POINTER_DOWN;
                unmaskedAction = getUnmaskedAction(ACTION_POINTER_DOWN, pointerIndex);
            }
        } else if (originalAction == ACTION_UP) {
            if (pointers > 0) {
                pointers--;
                pointerIndex = getPointerIndex(mapId);
                if (pointers > 0) {
                    //                action = ACTION_POINTER_UP;
                    unmaskedAction = getUnmaskedAction(ACTION_POINTER_UP, pointerIndex);
                }
            } else {
                // No pointer to handle
                return;
            }
        } else {
            pointerIndex = getPointerIndex(mapId);
        }

        Log.l("Simulating touch " + originalAction + " in " + x + "," + y + " pointers: " + pointers);

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

        eventInput.injectTouch(unmaskedAction,
                               pointerPropertieses.toArray(new MotionEvent.PointerProperties[pointerPropertieses.size()]),
                               pointerCoordses.toArray(new MotionEvent.PointerCoords[pointerCoordses.size()]));

        if (originalAction == ACTION_UP) {
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
