/**
 * 
 */
package PS1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * @author Narasimman
 *
 */
public class StateSpaceSearch {
  private int numberOfTasks;
  private int targetValue;
  private int targetDeadline;
  private int maxSize;
  
  Queue<Task> queue = new java.util.LinkedList<Task>();
  List<Task> taskList = new ArrayList<Task>();
  DirectedGraph<Task, DefaultEdge> g =
      new DefaultDirectedGraph<Task, DefaultEdge>(DefaultEdge.class);
  
  DirectedGraph<State, DefaultEdge> stateSpaceTree = 
      new DefaultDirectedGraph<State, DefaultEdge>(DefaultEdge.class);
  
  
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
        queue.add(task);
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
      
      System.out.println(g.toString());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  private void constructSpaceSearchTree() {
    Task initial = new Task(-1, -1, -1);
    State state = new State(initial.getId());
    
    stateSpaceTree.addVertex(state);
    
    Iterator<Task> it = g.vertexSet().iterator();
    
    while(it.hasNext()) {
      Task task = it.next();
      if(g.inDegreeOf(task) < 1) {
        State s = new State(task.getId());
        stateSpaceTree.addVertex(s);
        stateSpaceTree.addEdge(state, s);
        
        System.out.println(task.getId() + " ==>  " + g.outDegreeOf(task));
        
        
      }
    }
    System.out.println(stateSpaceTree);
  }
  
  private void doBFS() {
    while(!queue.isEmpty()) {
      Task task = queue.poll();
      //System.out.println(task.getId());
    }
    
  }
  
  public static void main(String[] args) {
    StateSpaceSearch spaceSearch = new StateSpaceSearch();
    spaceSearch.constructSpaceSearchTree();
    spaceSearch.doBFS();
  }
}
