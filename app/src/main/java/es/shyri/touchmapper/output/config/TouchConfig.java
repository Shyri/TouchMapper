package es.shyri.touchmapper.output.config;

import java.util.List;

import es.shyri.touchmapper.output.touch.CircleMapping;
import es.shyri.touchmapper.output.touch.FPSJoystick;
import es.shyri.touchmapper.output.touch.TapMapping;
import es.shyri.touchmapper.output.touch.TriggeredJoystickMapping;

/**
 * Created by shyri on 17/11/2017.
 */

public class TouchConfig {
    public List<CircleMapping> circleMappings;
    public List<TapMapping> tapMappings;
    public List<TriggeredJoystickMapping> triggeredJoystickMappings;
    public List<FPSJoystick> fpsJoysticks;
}
