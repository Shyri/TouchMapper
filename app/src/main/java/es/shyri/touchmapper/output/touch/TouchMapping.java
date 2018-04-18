package es.shyri.touchmapper.output.touch;

/**
 * Created by shyri on 23/12/2017.
 */

public abstract class TouchMapping {
    protected int pointerId;
    protected String deviceDescriptor;

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }
}
