package ps3;

import java.util.ArrayList;
import java.util.List;

public class OutcomeNode implements INode {
  private Type type;
  private int utility;
  private Action action;
  private boolean isSuccess;
  private List<Integer> consultedList = new ArrayList<Integer>();
  private double prob;

  OutcomeNode(boolean isSuccess, Action action, List<Integer> reviewerList, double prob) {
    this.isSuccess = isSuccess;
    this.action = action;
    this.type = Type.OUTCOME;
    this.consultedList.addAll(reviewerList);
    this.prob = prob;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public int getUtility() {
    return utility;
  }
  
  public double getProb() {
    return prob;
  }
  
  public Action getAction() {
    return action;
  }

  public boolean isOutcomeNode() {
    return this.type == Type.OUTCOME;
  }

  @Override
  public List<Integer> getConsultedList() {
    return consultedList;
  }

  @Override
  public List<INode> getChildren(List<Reviewer> reviewers) {
    int util = 0;
    int p_util = 0;
    
    for(int rId : this.consultedList) {      
      util += reviewers.get(rId).getCost();
    }

    if(this.action != Action.REJECT) {
      if(this.isSuccess) {
        p_util = DecisionTree.SUCCESS_UTIL;
      } else {
        p_util = DecisionTree.FAILURE_UTIL;
      }
    }

    this.utility = p_util - util;
    return new ArrayList<INode>();
  }
  
  public void calculateUtility() {
    //
  }
}