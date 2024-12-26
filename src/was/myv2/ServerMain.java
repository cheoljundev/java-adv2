package was.myv2;

import was.v1.HttpServerV1;

import java.io.IOException;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        HttpServerMyV2 server = new HttpServerMyV2(PORT);
        server.start();
    }
}
