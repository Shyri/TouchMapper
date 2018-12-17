package es.shyri.touchmapper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class ControllerTestActivity extends AppCompatActivity {

    public static void openActivity(Context context) {
        Intent i = new Intent(context, ControllerTestActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    TextView deviceNameTextView;
    TextView deviceIdTextView;
    TextView pressedKeyTextView;
    TextView axisXTextView;
    TextView axisXHatTextView;
    TextView axisZTextView;
    TextView axisYTextView;
    TextView axisYHatTextView;
    TextView axisRZTextView;
    TextView axisRTriggerTextView;
    TextView axisLTriggerTextView;
    TextView axisThrottleTextView;
    TextView axisBreakTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_test);
        deviceNameTextView = (TextView) findViewById(R.id.deviceNameTextView);
        deviceIdTextView = (TextView) findViewById(R.id.deviceIdTextView);
        pressedKeyTextView = (TextView) findViewById(R.id.pressedKeyTextView);
        axisXTextView = (TextView) findViewById(R.id.axisXTextView);
        axisXHatTextView = (TextView) findViewById(R.id.axisXHatTextView);
        axisZTextView = (TextView) findViewById(R.id.axisZTextView);
        axisYTextView = (TextView) findViewById(R.id.axisYTextView);
        axisYHatTextView = (TextView) findViewById(R.id.axisYHatTextView);
        axisRZTextView = (TextView) findViewById(R.id.axisRZTextView);
        axisRTriggerTextView = (TextView) findViewById(R.id.axisRTriggerTextView);
        axisLTriggerTextView = (TextView) findViewById(R.id.axisLTriggerTextView);
        axisThrottleTextView = (TextView) findViewById(R.id.axisThrottleTextView);
        axisBreakTextView = (TextView) findViewById(R.id.axisBreakTextView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        InputDevice mInputDevice = event.getDevice();

        deviceNameTextView.setText(mInputDevice.getName());
        deviceIdTextView.setText(mInputDevice.getDescriptor());

        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0 && event.getKeyCode() != KeyEvent.KEYCODE_DPAD_CENTER &&
                event.getKeyCode() != KeyEvent.KEYCODE_DEL && event.getKeyCode() != KeyEvent.KEYCODE_SPACE &&
                event.getKeyCode() != KeyEvent.KEYCODE_SPACE) {
                pressedKeyTextView.setText(getString(R.string.pressed_key, event.getKeyCode()));
            }
            if (handled) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final int historySize = event.getHistorySize();
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(event, i);
            }

            processJoystickInput(event, -1);
        }
        return super.onGenericMotionEvent(event);
    }

    private void processJoystickInput(MotionEvent event, int historyPos) {
        InputDevice mInputDevice = event.getDevice();
        deviceIdTextView.setText(mInputDevice.getDescriptor());
        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float axis_x = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_X, historyPos);
        axisXTextView.setText(getString(R.string.axis_x, axis_x));
        float axis_hat_x = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_HAT_X, historyPos);
        axisXHatTextView.setText(getString(R.string.axis_hat_x, axis_hat_x));
        float axis_z = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Z, historyPos);
        axisZTextView.setText(getString(R.string.axis_z, axis_z));

        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float axis_y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Y, historyPos);
        axisYTextView.setText(getString(R.string.axis_y, axis_y));
        float axis_hat_y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_HAT_Y, historyPos);
        axisYHatTextView.setText(getString(R.string.axis_hat_y, axis_hat_y));
        float axis_rz = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_RZ, historyPos);
        axisRZTextView.setText(getString(R.string.axis_rz, axis_rz));

        float axis_rtrigger = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_RTRIGGER, historyPos);
        axisRTriggerTextView.setText(getString(R.string.axis_rtrigger, axis_rtrigger));
        float axis_ltrigger = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_LTRIGGER, historyPos);
        axisLTriggerTextView.setText(getString(R.string.axis_ltrigger, axis_ltrigger));
        float axis_throttle = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_THROTTLE, historyPos);
        axisThrottleTextView.setText(getString(R.string.axis_throttle, axis_throttle));
        float axis_brake = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_BRAKE, historyPos);
        axisBreakTextView.setText(getString(R.string.axis_brake, axis_brake));

    }

    private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value = historyPos < 0 ? event.getAxisValue(axis) : event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

}
