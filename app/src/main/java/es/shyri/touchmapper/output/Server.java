package es.shyri.touchmapper.output;

import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.shyri.touchmapper.log.Log;
import es.shyri.touchmapper.Main;

/**
 * Created by shyri on 08/09/17.
 */

public class Server {
    private ServerSocket serverSocket;
    private Handler handler;
    private Thread serverThread;

    public Server(Handler handler) {
        this.handler = handler;
    }

    public void start() {
        serverThread = new Thread(new ServerThread());
        serverThread.start();
    }

    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
            Log.l("Starting server");
            try {
                serverSocket = new ServerSocket(Main.DEFAULT_PORT);

                while (!Thread.currentThread()
                              .isInterrupted()) {
                    socket = serverSocket.accept();
                    InputReceiver inputReceiver = new InputReceiver(socket, handler);
                    inputReceiver.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
