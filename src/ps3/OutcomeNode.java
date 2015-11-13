package ps3;

import java.util.List;

import ps3.INode.Type;

public class OutcomeNode implements INode {
  private Type type;
  private int utility;
  
  OutcomeNode(int util) {
    this.utility = util;
    this.type = Type.OUTCOME;
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
    // TODO Auto-generated method stub
    return null;
  }
}
