package es.shyri.touchmapper.input;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import es.shyri.touchmapper.Main;

/**
 * Created by shyri on 08/09/17.
 */

public class InputSender {
    private static final String TAG = "InputSender";
    private static final int MAX_EVENT_QUEUE = 300;
    private BlockingQueue<byte[]> blockingQueue = new LinkedBlockingQueue<>(MAX_EVENT_QUEUE);
    private Thread senderThread;

    void start() {
        senderThread = new Thread(new ClientThread());
        senderThread.start();
    }

    void sendKeyEvent(KeyEvent keyEvent) {
        Log.d(TAG, "Adding Key Event to Queue");
        blockingQueue.add(marshall(keyEvent));
    }

    void sendMotionEvent(MotionEvent motionEvent) {
        Log.d(TAG, "Adding Motion Event to Queue");
        blockingQueue.add(marshall(motionEvent));
    }

    byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle(); // not sure if needed or a good idea
        return bytes;
    }

    private class ClientThread implements Runnable {
        private Socket socket;

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getLocalHost();

                while (!Thread.currentThread()
                              .isInterrupted()) {
                    try {
                        Log.d(TAG, "Taking Event from queue");
                        byte[] str = blockingQueue.take();

                        socket = new Socket(serverAddr, Main.DEFAULT_PORT);

                        Log.d(TAG, "Sending Event");
                        OutputStream out = socket.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(out);
                        dos.writeInt(str.length);
                        dos.write(str);
                        dos.flush();
                        dos.close();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
