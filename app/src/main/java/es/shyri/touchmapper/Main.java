package es.shyri.touchmapper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ServerSocket;

import es.shyri.touchmapper.output.Server;
import es.shyri.touchmapper.output.TouchMapper;
import es.shyri.touchmapper.output.config.ConfigParser;
import es.shyri.touchmapper.output.config.TouchConfig;

import static es.shyri.touchmapper.EventInput.SOURCE_KEY;
import static es.shyri.touchmapper.EventInput.SOURCE_MOVEMENT;

/**
 * Created by shyri on 06/09/17.
 */

public class Main {
    static Looper looper;
    static EventInput eventInput;
    private ServerSocket serverSocket;
    private TouchMapper touchMapper;

    public static final int DEFAULT_PORT = 6543;

    Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SOURCE_MOVEMENT:
                    touchMapper.processEvent((MotionEvent) msg.obj);
                    break;
                case SOURCE_KEY:
                    touchMapper.processEvent((KeyEvent) msg.obj);
                    break;
            }
        }
    };

    private Server server;

    public Main() {

        TouchConfig touchConfig = null;
        try {
            touchConfig = readFile("/storage/self/primary/Android/data/es.shyri.touchmapper/files/mapping.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            touchMapper = new TouchMapper(touchConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        server = new Server(messageHandler);
        server.start();
    }

    public static void main(String[] args) {
        Looper.prepare();
        looper = Looper.myLooper();

        Main main = new Main();

        Looper.loop();
    }

    private TouchConfig readFile(String fileName) throws FileNotFoundException {
        ConfigParser configParser = new ConfigParser();
        return configParser.parseConfig(new File(fileName));
    }
}
