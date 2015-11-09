package ps3;

public interface INode {
  
  public enum Type {
    CHANCE,
    CHOICE,
    TERMINAL
  };
  
  public Type getType();
  public int getUtility();
}
