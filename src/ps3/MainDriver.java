package ps3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class MainDriver {
  private int number_of_reviewers;
  private int util_s;
  private int util_f;
  private double p_s;
  
  private List<Reviewer> reviewers = new ArrayList<Reviewer>();
  private SimpleGraph<INode, DefaultEdge> decisionTree =
      new SimpleGraph<INode, DefaultEdge>(DefaultEdge.class);
  
  MainDriver(String filePath) {
    File inputFile = new File(filePath);
    try(Scanner scanner = new Scanner(inputFile);) {
      number_of_reviewers = scanner.nextInt();
      util_s = scanner.nextInt();
      util_f = scanner.nextInt();
      p_s    = scanner.nextDouble();
      
      for (int i = 0; i < number_of_reviewers; i++) {
        Reviewer reviewer = new Reviewer(scanner.nextInt(), 
            scanner.nextDouble(), scanner.nextDouble());
        reviewers.add(reviewer);
      }
      
    } catch(FileNotFoundException e) {
      System.out.println("File not found exception");
    }
  }
  
  void generateDecisionTree() {
    INode root = new ChoiceNode(0);
    decisionTree.addVertex(root);
    INode current = root;
    List<Reviewer> myList = reviewers;
    int length = myList.size();
    
    
    while(!myList.isEmpty()) {
      switch (current.getType()) {
      case CHANCE:        
        break;

      case CHOICE:
        if(myList.size() == length) {
          // create one edge for each reviewer. first level
          for(Reviewer r : myList) {
            INode node = new ChanceNode(0);
            decisionTree.addEdge(current, node);
          }
        } else {
          
        }
        break;
        
      case OUTCOME:
        break;
       
      default:
        break;
      }
      
      
    }
    
  }
  

  public List<Reviewer> getReviewers() {
    return reviewers;
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Invalid argument! Please provide valid input path");
      return;
    }
    
    MainDriver driver = new MainDriver(args[0]);
    
    for (Reviewer r : driver.getReviewers()) {
      System.out.println(r.getCost());
    }
    
  }
}
