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
    
    if(choice == Action.CONSULT) {
      this.consultedList.add(reviewer);
    }

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

  @Override
  public List<Integer> getConsultedList() {
    return consultedList;
  }
  
  @Override
  public List<INode> getChildren(List<Reviewer> reviewers) {
    List<Integer> consultedList;
    
    List<INode> actionNodes = new ArrayList<INode>();
    consultedList = this.getConsultedList();

    if(this.getAction() == Action.PUBLISH) {
      INode successNode = new OutcomeNode(true, consultedList);
      actionNodes.add(successNode);

      INode failureNode = new OutcomeNode(false, consultedList);
      actionNodes.add(failureNode);      
    } else if(this.getAction() == Action.CONSULT) {
      INode yesChoiceNode = new ChoiceNode(0, true, 
          this.getReviewerId(), consultedList);
      actionNodes.add(yesChoiceNode);

      if(consultedList.size() == reviewers.size()) {
        INode noChoiceNode = new ChoiceNode(0, true, 
            this.getReviewerId(), consultedList);
        actionNodes.add(noChoiceNode);
      } else {
        INode NoNode = new OutcomeNode(false, consultedList);
        actionNodes.add(NoNode);
      }
    }
    return actionNodes;
  }
}
