package ps3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class MainDriver {
  private int number_of_reviewers;
  private int util_s;
  private int util_f;
  private double p_s;
  
  private List<Reviewer> reviewers = new ArrayList<Reviewer>();
  private DirectedGraph<INode, DefaultEdge> decisionTree =
      new DefaultDirectedGraph<INode, DefaultEdge>(DefaultEdge.class);
  
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
    INode root = new ChoiceNode(0, false);
    decisionTree.addVertex(root);
    INode current = root;
    List<Reviewer> currentReviewerList = reviewers;
    
    int length = currentReviewerList.size();
    Queue<INode> queue = new ArrayDeque();
    
    for(Reviewer r : currentReviewerList) {
      INode node = new ChanceNode(0, r.getId());
      decisionTree.addVertex(node);
      decisionTree.addEdge(current, node);
      queue.add(node);
    }
    
    
    
    
    while(!queue.isEmpty()) {
      current = queue.poll();
      switch (current.getType()) {
      case CHANCE:
        INode yesNode = new ChoiceNode(0, true);
        INode noNode = new ChoiceNode(0, false);
        
        decisionTree.addVertex(yesNode);
        decisionTree.addVertex(noNode);
        decisionTree.addEdge(current, yesNode);
        decisionTree.addEdge(current, noNode);
        
        queue.add(yesNode);
        queue.add(noNode);
        
        break;

      case CHOICE:
        ChoiceNode c  = (ChoiceNode) current;
        if(c.getChance()) {
          
          // TODO: Fix this
          INode publishNode = new ChanceNode(0, 0);
          INode consultNode = new ChanceNode(0, 0);
        
          decisionTree.addVertex(publishNode);
          decisionTree.addVertex(consultNode);
          decisionTree.addEdge(current, publishNode);
          decisionTree.addEdge(current, consultNode);
          
          queue.add(publishNode);
          queue.add(consultNode);
          
        } else {
       // TODO: Fix this
          INode rejectNode = new ChanceNode(0, 1);
          INode consultNode = new ChanceNode(0, 1);
        
          decisionTree.addVertex(rejectNode);
          decisionTree.addVertex(consultNode);
          decisionTree.addEdge(current, rejectNode);
          decisionTree.addEdge(current, consultNode);
          
          queue.add(rejectNode);
          queue.add(consultNode);
          
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
    
    driver.generateDecisionTree();
    
    for (Reviewer r : driver.getReviewers()) {
      System.out.println(r.getCost());
    }
    
  }
}
