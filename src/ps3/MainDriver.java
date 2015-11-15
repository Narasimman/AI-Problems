package ps3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainDriver {  
  private DecisionTree tree;  
  
  MainDriver(String filePath) {
    File inputFile = new File(filePath);
    try(Scanner scanner = new Scanner(inputFile);) {
      int number_of_reviewers = scanner.nextInt();
      int util_s = scanner.nextInt();
      int util_f = scanner.nextInt();
      double p_s    = scanner.nextDouble();

      List<Reviewer> reviewers = new ArrayList<Reviewer>();
      for (int i = 0; i < number_of_reviewers; i++) {
        Reviewer reviewer = new Reviewer(scanner.nextInt(), 
            scanner.nextDouble(), scanner.nextDouble());
        reviewers.add(reviewer);
      }
      //Initiate the tree
      tree = new DecisionTree(util_s, util_f, p_s, reviewers);
      
    } catch(FileNotFoundException e) {
      System.out.println("File not found exception");
    }
  }
  
  //FIXME:
  public DecisionTree getTree() {
    return tree;
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Invalid argument! Please provide valid input path");
      return;
    }

    MainDriver driver = new MainDriver(args[0]);
    driver.tree.initiateDecisionTree();
  }
}