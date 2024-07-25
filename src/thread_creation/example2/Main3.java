package thread_creation.example2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main3 {
  public static void main(String[] args) throws InterruptedException {

    List<Long> inputNumbers =
        List.of(0L, 3435L, 35435L, 2324L, 4656L, 23L, 2435L, 5566L, 7324L, 3999L);

    List<FactorialThread> threads = new ArrayList<>();
    for (long inputNumber : inputNumbers) {
      threads.add(new FactorialThread(inputNumber));
    }

    for (Thread thread : threads) {
      thread.start();
    }
    for (Thread thread : threads) {
      thread.join();
    }

    for (int i = 0; i < inputNumbers.size(); i++) {
      FactorialThread thread = threads.get(i);
      if (thread.isFinished()) {
        System.out.println(
            "Factorial of " + inputNumbers.get(i) + " is " + thread.getFactorialResult());
      } else {
        System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
      }
    }
  }

  public static class FactorialThread extends Thread {
    private final long inputNumber;
    private BigInteger factorialResult = BigInteger.ZERO;
    private boolean isFinished = false;

    public FactorialThread(long inputNumber) {
      this.inputNumber = inputNumber;
    }

    @Override
    public void run() {
      this.factorialResult = factorial(inputNumber);
      this.isFinished = true;
    }

    public BigInteger factorial(long n) {
      BigInteger result = BigInteger.ONE;
      for (long i = n; i > 0; i--) {
        result = result.multiply(new BigInteger(Long.toString(i)));
      }
      return result;
    }

    public boolean isFinished() {
      return isFinished;
    }

    public BigInteger getFactorialResult() {
      return factorialResult;
    }
  }
}