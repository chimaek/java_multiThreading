package thread_creation.example4;

public class Main1 {
  public static void main(String[] args) {
    int x = 1;
    int y = 2;
    int result = sum(x, y);
  }

  private static int sum(int a, int b) {
    return a + b;
  }
}