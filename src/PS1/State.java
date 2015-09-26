package PS1;

public class State {
  private int taskId;
  private boolean visited;
  private String sequence;
  
  public State(int id) {
    this.taskId = id;
    this.visited = false;
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
  
  @Override
  public String toString() {
    return "" + this.sequence;
  }
}
