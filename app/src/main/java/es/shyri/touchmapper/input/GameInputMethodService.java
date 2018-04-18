package es.shyri.touchmapper.input;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shyri on 06/09/17.
 */

public class GameInputMethodService extends InputMethodService {
    private String currentDevice = "1436698747f9cdebcff7fbaad0e5441d8c3858b7";
    private InputSender inputSender;

    @Override
    public void onCreate() {
        super.onCreate();
        inputSender = new InputSender();
        inputSender.start();
    }

    @Override
    public View onCreateInputView() {
        return super.onCreateInputView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("InputMethod", "Key Down " + event.getDevice()
                                                .getDescriptor());
        if (event.getDevice()
                 .getDescriptor()
                 .equals(currentDevice)) {
            inputSender.sendKeyEvent(event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getDevice()
                 .getDescriptor()
                 .equals(currentDevice)) {
            inputSender.sendKeyEvent(event);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getDevice()
                 .getDescriptor()
                 .equals(currentDevice)) {
            inputSender.sendMotionEvent(event);
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

}
