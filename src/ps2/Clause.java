package ps2;

import java.util.ArrayList;
import java.util.List;

public class Clause {
  private int id;
  private List<Literal> literals = new ArrayList<Literal>();

  public Clause(int id) {
    this.id = id;
  }
  
  public void addLiteral(Literal literal) {
    this.literals.add(literal);
  }
  
  public List<Literal> getLiterals() {
    return this.literals;
  }
  
  public boolean isSingleton() {
    return literals.size() == 1;
  }
  
  public boolean isEmpty() {
    return literals.isEmpty();
  }

  public boolean contains(Literal literal) {
    return literals.contains(literal);
  }
}
