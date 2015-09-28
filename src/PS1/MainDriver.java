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

/**
 * Main driver that generates random samples as per the stated problem
 * and call the space search algorithms and builds statistics.
 * @author Narasimman
 *
 */
public class MainDriver {
  private List<Task> taskList = new ArrayList<Task>();
  private DirectedGraph<Task, DefaultEdge> graph;
  private StateSpaceSearch search;
  private static Statistics stats = new Statistics();

  /**
   * Constructor
   * @param inputFile
   */
  public MainDriver(String inputFile) {
    Task start = new Task(-1, 0, 0);

    try(Scanner scanner = new Scanner(new File(inputFile));) {      
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

  /**
   * Run method to initiate search and collect stats
   */
  private void run() {
    search.doBFS();
    stats.addToList(search.getStat());
  }

  /**
   * Display the statistics collected
   */
  static void displayResult() {
    int count = 0;
    List<Statistics> statList = stats.getStats();
    for(Statistics stat : stats.getStats()) {
      System.out.println("------------------------");
      System.out.println("Result " + count);
      System.out.println("Search Output: " + stat.getResult());
      System.out.println("Is search successful: " + stat.getIsSuccess());      
      System.out.println("Total Number of states in the tree: " + stat.getNumberOfStates());
      System.out.println("Total Number of frontier states during searching: " + stat.getNumberOfFrontierStates());
      System.out.println("------------------------");
      count++;
    }
    System.out.println("Total Number of searches: " + statList.size());
  }

  /**
   * Construct a random DAG using the algorithm provided in the problem
   * @param N
   * @return
   */
  private static String generateRandomDAG(int N) {
    StringBuffer sb = new StringBuffer();

    Long rangeFrom = Math.round(Math.pow(N, 2) * (1 - (2/Math.sqrt(N)))/4);
    Long rangeTo = Math.round(Math.pow(N, 2) * (1 + (2/Math.sqrt(N)))/4);

    Random r = new Random();
    int targetValue = r.nextInt(rangeTo.intValue() - 
        rangeFrom.intValue()) + rangeFrom.intValue();
    int targetDeadline = r.nextInt(rangeTo.intValue() - 
        rangeFrom.intValue()) + rangeFrom.intValue();
    int maxFrontier = r.nextInt(N - 3) + 3;

    sb.append(N + " " + targetValue + " " + 
        targetDeadline + " " + maxFrontier + "\n");
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
        if(r1.nextInt(100) <= 20) {
          sb.append((P.get(I) -1 ) + " " + (P.get(J) - 1) + "\n");
        }
      }
    }
    return sb.toString();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("Invalid number of arguments. Expected <N,E>");
      System.exit(-1);
    }
    
    int N = Integer.parseInt(args[0]);
    int E = Integer.parseInt(args[1]);
    String inputFile = "input2";
    PrintWriter out = null;
    String input;
    for (int k = 0; k < E; k++) {
      input = MainDriver.generateRandomDAG(N);
      try {
        out = new PrintWriter(inputFile);
        out.write(input);        
      } catch (FileNotFoundException e) {
        System.out.println("Problem in generating input file");
      } finally {      
        out.close();
      }

      MainDriver driver = new MainDriver(inputFile);
      driver.run();
    }
    MainDriver.displayResult();
  }
}
