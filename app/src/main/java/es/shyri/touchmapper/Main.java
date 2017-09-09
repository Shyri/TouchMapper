package es.shyri.touchmapper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.net.ServerSocket;

import es.shyri.touchmapper.output.Server;

/**
 * Created by shyri on 06/09/17.
 */

public class Main {
    static Looper looper;
    static EventInput eventInput;
    private ServerSocket serverSocket;
    public static final int DEFAULT_PORT = 6543;

    Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.l("Handling message " + msg.obj);
        }
    };

    private Server server;

    public Main() {
        server = new Server(messageHandler);
        server.start();
    }

    public static void main(String[] args) {
        Looper.prepare();
        looper = Looper.myLooper();

        Main main = new Main();

        Looper.loop();
    }

}
