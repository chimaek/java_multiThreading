package thread_creation.example5;

public class Main3 {

  public static void main(String[] args) {
    SharedClass sharedClass = new SharedClass();
    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < Integer.MAX_VALUE; i++) {
        sharedClass.increment();
      }
    });

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < Integer.MAX_VALUE; i++) {
        sharedClass.checkForDataRace();
      }
    });

    thread1.start();
    thread2.start();
  }

  public static class SharedClass {
    // volatile을 쓰는 이유는 메모리 가시성을 보장하기 위함

    private volatile int x = 0;
    private volatile int y = 0;

    public void increment() {
      x++;
      y++;
    }

    public void checkForDataRace() {
      if (y > x) {
        System.out.println("y > x - Data Race is detected");
      }
    }
  }
}