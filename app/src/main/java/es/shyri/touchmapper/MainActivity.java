package es.shyri.touchmapper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);
        //        Intent intent = new Intent(this, MainService.class);
        //        startService(intent);
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

        ArrayList gameControllerDeviceIds = new ArrayList();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            Log.d("Dev", "Device: " + dev.getName() + " vib: " + dev.getVibrator()
                                                                    .hasVibrator());
            //            dev.getVibrator()
            //               .vib(1500);

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }

        final EventInput eventInput;
        try {
            eventInput = new EventInput();
            final int[] devices = eventInput.getInputDevices();

            for (int i = 0; i < devices.length; i++) {
                //                InputDevice device = eventInput.getInputDevice(devices[i]);
                //                Log.d("Devices",
                //                      device.getName() + "|" + device.getDescriptor() + "|" + (device.getSources() &
                // SOURCE_CLASS_JOYSTICK) +
                //                      "|" + (device.getSources() & SOURCE_GAMEPAD) + "|" + (device.getSources() &
                // SOURCE_DPAD) + "|" +
                //                      (device.getSources() & SOURCE_MOUSE));
                //                if(device.getDescriptor().equals("a6fffa631e21755e40b7c95de1f9cbae3f59a0b2")) {
                //                    eventInput.register(new InputManager.InputDeviceListener() {
                //                        @Override
                //                        public void onInputDeviceAdded(int deviceId) {
                //
                //                        }
                //
                //                        @Override
                //                        public void onInputDeviceRemoved(int deviceId) {
                //
                //                        }
                //
                //                        @Override
                //                        public void onInputDeviceChanged(int deviceId) {
                //                            InputDevice device1 = eventInput.getInputDevice(deviceId);
                //                            device1.get
                //                        }
                //                    });
                //                }
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imeManager != null) {
                imeManager.showInputMethodPicker();
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG)
                     .show();
            }
            //            imm.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //        try {
        //            IBinder binder = (IBinder) Class.forName("android.os.ServiceManager")
        //                                            .getDeclaredMethod("getService", String.class)
        //                                            .invoke(null, "activity");
        //            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //            Object objIActMag, objActMagNative;
        //            Class clzIActMag = Class.forName("android.app.IActivityManager");
        //            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
        //            Method getDefault = clzActMagNative.getDeclaredMethod("getDefault");
        //            // IActivityManager iActMag = ActivityManagerNative.getDefault();
        //            objIActMag = getDefault.invoke(clzActMagNative);
        //            Method getConfiguration = clzIActMag.getMethod("getServices", int.class, int.class);
        //            List<ActivityManager.RunningServiceInfo> list =
        //                    (List<ActivityManager.RunningServiceInfo>) getConfiguration.invoke(objIActMag, Integer
        // .MAX_VALUE, 0);
        //            for (ActivityManager.RunningServiceInfo service : list) {
        //                Log.d("Services", service.service.getClassName());
        //            }
        //        } catch (NoSuchMethodException e) {
        //            e.printStackTrace();
        //        } catch (ClassNotFoundException e) {
        //            e.printStackTrace();
        //        } catch (InvocationTargetException e) {
        //            e.printStackTrace();
        //        } catch (IllegalAccessException e) {
        //            e.printStackTrace();
        //        }
        InputDevice mInputDevice = event.getDevice();

        deviceNameTextView.setText(mInputDevice.getName());
        deviceIdTextView.setText(mInputDevice.getDescriptor());

        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                pressedKeyTextView.setText(getString(R.string.pressed_key, "" + keyCode));
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
