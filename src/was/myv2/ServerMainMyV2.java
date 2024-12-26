package was.myv2;

import java.io.IOException;

public class ServerMainMyV2 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        HttpServerMyV2 server = new HttpServerMyV2(PORT);
        server.start();
    }
}
