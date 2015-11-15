package ps3;

public class Reviewer {
  private int id;
  private int cost;
  private double p_s;
  private double p_f;
  private boolean decision;
  private static int s_id = 0;
  
  Reviewer(int cost, double p_s, double p_f) {
    this.id = s_id++;
    this.cost = cost;
    this.p_s  = p_s;
    this.p_f  = p_f;
  }

  public int getId() {
    return id;
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

  void setDecision(boolean d) {
    this.decision = d;
  }
  
  public boolean getDecision() {
    return decision;
  }
}
