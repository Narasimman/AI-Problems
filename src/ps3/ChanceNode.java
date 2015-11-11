package ps3;

public class ChanceNode implements INode {
  private Type type;
  private int utility;
  private int choice;
  
  ChanceNode(int util, int choice) {
    this.utility = util;
    this.type = Type.CHANCE;
    this.choice = choice;
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

  public int getChoice() {
    return choice;
  }
}
