package es.shyri.touchmapper.output;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

import es.shyri.touchmapper.output.config.TouchConfig;
import es.shyri.touchmapper.output.touch.CircleMapping;
import es.shyri.touchmapper.output.touch.TapMapping;

import static android.view.KeyEvent.KEYCODE_BUTTON_A;

/**
 * Created by shyri on 08/09/17.
 */

public class TouchMapper {
    private final static String LOG_TAG = "TouchMapper";
    private final TouchSimulator touchSimulator;
    private final TouchConfig touchConfig;
    ArrayList<TapMapping> tapMappings = new ArrayList<>();
    ArrayList<CircleMapping> circleMappings = new ArrayList<>();

    public TouchMapper(TouchConfig touchConfig) throws Exception {
        this.touchConfig = touchConfig;

        touchSimulator = new TouchSimulator();
        circleMappings.add(new CircleMapping(167, 933, 110, touchSimulator));
        tapMappings.add(new TapMapping(KEYCODE_BUTTON_A, 1750, 950, touchSimulator));

        //        for (int i = 0; i < tapMappings.size(); i++) {
        //            touchSimulator.addPointer(i);
        //        }
        //        //
//        for (int i = 0; i < circleMappings.size(); i++) {
        //            touchSimulator.addPointer(i);
        //        }

        //        tapMappings.add(new TapMapping(0, 101, 1750, 950, touchSimulator));
        //        tapMappings.add(new TapMapping(2, 102, 1500, 400, touchSimulator));
    }

    public void processEvent(KeyEvent event) {
        Log.d(LOG_TAG, "Key Event received");
        for (TapMapping tapMapping : tapMappings) {
            tapMapping.processEvent(event);
        }
    }

    public void processEvent(MotionEvent event) {
        Log.d(LOG_TAG, "Motion Event received");
        for (CircleMapping circleMapping : circleMappings) {
            circleMapping.processEvent(event);
        }
    }
}
