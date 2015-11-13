package ps3;

import java.util.List;

public interface INode {
  
  public enum Type {
    CHANCE,
    CHOICE,
    OUTCOME
  };
  
  public Type getType();
  public int getUtility();
  public List<Integer> getConsultedList();
}
