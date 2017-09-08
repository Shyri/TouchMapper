package es.shyri.touchmapper.input;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shyri on 08/09/17.
 */

public class InputSender {
    private static final int MAX_EVENT_QUEUE = 300;
    private static final int DEFAULT_PORT = 6543;
    private BlockingQueue<byte[]> blockingQueue = new LinkedBlockingQueue<>(MAX_EVENT_QUEUE);

    void start() {
        new Thread(new ClientThread()).start();
    }

    void sendKeyEvent(KeyEvent keyEvent) {
        blockingQueue.add(marshall(keyEvent));
    }

    void sendMotionEvent(MotionEvent motionEvent) {
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

                socket = new Socket(serverAddr, DEFAULT_PORT);

                while (!Thread.currentThread()
                              .isInterrupted()) {
                    try {
                        byte[] str = blockingQueue.take();

                        OutputStream out = socket.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(out);
                        dos.write(str);
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
