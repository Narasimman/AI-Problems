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
  
  public boolean isGoalReached(StateSpaceSearch search) {
    int currentValue = 0;
    int currentDeadline = 0;
    
    if(this.getTaskId() > -1) {      
      String[] s = this.getSequence().split("");      
      for(int i = 1; i < s.length; i++) {
        currentValue += search.getTaskList().get(Integer.parseInt(s[i])).getValue();
        currentDeadline += search.getTaskList().get(Integer.parseInt(s[i])).getTime();
      }
    }
    return (currentValue >= search.getTargetValue() && currentDeadline <= search.getTargetDeadline());
    
  }
  
  @Override
  public String toString() {
    return "" + this.sequence;
  }
}
