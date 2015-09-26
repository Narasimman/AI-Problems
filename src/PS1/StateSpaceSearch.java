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

  Queue<Task> queue = new LinkedList<Task>();
  List<Task> taskList = new ArrayList<Task>();
  DirectedGraph<Task, DefaultEdge> g =
      new DefaultDirectedGraph<Task, DefaultEdge>(DefaultEdge.class);

  DirectedGraph<State, DefaultEdge> stateSpaceTree = 
      new DefaultDirectedGraph<State, DefaultEdge>(DefaultEdge.class);
  
  Task initial = new Task(-1, 0, 0);
  State root = new State(initial.getId());
  


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
    stateSpaceTree.addVertex(root);
    Queue<State> stateQueue = new LinkedList<State>();

    TopologicalOrderIterator<Task, DefaultEdge> orderIterator;

    orderIterator =
        new TopologicalOrderIterator<Task, DefaultEdge>(g);

    while(orderIterator.hasNext()) {
      Task task = orderIterator.next();
      System.out.println(">>" + task.getId());
      if(g.inDegreeOf(task) < 1) {
        State s = new State(task.getId());
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
          int sequenceLength = currentState.getSequence().length();
          boolean valid = true;
          
          if(g.inDegreeOf(task) < 1 && !currentState.getSequence().contains(Integer.toString(task.getId()))) {
            State newState = new State(task.getId());
            newState.setSequence(currentState.getSequence() + Integer.toString(task.getId()));
            stateSpaceTree.addVertex(newState);
            stateSpaceTree.addEdge(currentState, newState);
            stateQueue.add(newState);          
          } else if(indegree > 1) {
            for(Task t : Graphs.predecessorListOf(g, task)) {
              if(indegree == sequenceLength && currentState.getSequence().contains(Integer.toString(t.getId()))) {              
                continue;
              } else {
                valid = false;
              }
            }

            if(valid) {
              State newState = new State(task.getId());
              newState.setSequence(currentState.getSequence() + Integer.toString(task.getId()));
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
    Queue<State> Fringe = new LinkedList<State>();
    Fringe.add(root);
    
    int currentValue = 0;
    int currentDeadline = 0;
    
    
    while (!Fringe.isEmpty())
    {
        State Parent = Fringe.poll();
        currentValue = 0;
        currentDeadline = 0;
        if(Parent.getTaskId() > -1) {
          
          String[] s = Parent.getSequence().split("");
          
          for(int i = 1; i < s.length; i++) {
            currentValue += taskList.get(Integer.parseInt(s[i])).getValue();
            currentDeadline += taskList.get(Integer.parseInt(s[i])).getTime();
          }
        }
        //System.out.println("Node Visited " + Parent.getSequence());
        //System.out.println(currentValue + " " + targetValue + " " + currentDeadline + " " + targetDeadline );
        if (currentValue >= targetValue && currentDeadline <= targetDeadline)
        {
            
            System.out.println("Find Goal   " + Parent.getSequence());
            break;
        }//end if
        children = Graphs.successorListOf(stateSpaceTree, Parent);        
        for (int i = 0; i < children.size(); i++)
        {
            Fringe.add(children.get(i));

        }//end for

    }//end while


  }

  public static void main(String[] args) {
    StateSpaceSearch spaceSearch = new StateSpaceSearch();
    spaceSearch.constructSpaceSearchTree();
    spaceSearch.doBFS();
  }
}
