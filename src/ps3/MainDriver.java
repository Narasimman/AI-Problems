package ps3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainDriver {
  static int number_of_reviewers;
  static int util_s;
  static int util_f;
  static double p_s;

  private List<Reviewer> reviewers = new ArrayList<Reviewer>();

  MainDriver(String filePath) {
    File inputFile = new File(filePath);
    try(Scanner scanner = new Scanner(inputFile);) {
      number_of_reviewers = scanner.nextInt();
      util_s = scanner.nextInt();
      util_f = scanner.nextInt();
      p_s    = scanner.nextDouble();

      for (int i = 0; i < number_of_reviewers; i++) {
        Reviewer reviewer = new Reviewer(scanner.nextInt(), 
            scanner.nextDouble(), scanner.nextDouble());
        reviewers.add(reviewer);
      }
    } catch(FileNotFoundException e) {
      System.out.println("File not found exception");
    }
  }

  void initiateDecisionTree() {
    List<Integer> reviewerList = new ArrayList<Integer>();
    ChoiceNode root = new ChoiceNode(0, true, -1, reviewerList);

    recursiveDecisionTree(root);
  }

  private void recursiveDecisionTree(INode node) {
    List<INode> actionNodes = new ArrayList<INode>();

    actionNodes = node.getChildren(reviewers);

    //RECURSIVE CALL
    for(INode actionNode : actionNodes ) {
      recursiveDecisionTree(actionNode);
    }
    
  }

  public List<Reviewer> getReviewers() {
    return reviewers;
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Invalid argument! Please provide valid input path");
      return;
    }

    MainDriver driver = new MainDriver(args[0]);
    driver.initiateDecisionTree();
  }
}