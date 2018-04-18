package es.shyri.touchmapper.output.touch;

import es.shyri.touchmapper.output.TouchSimulator;

/**
 * Created by shyri on 23/12/2017.
 */

public abstract class TouchMapping {
    protected transient TouchSimulator touchSimulator;
    protected transient int pointerId;
    protected transient String deviceDescriptor;

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public void setTouchSimulator(TouchSimulator touchSimulator) {
        this.touchSimulator = touchSimulator;
    }
}
