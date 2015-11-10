package ps3;

public class Reviewer {
  private int cost;
  private double p_s;
  private double p_f;
  
  Reviewer(int cost, double p_s, double p_f) {
    this.cost = cost;
    this.p_s  = p_s;
    this.p_f  = p_f;
  }

  public int getCost() {
    return cost;
  }

  public double getP_s() {
    return p_s;
  }

  public double getP_f() {
    return p_f;
  }
}
