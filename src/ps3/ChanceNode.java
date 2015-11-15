package ps3;

import java.util.ArrayList;
import java.util.List;

public class ChanceNode implements INode {
  private Type type;
  private int utility;
  private Action action;
  private int reviewerId;
  private List<Integer> consultedList = new ArrayList<Integer>();
  private double prob;
  private List<Boolean> chances = new ArrayList<Boolean>();
  private List<INode> children = new ArrayList<INode>();

  ChanceNode(int util, Action choice, int reviewer, List<Integer> reviewerList, double prob, List<Boolean> chanceList) {
    this.utility = util;
    this.type = Type.CHANCE;
    this.action = choice;
    this.reviewerId  = reviewer;
    this.consultedList.addAll(reviewerList);
    this.prob = prob;
    chances.addAll(chanceList);
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

  public double getProb() {
    return prob;
  }
  
  private double getProbabilityForOutcomeNode(List<Reviewer> reviewers) {
    double prob = 0.0;
    double s = 1.0, f = 1.0;

    int i =0;
      for(int rId : consultedList) {
        Reviewer r = reviewers.get(rId);
        if(chances.get(i)) {
        s *= r.getP_s();
        f *= r.getP_f();
        } else {
          s *= 1 - r.getP_s();
          f *= 1 - r.getP_f();
        }
        i++;
      
    }

    s *= DecisionTree.p_s;
    f *= (1 - DecisionTree.p_s);

    prob = s/(s+f);

    return prob;
  }

  private double getProbability(List<Reviewer> reviewers) {
    double prob = 0.0;
    double prob_r_s = 0.0;
    double prob_r_f = 0.0;

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
      } else {
        prob_r_s = 1.0;
        prob_r_f = 1.0;
      }

      //System.out.println("Rev : " + reviewerId + "   " + prob_r_s + " " + currentReviewer.getP_s() + " " + this.prob);
      if(this.action == Action.PUBLISH) {
        prob = currentReviewer.getP_s() * DecisionTree.p_s / this.prob;
      } else {
        prob = ((prob_r_s * currentReviewer.getP_s() * DecisionTree.p_s) + 
            (prob_r_f * currentReviewer.getP_f() * (1 - DecisionTree.p_s))) / this.prob;
      
        
      }
    } else {      
      prob = DecisionTree.p_s;
    }
    //System.out.println("Prob " + prob);
    return prob;
  }

  @Override
  public List<INode> getChildren(List<Reviewer> reviewers) {
    List<Integer> consultedList;

    List<INode> actionNodes = new ArrayList<INode>();
    consultedList = this.getConsultedList();

    if(this.getAction() == Action.PUBLISH) {
      double prob = this.getProbabilityForOutcomeNode(reviewers);

      INode successNode = new OutcomeNode(true, this.getAction(), consultedList, prob);
      actionNodes.add(successNode);

      INode failureNode = new OutcomeNode(false, this.getAction(), consultedList, 1- prob);
      actionNodes.add(failureNode);
    } else if(this.getAction() == Action.CONSULT) {
      double prob = this.getProbability(reviewers);
      INode yesChoiceNode = new ChoiceNode(0, true, 
          this.getReviewerId(), consultedList, prob, this.chances);
      actionNodes.add(yesChoiceNode);

      if(consultedList.size() < reviewers.size()) {
        INode noChoiceNode = new ChoiceNode(0, false,
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
  
  public void calculateUtility() {
    double util = 0.0;
    for (INode child : children) {
      util += (child.getUtility() * child.getProb());
    }
    this.utility = (int) util;
    //System.out.println(util);
  }
}
