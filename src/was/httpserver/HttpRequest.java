package was.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpRequest {
    private String method;
    private String path;
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader reader) throws IOException {
        parseRequestLine(reader);
        parseHeaders(reader);
        parseBody(reader);

    }

    /* "GET /search?q=hello HTTP/1.1" */

    private void parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new IOException("EOF: No request line received");
        }
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        method = parts[0];
        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0];

        if (pathParts.length > 1) {
            parseQueryParameters(pathParts[1]);
        }
    }
    /* q=hello */
    /* key1=value1&key2=value2 */
    /* q= */

    private void parseQueryParameters(String queryString) {
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            String key = URLDecoder.decode(keyValue[0], UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }
    /* "Host: localhost:12345" */
    /* "Connection: keep-alive" */
    /* "Cache-Control: max-age=0" */

    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(":");
            //trim() 앞뒤 공백 제거
            headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
    }
    private void parseBody(BufferedReader reader) throws IOException {
        if (!headers.containsKey("Content-Length")) {
            return;
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length")); //length 길이만 반환
        char[] bodyChars = new char[contentLength]; //length 길이만큼 char 배열 생성
        int read = reader.read(bodyChars); //BufferedReader는 내부적으로 스트림에서 데이터를 읽어와 bodyChars에 저장하고 읽은 문자수 반환
        if (read != contentLength) {
            throw new IOException("Fail to read entire body. Expected: " + contentLength + " bytes, but read: " + read);
        }
        String body = new String(bodyChars); //bodyChars배열을 String으로 생성
        log("HTTP Message Body: " + body);

        String contentType = headers.get("Content-Type");
        if (contentType.equals("application/x-www-form-urlencoded")) {
            //id=id1&name=name1&age=30
            parseQueryParameters(body);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String name) {
        return queryParameters.get(name);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                ", headers=" + headers +
                '}';
    }
}
