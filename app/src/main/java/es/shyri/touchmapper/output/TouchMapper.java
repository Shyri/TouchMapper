package es.shyri.touchmapper.output;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

import es.shyri.touchmapper.output.config.TouchConfig;
import es.shyri.touchmapper.output.touch.CircleMapping;
import es.shyri.touchmapper.output.touch.TapMapping;

import static android.view.KeyEvent.KEYCODE_BUTTON_A;
import static android.view.KeyEvent.KEYCODE_BUTTON_B;
import static android.view.KeyEvent.KEYCODE_BUTTON_X;

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
        tapMappings.add(new TapMapping(KEYCODE_BUTTON_A, 1800, 970, touchSimulator));
        tapMappings.add(new TapMapping(KEYCODE_BUTTON_B, 1812, 812, touchSimulator));
        tapMappings.add(new TapMapping(KEYCODE_BUTTON_X, 1652, 1011, touchSimulator));

        for (int i = 0; i < circleMappings.size(); i++) {
            circleMappings.get(i)
                          .setPointerId(i);
        }
        for (int i = 0; i < tapMappings.size(); i++) {
            tapMappings.get(i)
                       .setPointerId(i + circleMappings.size());
        }
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
