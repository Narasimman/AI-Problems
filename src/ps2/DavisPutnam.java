package ps2;

import java.io.IOException;

public class DavisPutnam {
  private PropositionSet prop;
  
  public DavisPutnam(String filePath) {
    FileHandler f = new FileHandler(filePath);
    try {
      f.read();
      f.getPropositionSet();
      prop = f.getPropositionSet();
    } catch (IOException e) {
      System.out.println("Bad Input file path");
      
    }
  }
  
  public static void main(String[] args) {
    DavisPutnam dp = new DavisPutnam("dp-input");
  }

}
