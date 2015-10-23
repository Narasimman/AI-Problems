package ps2;

public class Literal {
  private int literal;
  private int atom;
  
  Literal(int literal) {
    this.literal = literal;
    this.atom = Math.abs(literal);
  }
  
  int getAtom() {
    return this.atom;
  }
  
  public Literal copy() {
    return new Literal(this.literal);
  }
  
  int getLiteral() {
    return this.literal;
  }
}
