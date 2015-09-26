/**
 * 
 */
package PS1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * @author Narasimman
 *
 */
public class StateSpaceSearch {
  private int numberOfTasks;
  private int targetValue;
  private int targetDeadline;
  private int maxSize;

  List<Task> taskList = new ArrayList<Task>();
  DirectedGraph<Task, DefaultEdge> g =
      new DefaultDirectedGraph<Task, DefaultEdge>(DefaultEdge.class);

  DirectedGraph<State, DefaultEdge> stateSpaceTree = 
      new DefaultDirectedGraph<State, DefaultEdge>(DefaultEdge.class);

  Task initial = new Task(-1, 0, 0);
  State root = new State(initial.getId(), 0);

  public List<Task> getTaskList() {
    return taskList;
  }

  public int getTargetValue() {
    return targetValue;
  }

  public int getTargetDeadline() {
    return targetDeadline;
  }

  public StateSpaceSearch() {
    try(Scanner scanner = new Scanner(new File("input"));) {      
      numberOfTasks = scanner.nextInt();
      targetValue = scanner.nextInt();
      targetDeadline = scanner.nextInt();
      maxSize = scanner.nextInt();

      for (int i = 0; i < numberOfTasks; i ++) {
        int id = scanner.nextInt();
        int value = scanner.nextInt();
        int time = scanner.nextInt();
        Task task = new Task(id, value, time);
        taskList.add(task);
      }

      for(Task task : taskList) {
        g.addVertex(task);     
      }

      // X is a pre req for Y
      while(scanner.hasNextInt()) {
        int X = scanner.nextInt();
        int Y = scanner.nextInt();
        g.addEdge(taskList.get(X), taskList.get(Y));
      }

      //System.out.println(g.toString());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void constructSpaceSearchTree() {
    Queue<State> stateQueue = new LinkedList<State>();
    stateSpaceTree.addVertex(root);
    //stateQueue.add(root);
    TopologicalOrderIterator<Task, DefaultEdge> orderIterator;
    orderIterator = new TopologicalOrderIterator<Task, DefaultEdge>(g);

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
          State newState = new State(task.getId(), currentState.getDepth() + 1);
          newState.setSequence(currentSequence + sTaskId);
          stateSpaceTree.addVertex(newState);
          stateSpaceTree.addEdge(currentState, newState);
          stateQueue.add(newState);    
          
        } else if(indegree > 0) {
          for(Task t : Graphs.predecessorListOf(g, task)) {
            if(indegree > sequenceLength                
                || !currentSequence.contains(Integer.toString(t.getId()))) {              
              valid = false;
              break;
            }
          }
          if(valid && !currentSequence.contains(sTaskId)) {
            State newState = new State(task.getId(), currentState.getDepth() + 1);
            newState.setSequence(currentSequence + sTaskId);
            stateSpaceTree.addVertex(newState);
            stateSpaceTree.addEdge(currentState, newState);
            stateQueue.add(newState);
          }
        }
      }
    }

    System.out.println(stateSpaceTree);
  }

  private void doBFS() {
    List<State> children = new ArrayList<State>();
    Queue<State> fringe = new LinkedList<State>();
    fringe.add(root);
    while (!fringe.isEmpty())
    {
      State parent = fringe.poll();
      if (parent.isGoalReached(this))
      {

        System.out.println("Find Goal   " + parent.getSequence());
        break;
      }
      children = Graphs.successorListOf(stateSpaceTree, parent);        
      for (int i = 0; i < children.size(); i++) {
        fringe.add(children.get(i));
      }
    }
  }

  public void doIterativeDeepening()
  {
    boolean Cutt_off = false;
    int depth = 0;
    while(Cutt_off == false)
    {
      System.out.println("Search Goal at Depth" + depth);
      Cutt_off = Depth_Limited_Search(depth, Cutt_off);

      depth++;
    }     
  }//end method

  public boolean Depth_Limited_Search(int depth_Limite, boolean Cut_off)
  {   

    List<State> children = new ArrayList<State>();
    Stack<State> fringe = new Stack<State>();
    fringe.push(root);
    while (!fringe.isEmpty())
    {
      State parent = fringe.pop();
      System.out.println("Node Visited " + parent.getTaskId());

      if (parent.isGoalReached(this))
      {

        System.out.println("Find Goal   " + parent.getSequence());
        break;
      }//end if
      if (parent.getDepth() == depth_Limite) { 
        continue;
      } else {
        children = Graphs.successorListOf(stateSpaceTree, parent);
        for (int i = 0; i < children.size(); i++) {
          fringe.push(children.get(i));
        }
      }         
    }
    return Cut_off;
  }



  public static void main(String[] args) {
    StateSpaceSearch spaceSearch = new StateSpaceSearch();
    spaceSearch.constructSpaceSearchTree();
    spaceSearch.doBFS();
    //spaceSearch.doIterativeDeepening();
  }

}
