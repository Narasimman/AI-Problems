package ps3;

public interface INode {
  
  public enum Type {
    CHANCE,
    CHOICE,
    OUTCOME
  };
  
  public Type getType();
  public int getUtility();
}
