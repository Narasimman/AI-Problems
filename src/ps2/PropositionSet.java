package ps2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PropositionSet {
  
  private List<Clause> clauses = new ArrayList<Clause>();
  
  private Set<Literal> atoms = new HashSet<Literal>();
  
  /**
   * Returns boolean if the clause is empty
   * @return
   */
  public boolean containsEmptyClause() {
    for (Clause clause : clauses) {
      if (clause.isEmpty()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isEmpty() {
    return clauses.isEmpty();
  }
  
  @Override
  public PropositionSet clone() {
    PropositionSet ps = new PropositionSet();
    
    for (Clause clause : this.clauses) {
      ps.addClause(clause.copy());
    }
    
    for (Literal atom : atoms) {
      ps.addAtom(atom);
    }

    return ps;
  }
  
  /**
   * Returns the first singleton clause identified
   * @return
   */
  public Literal findSingletonClause() {
    for (Clause clause : clauses) {
      if (clause.isSingleton()) {
        return clause.getLiterals().get(0);
      }
    }
    return null;
  }
  
  public void propagate(Literal literal) {
    
    this.atoms.remove(literal);
    this.atoms.remove(literal.negative());
    
    // remove satisfied clauses
    List<Clause> removable = new ArrayList<Clause>();
    for (Clause clause : clauses) {
      if (clause.contains(literal)) {
        removable.add(clause);
      }
    }
    clauses.removeAll(removable);
    
    for (Clause clause : clauses) {
      clause.propagate(literal);
    }
  }
  
  public Literal findPureLiteral() {
    //
    Set<Literal> positiveSet = new TreeSet<Literal>();
    Set<Literal> negativeSet = new TreeSet<Literal>();
    for (Clause clause : clauses) {
      List<Literal> atoms = clause.getLiterals();
      
      for (Literal atom : atoms) {
        if (atom.getAtom() > 0) {
          positiveSet.add(atom);
        } else {
          negativeSet.add(atom);
        }
      }
    }
    // find difference between sets
    Set<Literal> diff = difference(positiveSet, negativeSet);
    // if pure literal found, return first one
    System.out.println("DIFF  " + diff);
    if (!diff.isEmpty()) {
      return diff.toArray(new Literal[0])[0];
    }
    return null;
  }

  private Set<Literal> difference(Set<Literal> positiveSet, Set<Literal> negativeSet) {
    // may be simplified
    Iterator<Literal> it1 = positiveSet.iterator();
    Iterator<Literal> it2 = negativeSet.iterator();
    
    Set<Literal> res = new HashSet<Literal>();
    boolean firstAhead = true;
    boolean secondAhead = true;
    Literal first = null;
    Literal second = null;
    while (it1.hasNext() || it2.hasNext()) {
      if (it1.hasNext()) {
        if (firstAhead) {
          first = it1.next();
          firstAhead = false;
        }
      } else {
        first = null;
      }

      if (it2.hasNext()) {
        if (secondAhead) {
          second = it2.next();
          secondAhead = false;
        }
      } else {
        second = null;
      }

      // first iterator runs to the end
      if (first == null && second != null) {
        res.add(second);
        secondAhead = true;
        continue;
      }
      // second iterator runs to the end
      if (first != null && second == null) {
        res.add(first);
        firstAhead = true;
        continue;
      }
      int equalFlag = first.compareTo(second);
        
      if (equalFlag < 0) {
        res.add(first);
        firstAhead = true;
      } else if (equalFlag > 0) {
        res.add(second);
        secondAhead = true;
      } else {
        firstAhead = true;
        secondAhead = true;
      }
    }
    return res;
  }
  
  void addClause(Clause clause) {
    this.clauses.add(clause);
  }
  
  List<Clause> getClauses() {
    return this.clauses;
  }
  
  void addAtom(Literal atom) {
    if (!atoms.contains(atom) && !atoms.contains(atom.negative())) {
      this.atoms.add(atom);
    }
  }
  
  public Literal getAtom() {
    return atoms.toArray(new Literal[0])[0];
  }
  
  Set<Literal> getAtoms() {
    return this.atoms;
  }
}
