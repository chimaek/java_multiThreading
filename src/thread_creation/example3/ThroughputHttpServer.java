package thread_creation.example3;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//
public class ThroughputHttpServer {
  private static final String inputFile = "resources/war_and_peace.txt";
  private static final int numThreads = 1;

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
      String query = exchange.getRequestURI().getQuery();
      String[] keyValue = query.split("=");
      String action = keyValue[0];
      String value = keyValue[1];

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

    private long countWord(String word) {
      long count = 0;
      int index = 0;

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