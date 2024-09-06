package thread_creation.example3;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
  private static final String inputFile = "src/thread_creation/example3/resources/war_and_peace.txt";
  private static final int numThreads = 2;

  public static void main(String[] args) throws IOException {
    String text = new String(Files.readAllBytes(Paths.get(inputFile)));

    startServer(text);
  }
  // 이 메서드는 HttpServer를 생성하고, /search 경로에 대한 요청을 처리하는 WordCountHandler를 등록한다.
  private static void startServer(String text) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/search", new WordCountHandler(text));
    Executor executor = Executors.newFixedThreadPool(numThreads);
    server.setExecutor(executor);
    server.start();
  }

  private record WordCountHandler(String text) implements HttpHandler {

    @Override
    public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
      // query 파라미터를 파싱한다.
      String query = exchange.getRequestURI().getQuery();
      String[] keyValue = query.split("=");

      String action = keyValue[0];
      String value = keyValue[1];
      // action이 word가 아니면 400 Bad Request를 반환한다.
      if (!action.equals("word")) {
        exchange.sendResponseHeaders(400, 0);
        exchange.close();
        return;
      }

      long count = countWord(value);
      byte[] response = Long.toString(count).getBytes();
      exchange.sendResponseHeaders(200, response.length);
      exchange.getResponseBody().write(response);
      exchange.close();
    }

    // text에서 word의 개수를 센다.
    private long countWord(String word) {
      long count = 0;
      int index = 0;
      // text에서 word를 찾아서 count를 증가시킨다.
      while (index >= 0) {
        index = text.indexOf(word, index);
        if (index >= 0) {
          count++;
          index++;
        }
      }

      return count;
    }
  }
}