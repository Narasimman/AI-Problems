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

import ps3.INode.Action;

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

  void initiateDecisionTree() {
    List<Integer> reviewerList = new ArrayList<Integer>();
    ChoiceNode root = new ChoiceNode(0, true, -1, reviewerList);
    recursiveDecisionTree(root);
  }

  void recursiveDecisionTree(INode node) {
    List<Integer> consultedList ;
    List<INode> actionNodes = new ArrayList<INode>();
    
    switch(node.getType()){
    case CHANCE:
      ChanceNode currentChance = (ChanceNode)node;

      consultedList = currentChance.getConsultedList();

      if(currentChance.getAction() == Action.PUBLISH) {
        INode successNode = new OutcomeNode(util_s);
        actionNodes.add(successNode);
        
        INode failureNode = new OutcomeNode(util_f);
        actionNodes.add(failureNode);
      } else if(currentChance.getAction() == Action.CONSULT) {
        INode choiceNode = new ChoiceNode(0, true, 
            currentChance.getReviewerId(), consultedList);
        actionNodes.add(choiceNode);
        INode NoNode = new OutcomeNode(util_f);
        actionNodes.add(NoNode);        
      }
      
      //Recursive call
      for(INode actionNode : actionNodes ) {
        recursiveDecisionTree(actionNode);
      }

      break;
    case CHOICE:
      ChoiceNode current = (ChoiceNode)node;
      ChanceNode actionNode;

      consultedList = current.getConsultedList();      

      if(current.getChance()) {
        actionNode = new ChanceNode(0, INode.Action.PUBLISH, 
            current.getReviewerId(), consultedList);
        actionNodes.add(actionNode);  
      } else {
        OutcomeNode rejectNode = new OutcomeNode(util_f);    
      }



      for(Reviewer r : reviewers) {
        if(!consultedList.contains(r.getId())) {
          actionNode = new ChanceNode(0, INode.Action.CONSULT, r.getId(), consultedList);
          actionNodes.add(actionNode);
        }
      }

      //Recursive Calls
      for(INode action : actionNodes) {
        recursiveDecisionTree(action);
      }

      break;
    case OUTCOME:
      System.out.println("Terminal");
      break;      
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

    driver.initiateDecisionTree();

    for (Reviewer r : driver.getReviewers()) {
      System.out.println(r.getCost());
    }
  }
}
