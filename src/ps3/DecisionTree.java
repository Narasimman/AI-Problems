package ps3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTree {
  private List<Reviewer> reviewers;
  static int util_s;
  static int util_f;
  static double p_s;
  static Map<Integer, Map<Integer, INode>> reviewerDecisions = new HashMap<Integer, Map<Integer, INode>>();
  
  DecisionTree(int util_s, int util_f, double p_s, List<Reviewer> reviewers) {
    DecisionTree.util_s = util_s;
    DecisionTree.util_f = util_f;
    DecisionTree.p_s = p_s;
    this.reviewers = reviewers;
  }

  void initiateDecisionTree() {
    List<Integer> reviewerList = new ArrayList<Integer>();
    ChoiceNode root = new ChoiceNode(true, -1, reviewerList, 1, new ArrayList<Boolean>());
    recursiveDecisionTree(root);
    root.calculateUtility();
    System.out.println(root.getUtility());
    
    
    
    for (Integer rId : reviewerDecisions.keySet()) {
      Map<Integer, INode> reviewerChoices = reviewerDecisions.get(rId); 
      for(Integer choice : reviewerChoices.keySet()) {
        System.out.println(rId + "   " + choice + "  " + reviewerChoices.get(choice));
      }
      
    }
    
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
