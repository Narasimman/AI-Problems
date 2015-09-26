package PS1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
      int maxSize = scanner.nextInt();

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
      search.initialize(start, goal);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  void run() {
    search.doBFS();
    search.doIterativeDeepening();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    MainDriver driver = new MainDriver();
    driver.run();
  }

}
