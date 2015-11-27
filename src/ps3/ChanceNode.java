package ps3;

import java.util.ArrayList;
import java.util.List;

public class ChanceNode implements INode {
  private final Type type;
  private final Action action;
  private final int reviewerId;
  private final List<Integer> consultedList = new ArrayList<Integer>();
  private final double prob;
  private final List<Boolean> chances = new ArrayList<Boolean>();
  
  //Non final fields populated during backtracking
  private List<INode> children = new ArrayList<INode>();
  private int utility;
  
  ChanceNode(Action action, int reviewer, List<Integer> reviewerList,
      double prob, List<Boolean> chanceList) {
    this.type = Type.CHANCE;
    this.action = action;
    this.reviewerId  = reviewer;
    this.consultedList.addAll(reviewerList);
    this.prob = prob;
    chances.addAll(chanceList);
    if(action == Action.CONSULT) {
      this.consultedList.add(reviewer);
    }
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
  public Type getType() {
    return type;
  }

  @Override
  public int getUtility() {
    return utility;
  }
  
  @Override
  public List<Integer> getConsultedList() {
    return consultedList;
  }

  public double getProb() {
    return prob;
  }

  private double getProbabilityForOutcomeNode(List<Reviewer> reviewers) {
    double prob = 0.0;
    double p_success = 1.0, p_failure = 1.0;

    int i = 0;
    for(int rId : consultedList) {
      Reviewer r = reviewers.get(rId);
      if(chances.get(i)) {
        p_success *= r.getP_s();
        p_failure *= r.getP_f();
      } else {
        p_success *= 1 - r.getP_s();
        p_failure *= 1 - r.getP_f();
      }
      i++;
    }

    p_success *= DecisionTree.PROB_SUCCESS;
    p_failure *= (1 - DecisionTree.PROB_SUCCESS);

    prob = p_success/(p_success + p_failure);

    return prob;
  }

  private double getProbabilityForChoiceNode(List<Reviewer> reviewers) {
    double prob = 0.0;
    double prob_r_s = 1.0;
    double prob_r_f = 1.0;

    Reviewer currentReviewer = null;
    if(reviewerId >= 0) {
      currentReviewer  = reviewers.get(reviewerId);
    }

    if(currentReviewer != null) {
      if (consultedList.size() - 1  > 0) {
        int i = 0;
        for(int rId : consultedList) {
          if(rId != currentReviewer.getId()){
            if(chances.get(i)) {
              prob_r_s = reviewers.get(rId).getP_s();
              prob_r_f = reviewers.get(rId).getP_f();
            } else {
              prob_r_s = 1 - reviewers.get(rId).getP_s();
              prob_r_f = 1 - reviewers.get(rId).getP_f();
            }
            i++;  
          }
        }
      }

      if(this.action == Action.PUBLISH) {
        prob = currentReviewer.getP_s() * DecisionTree.PROB_SUCCESS / this.prob;
      } else {
        prob = ((prob_r_s * currentReviewer.getP_s() * DecisionTree.PROB_SUCCESS) + 
            (prob_r_f * currentReviewer.getP_f() * (1 - DecisionTree.PROB_SUCCESS))) / this.prob;


      }
    } else {      
      prob = DecisionTree.PROB_SUCCESS;
    }
    return prob;
  }

  @Override
  public List<INode> getChildren(List<Reviewer> reviewers) {
    List<INode> actionNodes = new ArrayList<INode>();
    List<Integer> consultedList = this.getConsultedList();

    if(this.getAction() == Action.PUBLISH) {
      double prob = this.getProbabilityForOutcomeNode(reviewers);

      INode successNode = new OutcomeNode(true, this.getAction(), consultedList, prob);
      actionNodes.add(successNode);

      INode failureNode = new OutcomeNode(false, this.getAction(), consultedList, 1- prob);
      actionNodes.add(failureNode);
    } else if(this.getAction() == Action.CONSULT) {
      
      double prob = this.getProbabilityForChoiceNode(reviewers);
      INode yesChoiceNode = new ChoiceNode(true, this.getReviewerId(), consultedList, prob, this.chances);
      actionNodes.add(yesChoiceNode);

      if(consultedList.size() < reviewers.size()) {
        INode noChoiceNode = new ChoiceNode(false,
            this.getReviewerId(), consultedList, 1 - prob, this.chances);
        actionNodes.add(noChoiceNode);
      } else {
        // Here you don't need to pass the consulted list as this is for reject
        INode noNode = new OutcomeNode(false, Action.REJECT, consultedList, 1 - prob);
        actionNodes.add(noNode);
      }
    }
    children = actionNodes;
    return actionNodes;
  }

  @Override
  public void calculateUtility() {
    double util = 0.0;
    for (INode child : children) {
      util += (child.getUtility() * child.getProb());
    }
    this.utility = (int) util;
    //System.out.println(util);
  }
}
