package ps2;

import java.util.ArrayList;
import java.util.List;

public class Clause {
  private int id;
  private List<Literal> literals = new ArrayList<Literal>();

  Clause(int id) {
    this.id = id;
  }
  
  void addLiteral(Literal literal) {
    this.literals.add(literal);
  }
  
  List<Literal> getLiterals() {
    return this.literals;
  }
  
  public Clause clone() {
    Clause c = new Clause(this.id);
    for(Literal literal : literals){
      c.addLiteral(literal.copy());
    }
    return c;
  }
  
  boolean isSingleton() {
    return literals.size() == 1;
  }
  
  boolean isEmpty() {
    return literals.isEmpty();
  }

  boolean contains(Literal literal) {
    return literals.contains(literal);
  }
  
  
  void resolve(Literal literal) {
    literals.remove(literal);
    literals.remove(literal.negative());
  }
  
}
