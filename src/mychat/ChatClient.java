package mychat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static util.MyLogger.log;

public class ChatClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String cmd = sc.nextLine();

            if (cmd.startsWith("/join| ")) {
                try (Socket socket = new Socket("localhost", 12345);
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    log("서버에 연결되었습니다.");
                    output.writeUTF(cmd.split(" ")[1]);

                    Runnable serverRunnable = new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    log(input.readUTF());
                                } catch (IOException e) {
                                    break;
                                }
                            }
                        }
                    };

                    Thread serverReadThread = new Thread(serverRunnable, "server");
                    serverReadThread.start();

                    while (isRunning) {
                        cmd = sc.nextLine();
                        if (cmd.startsWith("/message| ")) {
                            output.writeUTF("message");
                            output.writeUTF(cmd.split(" ")[1]);
                        } else if (cmd.startsWith("/change| ")) {
                            output.writeUTF("change");
                            output.writeUTF(cmd.split(" ")[1]);
                        } else if (cmd.equals("/users")) {
                            output.writeUTF("users");
                        } else if (cmd.equals("/exit")) {
                            isRunning = false;
                        }
                    }

                } catch (IOException e) {
                    log("서버에 연결하지 못했습니다.");
                    throw new RuntimeException(e);
                }
            } else {
                log("잘못된 입력입니다.");
            }
        }


    }

}
