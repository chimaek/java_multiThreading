package thread_creation.example4;

public class Main2 {

  public static void main(String[] args) throws InterruptedException {
    InventoryCounter inventoryCounter = new InventoryCounter();
    Incrementing incrementing = new Incrementing(inventoryCounter);
    Decrementing decrementing = new Decrementing(inventoryCounter);

    incrementing.start();
    decrementing.start();

    incrementing.join();
    decrementing.join();

    System.out.println(
        "The number of items in the inventory is " + inventoryCounter.getItems() + " items");
    ;
  }

  public static class Incrementing extends Thread {
    private InventoryCounter inventoryCounter;

    public Incrementing(InventoryCounter inventoryCounter) {
      this.inventoryCounter = inventoryCounter;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10000; i++) {
        inventoryCounter.increment();
      }
    }
  }

  private static class InventoryCounter {
    private int items = 0;

    public synchronized void increment() {
      items++;
    }

    public synchronized void decrement() {
      items--;
    }

    public synchronized int getItems() {
      return items;
    }
  }

  public static class Decrementing extends Thread {
    private InventoryCounter inventoryCounter;

    public Decrementing(InventoryCounter inventoryCounter) {
      this.inventoryCounter = inventoryCounter;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10000; i++) {
        inventoryCounter.decrement();
      }
    }
  }
}