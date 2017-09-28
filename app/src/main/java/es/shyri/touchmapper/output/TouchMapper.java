package es.shyri.touchmapper.output;

import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

import es.shyri.touchmapper.output.touch.CircleMapping;
import es.shyri.touchmapper.output.touch.TapMapping;

/**
 * Created by shyri on 08/09/17.
 */

public class TouchMapper {
    private TouchSimulator touchSimulator = new TouchSimulator(3);
    ArrayList<TapMapping> tapMappings = new ArrayList<>();
    ArrayList<CircleMapping> circleMappings = new ArrayList<>();

    public TouchMapper() throws Exception {
        tapMappings.add(new TapMapping(0, 101, 1750, 950, touchSimulator));
        tapMappings.add(new TapMapping(2, 102, 1500, 400, touchSimulator));
        circleMappings.add(new CircleMapping(1, 167, 933, 110, touchSimulator));
    }

    public void processEvent(KeyEvent event) {
        for (TapMapping tapMapping : tapMappings) {
            tapMapping.processEvent(event);
        }
    }

    public void processEvent(MotionEvent event) {
        for (CircleMapping circleMapping : circleMappings) {
            circleMapping.processEvent(event);
        }
    }
}
