package mychat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            ChatSessionManager sessionManager = new ChatSessionManager();

            log("채팅 서버 시작");

            //shutdownHook 추가
            ChatServerShutdownHook shutdownHook = new ChatServerShutdownHook(serverSocket, sessionManager);
            Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook, "shutdownHook"));

            while (true) {
                Socket socket = serverSocket.accept();
                ChatSession session = new ChatSession(socket, sessionManager);
                sessionManager.add(session);
                Thread thread = new Thread(session);
                thread.start();
            }
        } catch (IOException e) {
            log("서버 소켓 종료: " + e);
        }

    }

    private static class ChatServerShutdownHook implements Runnable{

        private final ServerSocket serverSocket;
        private final ChatSessionManager sessionManager;

        public ChatServerShutdownHook(ServerSocket serverSocket, ChatSessionManager sessionManager) {
            this.serverSocket = serverSocket;
            this.sessionManager = sessionManager;
        }

        @Override
        public void run() {
            try {
                log("ShutdownHook 실행");
                sessionManager.closeAll();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("e = " + e);
            }
        }
    }
}
