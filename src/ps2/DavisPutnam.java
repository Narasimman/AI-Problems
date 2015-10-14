package ps2;

import java.io.IOException;

public class DavisPutnam {

  public static void main(String[] args) {
    FileHandler f = new FileHandler("dp-input");
    try {
      f.read();
    } catch (IOException e) {
      System.out.println("Bad Input file path");
      
    }
  }

}
