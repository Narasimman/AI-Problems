package ps2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  
  void addClause(Clause clause) {
    this.clauses.add(clause);
  }
  
  List<Clause> getClauses() {
    return this.clauses;
  }
  
  void addAtom(Literal atom) {
    this.atoms.add(atom);
  }
  
  Set<Literal> getAtoms() {
    return this.atoms;
  }
}
