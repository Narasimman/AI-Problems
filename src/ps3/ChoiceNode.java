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

  @Override
  public List<Integer> getConsultedList() {
    return consultedList;
  }
  
  @Override
  public List<INode> getChildren(List<Reviewer> reviewers) {
    List<Integer> consultedList;

    List<INode> actionNodes = new ArrayList<INode>();
    INode actionNode;

    consultedList = this.getConsultedList();   

    if(this.getChance()) {
      actionNode = new ChanceNode(0, INode.Action.PUBLISH, 
          this.getReviewerId(), consultedList);
    } else {
      actionNode = new OutcomeNode(false, consultedList);
    }
    actionNodes.add(actionNode);

    for(Reviewer r : reviewers) {
      if(!consultedList.contains(r.getId())) {
        actionNode = new ChanceNode(0, INode.Action.CONSULT, r.getId(), consultedList);
        actionNodes.add(actionNode);
      }
    }
    return actionNodes;
  }
}
