package ps3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;

import ps3.INode.Action;

public class DecisionTree {
  private final List<Reviewer> reviewers;
  static int SUCCESS_UTIL;
  static int FAILURE_UTIL;
  static double PROB_SUCCESS;
  private DirectedGraph<INode, DefaultEdge> tree =
      new DefaultDirectedGraph<INode, DefaultEdge>(DefaultEdge.class);

  DecisionTree(int util_s, int util_f, double p_s, List<Reviewer> reviewers) {
    DecisionTree.SUCCESS_UTIL = util_s;
    DecisionTree.FAILURE_UTIL = util_f;
    DecisionTree.PROB_SUCCESS = p_s;
    this.reviewers = reviewers;
  }

  private void predictDecision(ChoiceNode root) {
    System.out.println("Expected Utility: " + root.getUtility());

    Scanner scanner = new Scanner(System.in);
    boolean response = false;

    INode bestChild = root.getBestChild();
    List<INode> children;
    boolean done = false;

    while(!done) {
      if(bestChild instanceof ChanceNode) {
        ChanceNode node = (ChanceNode)bestChild;

        if(node.getAction() == Action.PUBLISH) {
          System.out.println(node.getAction());
          done = true;
          break;
        }

        System.out.println("Consult Reviewer " + (node.getReviewerId() + 1) + ": ");
        String userInput = scanner.next();

        if(userInput.equalsIgnoreCase("yes")) {
          response = true;
        } else {
          response = false;
        }

        children = Graphs.successorListOf(tree, node);

        for(INode child : children) {
          if(child instanceof ChoiceNode) {
            //ask a different reviewer
            if(((ChoiceNode) child).getChance() == response) {
              bestChild = ((ChoiceNode) child).getBestChild();
              break;
            }            
          } else if (child instanceof OutcomeNode) {
            // reject
            System.out.println(((OutcomeNode)child).getAction());
            done = true;
            break;
          }
        }
      }
    }
    scanner.close();
  }

  void initiateDecisionTree() {
    List<Integer> reviewerList = new ArrayList<Integer>();
    ChoiceNode root = new ChoiceNode(true, -1, reviewerList, 1, new ArrayList<Boolean>());
    recursiveDecisionTree(root);
    root.calculateUtility();
    
    //call the predictor
    predictDecision(root);
  }

  private void recursiveDecisionTree(INode node) {
    List<INode> actionNodes = new ArrayList<INode>();

    actionNodes = node.getChildren(reviewers);

    tree.addVertex(node);

    //RECURSIVE CALL
    for(INode actionNode : actionNodes ) {

      tree.addVertex(actionNode);
      tree.addEdge(node, actionNode);

      recursiveDecisionTree(actionNode);
      actionNode.calculateUtility();
    }
  }
}