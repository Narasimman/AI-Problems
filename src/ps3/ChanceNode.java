package ps3;

import java.util.ArrayList;
import java.util.List;

public class ChanceNode implements INode {
  private Type type;
  private int utility;
  private Action action;
  private int reviewerId;
  private List<Integer> consultedList = new ArrayList<Integer>();
  
  ChanceNode(int util, Action choice, int reviewer, List<Integer> reviewerList) {
    this.utility = util;
    this.type = Type.CHANCE;
    this.action = choice;
    this.reviewerId  = reviewer;
    this.consultedList.addAll(reviewerList);
    this.consultedList.add(reviewer);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public int getUtility() {
    return utility;
  }
  
  public boolean isChanceNode() {
    return this.type == Type.CHANCE;
  }

  public Action getAction() {
    return action;
  }

  public int getReviewerId() {
    return reviewerId;
  }

  public List<Integer> getConsultedList() {
    return consultedList;
  }

}
