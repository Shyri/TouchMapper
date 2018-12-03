package es.shyri.touchmapper.input;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;

import es.shyri.touchmapper.output.config.ConfigParser;
import es.shyri.touchmapper.output.config.TouchConfig;

/**
 * Created by shyri on 06/09/17.
 */

public class GameInputMethodService extends InputMethodService {
    private TouchConfig touchConfig;
    private InputSender inputSender;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            touchConfig = readFile("/storage/self/primary/Android/data/es.shyri.touchmapper/files/mapping.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                 .equals(touchConfig.deviceId)) {
            inputSender.sendKeyEvent(event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getDevice()
                 .getDescriptor()
                 .equals(touchConfig.deviceId)) {
            inputSender.sendKeyEvent(event);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getDevice()
                 .getDescriptor()
                 .equals(touchConfig.deviceId)) {
            inputSender.sendMotionEvent(event);
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    private TouchConfig readFile(String fileName) throws FileNotFoundException {
        ConfigParser configParser = new ConfigParser();
        return configParser.parseConfig(new File(fileName));
    }
}
