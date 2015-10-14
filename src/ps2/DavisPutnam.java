package ps2;

import java.io.IOException;

public class DavisPutnam {
  private PropositionSet prop;
  
  DavisPutnam(String filePath) {
    FileHandler f = new FileHandler(filePath);
    try {
      f.read();
      this.prop = f.getPropositionSet();
    } catch (IOException e) {
      System.out.println("Bad Input file path");
      
    }
  }
  /**
   * Compute the Davis Putnam algorithm and return the result
   * @return
   */
  boolean compute() {
    return false;
  }
  
  public static void main(String[] args) {
    DavisPutnam dp = new DavisPutnam("dp-input");
    dp.compute();
  }
}
