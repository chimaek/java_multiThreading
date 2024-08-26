package thread_creation.example3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main1 {
  public static final String SOURCE_FILE = "src/thread_creation/example3/resources/many-flowers.jpg";
  public static final String DESTINATION_FILE = "src/thread_creation/example3/out/many-flowers.jpg";

  public static void main(String[] args) throws IOException {
    long startTime = System.currentTimeMillis();

    BufferedImage read = ImageIO.read(new File(SOURCE_FILE));
    BufferedImage write =
        new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_RGB);

    recolorSingleThreaded(read, write);
    File outputFile = new File(DESTINATION_FILE);
    ImageIO.write(write, "jpg", outputFile);

    long endTime = System.currentTimeMillis();

    System.out.println("Image processing took " + (endTime - startTime) + " milliseconds");
  }

  public static void recolorSingleThreaded(BufferedImage read, BufferedImage write) {
    recolorImage(read, write, 0, 0, read.getWidth(), read.getHeight());
  }

  public static void recolorImage(
      BufferedImage read,
      BufferedImage write,
      int leftCorner,
      int topCorner,
      int width,
      int height) {
    for (int x = leftCorner; x < leftCorner + width && x < read.getWidth(); x++) {
      for (int y = topCorner; y < topCorner + height && y < read.getHeight(); y++) {
        recolorPixel(read, write, x, y);
      }
    }
  }

  public static void recolorPixel(BufferedImage read, BufferedImage write, int x, int y) {
    int rgb = read.getRGB(x, y);

    int red = getRed(rgb);
    int green = getGreen(rgb);
    int blue = getBlue(rgb);

    int newRed;
    int newGreen;
    int newBlue;

    if (isSharedOfGray(red, green, blue)) {
      newRed = Math.min(255, red + 10);
      newGreen = Math.max(0, green - 80);
      newBlue = Math.max(0, blue - 20);
    } else {
      newRed = red;
      newGreen = green;
      newBlue = blue;
    }

    int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
    write.getRaster().setDataElements(x, y, write.getColorModel().getDataElements(newRGB, null));
  }

  public static int getBlue(int rgb) {
    return rgb & 0x000000FF;
  }

  public static int getGreen(int rgb) {
    return (rgb & 0x0000FF00) >> 8;
  }

  public static int getRed(int rgb) {
    return (rgb & 0x00FF0000) >> 16;
  }

  public static int createRGBFromColors(int red, int green, int blue) {
    int rgb = 0;
    rgb |= blue;
    rgb |= green << 8;
    rgb |= red << 16;
    rgb |= 0xFF000000;
    return rgb;
  }

  public static boolean isSharedOfGray(int red, int green, int blue) {
    return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
  }
}