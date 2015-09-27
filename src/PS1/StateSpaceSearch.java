/**
 * 
 */
package PS1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * @author Narasimman
 *
 */
public class StateSpaceSearch {  
  private Task start;
  private Task goal;

  private State root;
  private boolean goalFound;
  private State goalState;
  private Task resultTask;

  private List<Task> taskList;
  private DirectedGraph<Task, DefaultEdge> g;

  private DirectedGraph<State, DefaultEdge> stateSpaceTree = 
      new DefaultDirectedGraph<State, DefaultEdge>(DefaultEdge.class);

  public StateSpaceSearch(List<Task> list, DirectedGraph<Task, DefaultEdge> g) {
    this.taskList = list;
    this.g = g;
    this.goalFound = false;
    
  }

  public void initialize(Task s, Task g) {
    this.start = s;
    this.goal = g;

    this.root = new State(start.getId(), 0);

    constructSpaceSearchTree();
  }

  public void displayResult() {
    if(this.goalFound) { 
      System.out.println("[" + this.goalState.getSequence() + "] " + 
          resultTask.getValue()+ " " + resultTask.getTime());
    } else {
      System.out.println("0");
    }
  }
  void doBFS() {
    List<State> children = new ArrayList<State>();
    Queue<State> fringe = new LinkedList<State>();
    fringe.add(root);
    while (!fringe.isEmpty())
    {
      State parent = fringe.poll();
      if (this.isGoalReached(parent))
      {
        goalFound = true;
        goalState = parent;
        break;
      }
      children = Graphs.successorListOf(stateSpaceTree, parent);        
      for (int i = 0; i < children.size(); i++) {
        fringe.add(children.get(i));
      }
    }
  }

  void doIterativeDeepening() {
    boolean proceed = false;
    int depth = 0;
    while(!proceed && depth <= this.taskList.size())
    {
      //System.out.println("Search Goal at Depth" + depth);
      proceed = Depth_Limited_Search(depth, proceed);
      depth++;
    }     
  }

  private boolean Depth_Limited_Search(int depthLimit, boolean proceed) {
    List<State> children = new ArrayList<State>();
    Stack<State> fringe = new Stack<State>();
    fringe.push(root);
    while (!fringe.isEmpty())
    {
      State parent = fringe.pop();
      //System.out.println("Node Visited " + parent.getSequence());

      if (this.isGoalReached(parent))
      {
        goalFound = true;        
        goalState = parent;
        proceed = true;
        break;
      }

      if (parent.getDepth() == depthLimit) { 
        continue;
      } else {
        children = Graphs.successorListOf(stateSpaceTree, parent);
        for (int i = 0; i < children.size(); i++) {
          fringe.push(children.get(i));
        }
      }         
    }
    return proceed;
  }

  private boolean isGoalReached(State goal) {
    int currentValue = 0;
    int currentDeadline = 0;

    if(goal.getTaskId() > -1) {      
      String[] s = goal.getSequence().split("");      
      for(int i = 1; i < s.length; i++) {
        currentValue += taskList.get(Integer.parseInt(s[i])).getValue();
        currentDeadline += taskList.get(Integer.parseInt(s[i])).getTime();
      }
    }
    
    if(currentValue >= this.goal.getValue() && currentDeadline <= this.goal.getTime()) {
      this.resultTask = new Task(-1, currentValue, currentDeadline);
      return true;
    }
    
    return false;

  }

  private void constructSpaceSearchTree() {
    Queue<State> stateQueue = new LinkedList<State>();
    stateSpaceTree.addVertex(root);
    //stateQueue.add(root);
    TopologicalOrderIterator<Task, DefaultEdge> orderIterator;
    orderIterator = new TopologicalOrderIterator<Task, DefaultEdge>(g);

    //Adding initial tasks that are with no pre-req
    while(orderIterator.hasNext()) {
      Task task = orderIterator.next();
      if(g.inDegreeOf(task) < 1) {
        State s = new State(task.getId(), root.getDepth() + 1);
        s.setSequence(Integer.toString(task.getId()));
        stateSpaceTree.addVertex(s);
        stateSpaceTree.addEdge(root, s);
        stateQueue.add(s);
      }
    }

    while(!stateQueue.isEmpty()) {
      State currentState = stateQueue.poll();
      orderIterator =
          new TopologicalOrderIterator<Task, DefaultEdge>(g);      
      while(orderIterator.hasNext()) {
        Task task = orderIterator.next();
        int indegree = g.inDegreeOf(task);
        String currentSequence = currentState.getSequence();
        int sequenceLength = currentSequence.length();
        int taskId = task.getId();
        String sTaskId = Integer.toString(taskId);
        boolean valid = true;

        if(indegree < 1 && !currentSequence.contains(sTaskId)
            && g.inDegreeOf(taskList.get(Integer.parseInt(currentSequence.substring(sequenceLength - 1)))) < 1){

          stateQueue.add(addNewStateToTree(taskId, currentState, currentSequence));    

        } else if(indegree > 0) {
          for(Task t : Graphs.predecessorListOf(g, task)) {
            if(indegree > sequenceLength                
                || !currentSequence.contains(Integer.toString(t.getId()))) {              
              valid = false;
              break;
            }
          }

          if(valid && !currentSequence.contains(sTaskId)) {
            stateQueue.add(addNewStateToTree(taskId, currentState, currentSequence));
          }
        }
      }
    }

    System.out.println(stateSpaceTree);
  }

  /**
   * Create new vertex and add it to the state space tree.
   * @param taskId
   * @param currentState
   * @param currentSequence
   * @return new state created
   */
  private State addNewStateToTree(int taskId, State currentState, String currentSequence) {
    State newState = new State(taskId, currentState.getDepth() + 1);
    newState.setSequence(currentSequence + Integer.toString(taskId));
    stateSpaceTree.addVertex(newState);
    stateSpaceTree.addEdge(currentState, newState);   
    return newState;
  }

}
