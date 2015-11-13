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
    INode root = new ChoiceNode(0, false, -1, new ArrayList<Integer>(0));
    decisionTree.addVertex(root);
    INode current = root;
    List<Reviewer> currentReviewerList = reviewers;

    Queue<INode> queue = new ArrayDeque<INode>();

    for(Reviewer r : currentReviewerList) {
      List<Integer> l = new ArrayList<Integer>();
      l.add(r.getId());
      INode node = new ChanceNode(0, r.getId(), r.getId(), l);
      decisionTree.addVertex(node);
      decisionTree.addEdge(current, node);
      queue.add(node);
    }   

    while(!queue.isEmpty()) {
      current = queue.poll();

      Reviewer currentReiviewer = null;
      for (Reviewer r : currentReviewerList) {
        if(!current.getConsultedList().contains(r.getId())) {
          System.out.println(">>> " + r.getId() + "   " + current.getConsultedList());
          currentReiviewer = r;
          break;
        }
      }

      //System.out.println(currentReiviewer);
      switch (current.getType()) {
      case CHANCE:
        if(currentReiviewer != null) {
          ChanceNode c = (ChanceNode) current;
          INode yesNode = new ChoiceNode(0, true, c.getReviewerId(), c.getConsultedList());
          INode noNode = new ChoiceNode(0, false, c.getReviewerId(), c.getConsultedList());

          decisionTree.addVertex(yesNode);
          decisionTree.addVertex(noNode);
          decisionTree.addEdge(current, yesNode);
          decisionTree.addEdge(current, noNode);

          queue.add(yesNode);
          queue.add(noNode);
        }
        break;

      case CHOICE:
        ChoiceNode c1  = (ChoiceNode) current;

        if(currentReiviewer != null && c1.getChance()) {

          // TODO: Fix this
          INode publishNode = new ChanceNode(0, 0, -1, c1.getConsultedList());
          INode consultNode = new ChanceNode(0, 0, currentReiviewer.getId(), c1.getConsultedList());

          decisionTree.addVertex(publishNode);
          decisionTree.addVertex(consultNode);
          decisionTree.addEdge(current, publishNode);
          decisionTree.addEdge(current, consultNode);

          //queue.add(publishNode);
          queue.add(consultNode);

        } else if (currentReiviewer != null){
          // TODO: Fix this
          INode rejectNode = new ChanceNode(0, 1, -1, c1.getConsultedList());
          INode consultNode = new ChanceNode(0, 1, currentReiviewer.getId(), c1.getConsultedList());

          decisionTree.addVertex(rejectNode);
          decisionTree.addVertex(consultNode);
          decisionTree.addEdge(current, rejectNode);
          decisionTree.addEdge(current, consultNode);

          //queue.add(rejectNode);
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
