package ps3;

import ps3.INode.Type;

public class ChoiceNode implements INode {
  private Type type;
  private int utility;
  
  ChoiceNode(int util) {
    this.utility = util;
    this.type = Type.CHOICE;
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
}
