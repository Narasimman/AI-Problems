package ps3;

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
}
