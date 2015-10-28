package ps2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DavisPutnam {
  private PropositionSet prop;
  private List<Literal> valuation = new ArrayList<Literal>();

  /**
   * Read the input file and construct objects
   * @param filePath
   */
  void readInput(String filePath) {
    FileHandler f = new FileHandler(filePath);
    try {
      f.read();
      this.prop = f.getPropositionSet();
    } catch (IOException e) {
      System.out.println("Bad Input file path");
    }
  }

  /**
   * Writes o/p to a file
   */
  void writeOutput() {
    PrintWriter writer = null;
    StringBuffer sb = new StringBuffer();
    Collections.sort(valuation);
    
    for (Literal atom : valuation) {
      if (atom.getLiteral() > 0) {
        sb.append(atom + " T \n");
      } else {
        sb.append(atom.negative() + " F \n");
      }
    }
    
    try {
      writer = new PrintWriter("dp-output", "UTF-8");      
      writer.print(sb.toString());      
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      System.out.println("Error in writing output");
    } finally {
      writer.close();
    }
  }

  List<Literal> getValidation() {    
    return this.valuation;
  }

  /**
   * Resolve the set with the given literal.
   * Remove if its truth
   * @param propositionSet
   * @param literal
   */
  void resolve(PropositionSet propositionSet, Literal literal) {
    this.valuation.add(literal);
    propositionSet.resolve(literal);
  }

  /**
   * Compute the Davis Putnam algorithm and return the result
   * @return
   */
  boolean evaluate(PropositionSet prop) {

    while(true) {      
      // Success case. Empty prop set
      if(prop.isEmpty()) {
        this.valuation.addAll(prop.getAtoms());
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
    this.resolve(propSetClone, atom);

    // recursively run on prop set and validation
    DavisPutnam recurDP = new DavisPutnam();
    boolean satisfied = recurDP.evaluate(propSetClone);

    // if satisfied
    if (satisfied) {
      this.valuation.addAll(recurDP.getValidation());
      return true;
    } else {
      // assign atom false
      valuation.remove(atom);

      // propagate atom
      propSetClone = prop.clone();      
      this.resolve(propSetClone, atom.negative());

      // recurse for false valuation
      recurDP = new DavisPutnam();      
      satisfied = recurDP.evaluate(propSetClone);

      if (satisfied) {
        this.valuation.addAll(recurDP.getValidation());
        return true;
      }
      return false;
    }
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Invalid argument! Please provide valid input path");
      return;
    }

    DavisPutnam dp = new DavisPutnam();
    dp.readInput(args[0]);
    dp.evaluate(dp.prop);
    dp.writeOutput();
  }
}
