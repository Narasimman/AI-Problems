/**
 * 
 */
package PS1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;

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
  private int maxFrontierSize;
  private int stateCounter;
  
  private List<Task> taskList;
  private DirectedGraph<Task, DefaultEdge> g;
  
  private Map<String, Boolean> searchMap = new HashMap<String, Boolean>();

  private DirectedGraph<State, DefaultEdge> stateSpaceTree = 
      new DefaultDirectedGraph<State, DefaultEdge>(DefaultEdge.class);

  public StateSpaceSearch(List<Task> list, DirectedGraph<Task, DefaultEdge> g) {
    this.taskList = list;
    this.g = g;
    this.goalFound = false;    
  }

  public void initialize(Task s, Task g, int maxFrontierSize) {
    this.start = s;
    this.goal = g;
    this.stateCounter = 0;
    this.root = new State(stateCounter, start.getId(), 0);
    this.maxFrontierSize = maxFrontierSize;
    constructSpaceSearchTree();
  }

  /**
   * Display the result if the goal is found
   * or print 0 if not found
   */
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
    Queue<State> frontier = new LinkedList<State>();
    boolean isFrontierFull = false;
    frontier.add(root);
    
    while (!frontier.isEmpty() && !isFrontierFull) {
      State parent = frontier.poll();
      if (this.isGoalReached(parent)) {
        goalFound = true;
        goalState = parent;
        break;
      }
      children = Graphs.successorListOf(stateSpaceTree, parent);        
      for (int i = 0; i < children.size(); i++) {
        State child = children.get(i);
        if(frontier.size() <= this.maxFrontierSize && !isVisited(child)) {
          frontier.add(child);
        } else {
          isFrontierFull = true;
          break;
        }
      }
    }
    
    if(!frontier.isEmpty()) {
      doIterativeDeepening(frontier);
    }
  }

  private void doIterativeDeepening(Queue<State> frontier) {
    boolean proceed = false;
    int depth = 0;
    while(!proceed && depth <= this.taskList.size())
    {
      //System.out.println("Search Goal at Depth" + depth);
      proceed = Depth_Limited_Search(frontier, depth, proceed);
      depth++;
    }
  }

  private boolean Depth_Limited_Search(Queue<State> frontier, int depthLimit, boolean proceed) {
    List<State> children = new ArrayList<State>();
    Stack<State> fringe = new Stack<State>();
    if(!frontier.isEmpty()) {
      while(!frontier.isEmpty()) {
        fringe.push(frontier.poll());
      }
    }
    
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
          State child = children.get(i);
          if(!isVisited(child))
          fringe.push(child);
        }
      }         
    }
    return proceed;
  }

  private boolean isVisited(State state) {
    String sequence = state.getSequence();
    char[] c = sequence.toCharArray(); 
    Arrays.sort(c);
    String sortedSequence = new String(c);
    if(!searchMap.containsKey(sortedSequence)) {
      searchMap.put(sortedSequence, true);
    } else {
      return true;
    }
    return false;
  }

  private boolean isGoalReached(State goal) {
    Map<String, Integer> map = goal.computeCumulateValues(this.taskList);  
    
    if(map.get("value") >= this.goal.getValue() && map.get("time") <= this.goal.getTime()) {
      this.resultTask = new Task(-1, map.get("value"), map.get("time"));
      System.out.println("States size : " + searchMap.size());
      return true;
    }
    
    return false;
  }

  private void constructSpaceSearchTree() {
    Queue<State> stateQueue = new LinkedList<State>();
    stateSpaceTree.addVertex(root);
    
    Iterator<Task> iterator = taskList.iterator();
    //Adding initial tasks that are with no pre-req
    while(iterator.hasNext()) {
      Task task = iterator.next();
      if(g.inDegreeOf(task) < 1) {
        State s = new State(this.stateCounter + 1, task.getId(), root.getDepth() + 1);
        s.setSequence(Integer.toString(task.getId()));
        stateSpaceTree.addVertex(s);
        stateSpaceTree.addEdge(root, s);
        stateQueue.add(s);
      }
    }
    
    while(!stateQueue.isEmpty()) {
      State currentState = stateQueue.poll();
      iterator = taskList.iterator();
      
      while(iterator.hasNext()) {
        String currentSequence = currentState.getSequence();
        Task task = iterator.next();
        if(currentSequence.contains(Integer.toString(task.getId()))) {
          continue;
        }
        
        int indegree = g.inDegreeOf(task);        
        int sequenceLength = currentSequence.length();
        int taskId = task.getId();
        String sTaskId = Integer.toString(taskId);
        boolean valid = true;

        if(indegree < 1 && !currentSequence.contains(sTaskId)
            && g.inDegreeOf(taskList.get(Integer.parseInt(currentSequence.substring(sequenceLength - 1)))) < 1){
          if(currentState.isValidState(taskList, this.goal)) {
            stateQueue.add(addNewStateToTree(taskId, currentState, currentSequence));
          }

        } else if(indegree > 0) {
          for(Task t : Graphs.predecessorListOf(g, task)) {
            if(indegree > sequenceLength ||               
                !currentSequence.contains(Integer.toString(t.getId()))) {              
              valid = false;
              break;
            }
          }

          if(valid && !currentSequence.contains(sTaskId)) {
            if(currentState.isValidState(taskList, this.goal)) {
              stateQueue.add(addNewStateToTree(taskId, currentState, currentSequence));
            }
          }
        }
      }
    }

    System.out.println(stateSpaceTree);
    System.out.println("Tree size = " + stateSpaceTree.vertexSet().size());
  }
    
  /**
   * Create new vertex and add it to the state space tree.
   * @param taskId
   * @param currentState
   * @param currentSequence
   * @return new state created
   */
  private State addNewStateToTree(int taskId, State currentState, String currentSequence) {
    State newState = new State(this.stateCounter + 1, taskId, currentState.getDepth() + 1);
    newState.setSequence(currentSequence + Integer.toString(taskId));
    stateSpaceTree.addVertex(newState);
    stateSpaceTree.addEdge(currentState, newState);   
    return newState;
  }

  public List<Task> getTaskList() {
    return taskList;
  }

}
