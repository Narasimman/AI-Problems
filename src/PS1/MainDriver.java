package PS1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

  public MainDriver(String inputFile) {
    Task start = new Task(-1, 0, 0);

    try(Scanner scanner = new Scanner(new File("input"));) {      
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
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Input file not found by the Driver");
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
    int N = Integer.parseInt(args[0]);
    int E = Integer.parseInt(args[1]);
    String inputFile = "input2";
    PrintWriter out = null;

    for (int k = 0; k < E; k++) {
      try {
        out = new PrintWriter(inputFile);
      } catch (FileNotFoundException e) {
        System.out.println("Problem in generating input file");
      }
      StringBuffer sb = new StringBuffer();

      Long rangeFrom = Math.round(Math.pow(N, 2) * (1 - (2/Math.sqrt(N)))/4);
      Long rangeTo = Math.round(Math.pow(N, 2) * (1 + (2/Math.sqrt(N)))/4);

      Random r = new Random();
      int targetValue = r.nextInt(rangeTo.intValue() - rangeFrom.intValue()) + rangeFrom.intValue();
      int targetDeadline = r.nextInt(rangeTo.intValue() - rangeFrom.intValue()) + rangeFrom.intValue();
      int maxFrontier = r.nextInt(N - 3) + 3;

      sb.append(N + " " + targetValue + " " + targetDeadline + " " + maxFrontier + "\n");
      List<Integer> P = new ArrayList<Integer>();
      Random r1 = new Random(N);
      for (int i = 0; i < N; i++) {
        int value = r1.nextInt(N-1) + 1;
        int time = r1.nextInt(N-1) + 1;
        P.add(i + 1);
        sb.append(i + " " + value + " " + time + "\n");      
      }

      /* Construct a random permutation */
      Collections.shuffle(P, r1);

      for (int I=0; I < N-1; I++) {
        for (int J = I+1; J < N; J++) {
          if(r1.nextInt(100) <= 50) {
            sb.append((P.get(I) -1 ) + " " + (P.get(J) - 1) + "\n");
          }
        }
      }
      out.write(sb.toString());
      out.close();
      
      MainDriver driver = new MainDriver(inputFile);
      driver.run();

    }
  }
}
