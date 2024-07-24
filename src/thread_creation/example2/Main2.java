package thread_creation.example2;

import java.math.BigInteger;

public class Main2 {
  public static void main(String[] args) {
    Thread thread =
        new Thread(new LongComputationTask(new BigInteger("2"), new BigInteger("1000000")));
    thread.setDaemon(true);
    thread.start();
    thread.interrupt();
  }

  private static class BlockingTask implements Runnable {
    @Override
    public void run() {
      try {
        Thread.sleep(500000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static class LongComputationTask implements Runnable {
    private final BigInteger base;
    private final BigInteger power;

    public LongComputationTask(BigInteger base, BigInteger power) {
      this.base = base;
      this.power = power;
    }

    @Override
    public void run() {
      System.out.println(base + "^" + power + " = " + pow(base, power));
    }

    private BigInteger pow(BigInteger base, BigInteger power) {
      BigInteger result = BigInteger.ONE;
      for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
//        if (Thread.currentThread().isInterrupted()) {
//          System.out.println("Prematurely interrupted computation");
//          return BigInteger.ZERO;
//        }
        result = result.multiply(base);
      }
      return result;
    }
  }
}