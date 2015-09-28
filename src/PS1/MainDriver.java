package PS1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;

public class MainDriver {
  private List<Task> taskList = new ArrayList<Task>();
  private DirectedGraph<Task, DefaultEdge> graph;
  private StateSpaceSearch search;

  public MainDriver() {
    Task start = new Task(-1, 0, 0);

    try(Scanner scanner = new Scanner(new File("input1"));) {      
      int numberOfTasks = scanner.nextInt();
      int targetValue = scanner.nextInt();
      int targetDeadline = scanner.nextInt();
      int maxFrontierSize = scanner.nextInt();

      Task goal = new Task(-2, targetValue, targetDeadline);
      for (int i = 0; i < numberOfTasks; i ++) {
        int id = scanner.nextInt();
        int value = scanner.nextInt();
        int time = scanner.nextInt();
        Task task = new Task(id, value, time);
        taskList.add(task);
      }

      List<Integer> dep = new ArrayList<Integer>();

      // X is a pre req for Y
      while(scanner.hasNextInt()) {
        dep.add(scanner.nextInt());        
      }

      DependencyGraph g = new DependencyGraph();      
      graph = g.createDependencyGraph(taskList, dep);

      search = new StateSpaceSearch(taskList, graph);
      search.initialize(start, goal, maxFrontierSize);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  void run() {
    search.doBFS();
    search.displayResult();    
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    MainDriver driver = new MainDriver();
    driver.run();
    
    int N = Integer.parseInt(args[0]);
    int E = Integer.parseInt(args[1]);
    
    List<Integer> P = new ArrayList<Integer>();
    Random r1 = new Random(N);
    for (int i = 0; i < N; i++) {
      int value = r1.nextInt(N-1) + 1;
      int time = r1.nextInt(N-1) + 1;
      P.add(i + 1);
      //System.out.println(i + " " + value + " " + time);
    }
    
    Random r2 = new Random(System.currentTimeMillis());
    /* Construct a random permutation */
    for (int I=0; I < N; I++) {         
      Collections.shuffle(P, r2);
    }
    
    Random r3 = new Random(System.currentTimeMillis());
    for (int I=0; I < N-1; I++) {
      for (int J = I+1; J < N; J++) {
         if(r3.nextInt(1) == 0) {
           //System.out.println((P.get(I) - 1) + " " + (P.get(J) -1 ));
         }
      }
    }
  }

}
