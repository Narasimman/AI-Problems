package ps2;

import java.util.Comparator;

public class Literal implements Comparator<Literal>, Comparable<Literal>{
  private int literal;
  private int atom;
  
  Literal(int literal) {
    this.literal = literal;
    this.atom = literal;
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
  
  Literal negative() {
    return new Literal(-this.atom);
  }
  
  @Override
  public boolean equals(Object obj) {
    Literal atom = (Literal) obj;
    return this.literal == atom.literal;
  }

  @Override
  public int hashCode() {
    return this.literal;
  }

  @Override
  public String toString() {
    return "" + literal;
  }

  @Override
  public int compare(Literal arg0, Literal arg1) {
    if (Math.abs(arg0.literal) == Math.abs(arg1.literal)) {
      return 0;
    } else if (Math.abs(arg0.literal) > Math.abs(arg1.literal)) {
      return 1;
    } else {
      return -1;
    }
  }

  public int compareTo(Literal arg1) {
    return this.compare(this, arg1);
  }

}
