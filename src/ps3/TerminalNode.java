package ps3;

public class TerminalNode implements INode {
  private Type type;
  private int utility;
  
  TerminalNode(int util) {
    this.utility = util;
    this.type = Type.TERMINAL;
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
