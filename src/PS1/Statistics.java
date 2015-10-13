package ps1;

import java.util.ArrayList;
import java.util.List;

/**
 * Records all the statistics related to state space search
 * @author Narasimman
 *
 */
public class Statistics {
  private boolean isSuccess;
  private int numberOfStates;
  private int numberOfFrontierStates;
  private String result;
  private List<Statistics> statList;
  
  Statistics() {
    statList = new ArrayList<Statistics>();
    isSuccess = false;
    result = "0";
  }
  
  public boolean getIsSuccess() {
    return isSuccess;
  }
  
  public void setIsSuccess(boolean s) {
    this.isSuccess = s;
  }

  public int getNumberOfStates() {
    return numberOfStates;
  }

  public void setNumberOfStates(int numberOfStates) {
    this.numberOfStates = numberOfStates;
  }
  
  public void addToList(Statistics stat) {
    statList.add(stat);
  }
  
  public List<Statistics> getStats() {
    return statList;
  }

  public int getNumberOfFrontierStates() {
    return numberOfFrontierStates;
  }

  public void setNumberOfFrontierStates(int numberOfFrontierStates) {
    this.numberOfFrontierStates = numberOfFrontierStates;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
