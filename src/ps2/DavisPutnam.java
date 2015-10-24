package ps2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DavisPutnam {
  private PropositionSet prop;
  private List<Literal> valuation = new ArrayList<Literal>();
  
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
  boolean compute(PropositionSet propositionSet) {
    
    // Success
    if(propositionSet.isEmpty()) {
      valuation.addAll(propositionSet.getAtoms());
      return true;
    }
    
    if(propositionSet.containsEmptyClause()) {
      return false;
    }
    
    
    
    
    
    return false;
  }
  
  public static void main(String[] args) {
    
    DavisPutnam dp = new DavisPutnam("dp-input");
    PropositionSet clone = dp.prop.clone();
    dp.compute(clone);
  }
}
