package ps2;

public class Literal {
  private int literal;
  private int atom;
  
  public Literal(int literal) {
    this.literal = literal;
    this.atom = Math.abs(literal);
  }
  
  public int getAtom() {
    return this.atom;
  }
}
