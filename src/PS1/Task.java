package PS1;

public class Task {
  private int id;
  private int value;
  private int time;
  
  public Task(int id, int v, int t) {
    this.id = id;
    this.value = v;
    this.time = t;
  }

  public int getId() {
    return id;
  }

  public int getValue() {
    return value;
  }

  public int getTime() {
    return time;
  }
  
  @Override
  public String toString() {
    return "" + this.id;
  }
}
