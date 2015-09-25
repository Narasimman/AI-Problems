package PS1;

public class State {
  private int taskId;
  private boolean visited;
  
  public State(int id) {
    this.taskId = id;
    this.visited = false;
  }
  
  public boolean isVisited() {
    return visited;
  }
  
  public int getTask() {
    return taskId;
  }
  
  @Override
  public String toString() {
    return "" + this.taskId;
  }
}
