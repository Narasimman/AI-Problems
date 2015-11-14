package ps3;

import java.util.ArrayList;
import java.util.List;

public class OutcomeNode implements INode {
  private Type type;
  private int utility;
  private boolean isSuccess;
  private List<Integer> consultedList = new ArrayList<Integer>();
  
  OutcomeNode(boolean isSuccess, List<Integer> reviewerList) {
    this.isSuccess = isSuccess;
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
    
    for(int rId : this.consultedList) {      
      util += reviewers.get(rId).getCost();
    }
    
    if(this.isSuccess) {
      this.utility = MainDriver.util_s - util;
    } else {
      this.utility = MainDriver.util_f - util;
    }
    
    System.out.println(this.utility);
    return new ArrayList<INode>();
  }
}
