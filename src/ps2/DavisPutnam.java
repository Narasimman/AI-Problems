package ps2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DavisPutnam {
  private PropositionSet prop;
  private List<Literal> valuation = new ArrayList<Literal>();

  void readInput(String filePath) {
    FileHandler f = new FileHandler(filePath);
    try {
      f.read();
      this.prop = f.getPropositionSet();
    } catch (IOException e) {
      System.out.println("Bad Input file path");
    }
  }

  public List<Literal> getValidation() {
    System.out.println("GET VAL   "  + valuation);
    
    return valuation;
  }
  /**
   * Compute the Davis Putnam algorithm and return the result
   * @return
   */
  boolean compute(PropositionSet propositionSet) {

    while(true) {
      // Success
      if(propositionSet.isEmpty()) {
        System.out.println("Final " + valuation);
        valuation.addAll(propositionSet.getAtoms());
        return true;
      }

      if(propositionSet.containsEmptyClause()) {
        return false;
      }

      Literal atom = propositionSet.findPureLiteral();

      if (atom != null) {
        // add atom
        System.out.println("Pure  " + atom);
        this.valuation.add(atom);
        propositionSet.propagate(atom);
      } else {
        Literal atom1 = propositionSet.findSingletonClause();
        // remove singleton atoms
        if (atom1 != null) {
          System.out.println("SingleTon  " + atom1);
          this.valuation.add(atom1);
          propositionSet.propagate(atom1);
        } else {
          break;
        }
      }
    } // while

    PropositionSet propSetClone = propositionSet.clone();
    // pick arbitrary atom
    System.out.println(propSetClone.isEmpty());
    Literal atom = propSetClone.getAtom();
    // assign atom true
    System.out.println("Set atom TRUE  " + atom);
    valuation.add(atom);
    propSetClone.propagate(atom);
    // recursively run on prop set and validation
    DavisPutnam recurDP = new DavisPutnam();
    boolean satisfied = recurDP.compute(propSetClone);
    // if satisfied
    if (satisfied) {
      System.out.println("Satisgied    " + valuation);
      this.valuation.addAll(recurDP.getValidation());
      return true;
    } else {
      // assign atom false
      System.out.println("Set atom FALSE  " + atom);
      valuation.remove(atom);
      System.out.println("neg  " + atom);
      System.out.println("neg val  " + valuation);
      valuation.add(atom.negative());
      // propagate atom
      propSetClone = propositionSet.clone();
      
      propSetClone.propagate(atom.negative());
      // recur
      recurDP = new DavisPutnam();
      boolean sat = recurDP.compute(propSetClone);
      if (sat) {
        this.valuation.addAll(recurDP.getValidation());
        System.out.println("SAT   " + valuation);
        return true;
      }
      return false;
    }
  }

  public static void main(String[] args) {

    DavisPutnam dp = new DavisPutnam();
    dp.readInput("dp-input");
    PropositionSet clone = dp.prop.clone();
    dp.compute(clone);

    System.out.println(dp);

  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    Collections.sort(valuation);
    for (Literal atom : valuation) {
      if (atom.getLiteral() > 0) {
        sb.append(atom + " T \r\n");
      } else {
        sb.append(atom.negative() + " F \r\n");
      }
    }
    //sb.append(String.valueOf(0));
    return sb.toString();
  }
}
