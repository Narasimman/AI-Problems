package ps3;

import java.util.ArrayList;
import java.util.List;

public class OutcomeNode implements INode {
  private Type type;
  private int utility;
  private Action action;
  private boolean isSuccess;
  private List<Integer> consultedList = new ArrayList<Integer>();

  OutcomeNode(boolean isSuccess, Action action, List<Integer> reviewerList) {
    this.isSuccess = isSuccess;
    this.action = action;
    this.type = Type.OUTCOME;
    this.consultedList.addAll(reviewerList);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public int getUtility() {
    return utility;
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
        p_util = MainDriver.util_s;
      } else {
        p_util = MainDriver.util_f;
      }
    }

    this.utility = p_util - util;
    System.out.println(this.utility);
    return new ArrayList<INode>();
  }
}