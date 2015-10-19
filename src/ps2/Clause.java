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
  
  boolean isSingleton() {
    return literals.size() == 1;
  }
  
  boolean isEmpty() {
    return literals.isEmpty();
  }

  boolean contains(Literal literal) {
    return literals.contains(literal);
  }
  
  boolean propagate(Literal literal) {
    return literals.remove(literal);
  }
  
}
