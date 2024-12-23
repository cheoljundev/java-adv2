package mychat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatSessionManager {
    private final List<ChatSession> sessions = new ArrayList<>();

    public void add(ChatSession session) {
        sessions.add(session);
    }

    public void remove(ChatSession session) {
        sessions.remove(session);
    }

    public void sendAll(String message) throws IOException {
        for (ChatSession session : sessions) {
            session.send(message);
        }
    }

    public void sendUsers() throws IOException {
        sendAll("== 유저 목록 ==");
        for (ChatSession session : sessions) {
            sendAll(session.getName());
        }
    }

    public void closeAll() {
        for (ChatSession session : sessions) {
            session.close();
        }
        sessions.clear();
    }
}
