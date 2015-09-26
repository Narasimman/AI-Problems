package PS1;

public class State {
  private int taskId;
  private int depth;
  private boolean visited;
  private String sequence;
  
  public State(int id, int depth) {
    this.taskId = id;
    this.visited = false;
    this.depth = depth;
  }
  
  public boolean isVisited() {
    return visited;
  }
  
  public int getTaskId() {
    return taskId;
  }
  
  public void setSequence(String s) {
    this.sequence = s;
  }
  
  public String getSequence() {
    return this.sequence;
  }
  
  public int getDepth() {
    return this.depth;
  }
  
  @Override
  public String toString() {
    return "" + this.sequence;
  }
}
