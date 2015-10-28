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

  List<Literal> getValidation() {    
    return valuation;
  }
  
  void resolve(PropositionSet propositionSet, Literal literal) {
    this.valuation.add(literal);
    propositionSet.resolve(literal);
  }
  
  /**
   * Compute the Davis Putnam algorithm and return the result
   * @return
   */
  boolean compute(PropositionSet prop) {

    while(true) {      
      // Success case. Empty prop set
      if(prop.isEmpty()) {
        valuation.addAll(prop.getAtoms());
        return true;
      }

      // Failure case. Empty clause
      if(prop.containsEmptyClause()) {
        return false;
      }

      // Find if there is pure literal
      Literal pureLiteral = prop.findPureLiteral();

      if (pureLiteral != null) {
        this.resolve(prop, pureLiteral);
      } else {
        // No pure literal, check for singleton clause
        Literal singletonLiteral = prop.findSingletonClause();
        if (singletonLiteral != null) {
          this.resolve(prop, singletonLiteral);          
        } else {
          break;
        }
      }
    } // while

    PropositionSet propSetClone = prop.clone();
    
    // pick the first atom
    Literal atom = propSetClone.getAtom();
    
    // assign atom true
    valuation.add(atom);
    propSetClone.resolve(atom);
    
    // recursively run on prop set and validation
    DavisPutnam recurDP = new DavisPutnam();
    boolean satisfied = recurDP.compute(propSetClone);
    
    // if satisfied
    if (satisfied) {
      this.valuation.addAll(recurDP.getValidation());
      return true;
    } else {
      // assign atom false
      valuation.remove(atom);
      valuation.add(atom.negative());
      
      // propagate atom
      propSetClone = prop.clone();
      
      propSetClone.resolve(atom.negative());
      
      // recur
      recurDP = new DavisPutnam();
      
      boolean sat = recurDP.compute(propSetClone);
      
      if (sat) {
        this.valuation.addAll(recurDP.getValidation());
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
        sb.append(atom + " T \n");
      } else {
        sb.append(atom.negative() + " F \n");
      }
    }
    return sb.toString();
  }
}
