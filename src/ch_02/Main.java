package ch_02;

public class Main {

  public static void main(String[] args) {
    Thread thread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                System.out.println("스레드를 실행합니다." + Thread.currentThread().getName());
                System.out.println("우선순위 : " + Thread.currentThread().getPriority());
              }
            });
    thread.setPriority(Thread.MAX_PRIORITY); // 우선순위를 높게 설정
    thread.setName("새로운 스레드");
    System.out.println("현재 스레드는" + Thread.currentThread().getName() + "입니다.");
    thread.start();
    System.out.println("현재 스레드는" + Thread.currentThread().getName() + "입니다.");
  }
}