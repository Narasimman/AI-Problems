package ps3;

import java.util.ArrayList;
import java.util.List;

public class ChoiceNode implements INode {
  private Type type;
  private int utility;
  private boolean chance;
  private int reviewerId;
  private List<Integer> consultedList = new ArrayList<Integer>();
  
  ChoiceNode(int util, boolean chance, int reviewer, List<Integer> reviewerList) {
    this.utility = util;
    this.type = Type.CHOICE;
    this.chance = chance;
    this.reviewerId  = reviewer;
    this.consultedList.addAll(reviewerList);
    //this.consultedList.add(reviewer);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public int getUtility() {
    return utility;
  }
  
  public boolean isChoiceNode() {
    return this.type == Type.CHOICE;
  }
  
  public boolean getChance() {
    return chance;
  }

  public int getReviewerId() {
    return reviewerId;
  }

  public List<Integer> getConsultedList() {
    return consultedList;
  }

}
