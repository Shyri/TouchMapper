package es.shyri.touchmapper.output;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import es.shyri.touchmapper.output.config.TouchConfig;
import es.shyri.touchmapper.output.touch.CircleMapping;
import es.shyri.touchmapper.output.touch.FPSJoystick;
import es.shyri.touchmapper.output.touch.TapMapping;
import es.shyri.touchmapper.output.touch.TriggeredJoystickMapping;

/**
 * Created by shyri on 08/09/17.
 */

public class TouchMapper {
    private final static String LOG_TAG = "TouchMapper";
    private final TouchSimulator touchSimulator;
    private final TouchConfig touchConfig;
    //    ArrayList<TapMapping> tapMappings = new ArrayList<>();
    //    ArrayList<CircleMapping> circleMappings = new ArrayList<>();

    public TouchMapper(TouchConfig touchConfig) throws Exception {
        this.touchConfig = touchConfig;

        touchSimulator = new TouchSimulator();
        //        circleMappings.add(new CircleMapping(167, 933, 110, touchSimulator));
        //        tapMappings.add(new TapMapping(KEYCODE_BUTTON_A, 1800, 970, touchSimulator));
        //        tapMappings.add(new TapMapping(KEYCODE_BUTTON_B, 1812, 812, touchSimulator));
        //        tapMappings.add(new TapMapping(KEYCODE_BUTTON_X, 1652, 1011, touchSimulator));

        //        for (int i = 0; i < circleMappings.size(); i++) {
        //            circleMappings.get(i)
        //                          .setPointerId(i);
        //        }
        //        for (int i = 0; i < tapMappings.size(); i++) {
        //            tapMappings.get(i)
        //                       .setPointerId(i + circleMappings.size());
        //        }
        for (int i = 0; i < touchConfig.circleMappings.size(); i++) {
            touchConfig.circleMappings.get(i)
                                      .setPointerId(i);
            touchConfig.circleMappings.get(i)
                                      .setTouchSimulator(touchSimulator);
        }
        for (int i = 0; i < touchConfig.tapMappings.size(); i++) {
            touchConfig.tapMappings.get(i)
                                   .setPointerId(i + touchConfig.circleMappings.size());

            touchConfig.tapMappings.get(i)
                                   .setTouchSimulator(touchSimulator);
        }
        for (int i = 0; i < touchConfig.triggeredJoystickMappings.size(); i++) {
            touchConfig.triggeredJoystickMappings.get(i)
                                                 .setPointerId(i + touchConfig.circleMappings.size() +
                                                               touchConfig.tapMappings.size());

            touchConfig.triggeredJoystickMappings.get(i)
                                                 .setTouchSimulator(touchSimulator);
        }
        for (int i = 0; i < touchConfig.fpsJoysticks.size(); i++) {
            touchConfig.fpsJoysticks.get(i)
                                    .setPointerId(i + touchConfig.circleMappings.size() + touchConfig.tapMappings.size() +
                                                  touchConfig.triggeredJoystickMappings.size());

            touchConfig.fpsJoysticks.get(i)
                                    .setTouchSimulator(touchSimulator);
        }
    }

    public void processEvent(KeyEvent event) {
        Log.d(LOG_TAG, "Key Event received");
        for (TapMapping tapMapping : touchConfig.tapMappings) {
            tapMapping.processEvent(event);
        }

        for (TriggeredJoystickMapping triggeredJoystickMapping : touchConfig.triggeredJoystickMappings) {
            triggeredJoystickMapping.processEvent(event);
        }
    }

    public void processEvent(MotionEvent event) {
        Log.d(LOG_TAG, "Motion Event received");
        for (CircleMapping circleMapping : touchConfig.circleMappings) {
            circleMapping.processEvent(event);
        }

        for (TriggeredJoystickMapping triggeredJoystickMapping : touchConfig.triggeredJoystickMappings) {
            triggeredJoystickMapping.processEvent(event);
        }

        for (FPSJoystick fpsJoystick : touchConfig.fpsJoysticks) {
            fpsJoystick.processEvent(event);
        }
    }
}
