package mychat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static network.tcp.SocketCloseUtil.closeAll;
import static util.MyLogger.log;

public class ChatSession implements Runnable {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final ChatSessionManager sessionManager;
    private String name;

    public ChatSession(Socket socket, ChatSessionManager sessionManager) throws IOException {
        this.socket = socket;
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
                name = input.readUTF(); //blocking
                log("유저 연결 성공: " + name);
                sessionManager.sendAll(name + " 님이 입장하셨습니다.");

                while (true) {
                    String cmd = input.readUTF(); //blocking

                    if (cmd.equals("message")) {
                        String message = input.readUTF(); //blocking
                        sessionManager.sendAll(name + " : " + message);
                    } else if (cmd.equals("change")) {
                        String newName = input.readUTF(); //blocking
                        sessionManager.sendAll(name + "님이 " + newName + "으로 이름을 변경하셨습니다.");
                        name = newName;
                    } else if (cmd.equals("users")) {
                        sessionManager.sendUsers();
                    }
                }

            }
        } catch (IOException e) {
            log(e);
        } finally {
            sessionManager.remove(this);
            close();
            log("연결 종료: " + socket);
        }
    }

    public void send(String message) throws IOException {
        output.writeUTF(message);
    }

    public String getName() {
        return name;
    }

    public void close() {
        closeAll(socket, input, output);
    }
}
