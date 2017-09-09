package es.shyri.touchmapper.output;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import es.shyri.touchmapper.Log;

/**
 * Created by shyri on 08/09/17.
 */

public class InputReceiver {
    private Socket socket;
    private Handler handler;
    private Thread communicationThread;

    InputReceiver(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    void start() {
        communicationThread = new Thread(new CommunicationThread(socket));
        communicationThread.start();
    }

    class CommunicationThread implements Runnable {
        private Socket clientSocket;
        private DataInputStream input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;

            try {
                Log.l("Starting comm Thread");
                this.input = new DataInputStream(this.clientSocket.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                int len = input.readInt();
                byte[] data = new byte[len];
                if (len > 0) {
                    input.readFully(data);
                }

                Parcel parcel = unmarshall(data);

                Message message = new Message();

                int type = parcel.readInt();
                parcel.setDataPosition(0);

                if (type == 1) {
                    MotionEvent event = MotionEvent.CREATOR.createFromParcel(parcel);

                    message.what = 1;
                    message.obj = event;
                    Log.l("Motion Event Received");
                } else if (type == 2) {
                    KeyEvent event = KeyEvent.CREATOR.createFromParcel(parcel);

                    message.what = 2;
                    message.obj = event;
                    Log.l("Key Event Received");
                } else {
                    Log.l("Message not recognized" + message.obj);
                }

                handler.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // this is extremely important!
        return parcel;
    }
}
