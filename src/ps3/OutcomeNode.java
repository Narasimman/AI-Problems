package ps3;

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
}
