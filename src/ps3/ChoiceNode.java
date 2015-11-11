package ps3;

public class ChoiceNode implements INode {
  private Type type;
  private int utility;
  private boolean chance;
  
  ChoiceNode(int util, boolean chance) {
    this.utility = util;
    this.type = Type.CHOICE;
    this.chance = chance;
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
}
