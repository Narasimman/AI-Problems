package ps3;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
  private List<Reviewer> reviewers;
  static int util_s;
  static int util_f;
  static double p_s;
  
  DecisionTree(int util_s, int util_f, double p_s, List<Reviewer> reviewers) {
    DecisionTree.util_s = util_s;
    DecisionTree.util_f = util_f;
    DecisionTree.p_s = p_s;
    this.reviewers = reviewers;
  }

  void initiateDecisionTree() {
    List<Integer> reviewerList = new ArrayList<Integer>();
    ChoiceNode root = new ChoiceNode(0, true, -1, reviewerList, 1, new ArrayList<Boolean>());
    recursiveDecisionTree(root);
    root.calculateUtility();
    System.out.println(root.getUtility());
  }

  private void recursiveDecisionTree(INode node) {
    List<INode> actionNodes = new ArrayList<INode>();

    actionNodes = node.getChildren(reviewers);

    //RECURSIVE CALL
    for(INode actionNode : actionNodes ) {      
      recursiveDecisionTree(actionNode);
      actionNode.calculateUtility();
    }
    
    
  }  
}
