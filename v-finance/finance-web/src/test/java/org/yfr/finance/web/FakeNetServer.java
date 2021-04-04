package org.yfr.finance.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FakeNetServer {

    public static final int LISTEN_PORT = 1234;

    public FakeNetServer() {
        ServerSocket serverSocket = null;
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(LISTEN_PORT);
            System.out.println("Server listening requests...");
            while (true) {
                Socket socket = serverSocket.accept();
                threadExecutor.execute(new RequestThread(socket));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            threadExecutor.shutdown();

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new FakeNetServer();
    }

    private class RequestThread implements Runnable {

        private Socket clientSocket;

        public RequestThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            System.out.printf("有%s連線進來!\n", clientSocket.getRemoteSocketAddress());
            BufferedReader input = null;
            DataOutputStream output = null;

            try {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new DataOutputStream(clientSocket.getOutputStream());

                String line = null;
                while ((line = input.readLine()) != null) {
                    switch (line) {
                        case "DUMP_MIN_K":
                            output.write(("Server Receive " + line + "\n").getBytes());
                            output.flush();
                            break;
                        default:
                            output.write(("Unsupport Command " + line + "\n").getBytes());
                            output.flush();
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (output != null) {
                        output.close();
                    }
                    if (this.clientSocket != null && !this.clientSocket.isClosed()) {
                        this.clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.printf("%s連線斷線!\n", clientSocket.getRemoteSocketAddress());
            }
        }
    }

}
