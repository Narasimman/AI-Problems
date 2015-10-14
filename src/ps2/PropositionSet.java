package ps2;

import java.util.ArrayList;
import java.util.List;

public class PropositionSet {
  
  private List<Clause> clauses = new ArrayList<Clause>();
  
  void addClause(Clause clause) {
    this.clauses.add(clause);
  }
  
  List<Clause> getClauses() {
    return this.clauses;
  }
}
