package es.shyri.touchmapper;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.support.v4.view.InputDeviceCompat;
import android.view.InputEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shyri on 05/09/17.
 */

public class EventInput {
    public final static int SOURCE_KEY = 2;
    public final static int SOURCE_MOVEMENT = 1;

    Method injectInputEventMethod;
    InputManager im;

    public EventInput() throws Exception {
        //Get the instance of InputManager class using reflection
        String methodName = "getInstance";
        Object[] objArr = new Object[0];
        im = (InputManager) InputManager.class.getDeclaredMethod(methodName, new Class[0])
                                              .invoke(null, objArr);

        //Make MotionEvent.obtain() method accessible
        methodName = "obtain";
        MotionEvent.class.getDeclaredMethod(methodName, new Class[0])
                         .setAccessible(true);

        //Get the reference to injectInputEvent method
        methodName = "injectInputEvent";
        injectInputEventMethod = InputManager.class.getMethod(methodName, new Class[] {InputEvent.class, Integer.TYPE});
    }

    public void injectTouch(int action,
                            MotionEvent.PointerProperties[] pointerProperties,
                            MotionEvent.PointerCoords[] pointerCoords) throws InvocationTargetException,
            IllegalAccessException {
        long when = SystemClock.uptimeMillis();
        MotionEvent event =
                MotionEvent.obtain(when, when, action, pointerProperties.length, pointerProperties, pointerCoords, 0, 0,
                                   1.0f, 1.0f, 0, 0, InputDeviceCompat.SOURCE_TOUCHSCREEN, 0);
        event.setSource(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        injectInputEventMethod.invoke(im, new Object[] {event, Integer.valueOf(2)});
    }

    public int[] getInputDevices() {
        return im.getInputDeviceIds();
    }
}
