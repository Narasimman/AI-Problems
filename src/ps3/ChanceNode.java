package ps3;

public class ChanceNode implements INode {
  private Type type;
  private int utility;
  
  ChanceNode(int util) {
    this.utility = util;
    this.type = Type.CHANCE;
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
