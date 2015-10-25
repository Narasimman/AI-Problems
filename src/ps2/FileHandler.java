package ps2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHandler {
  private File inputFile;
  private PropositionSet prop = new PropositionSet();
  
  FileHandler(String filePath) {
    inputFile = new File(filePath);
  }
  
  /**
   * Read the input file from the path and construct 
   * necessary data structures for the DP algorithm
   * @throws IOException
   */
  void read() throws IOException{
    FileInputStream fstream = new FileInputStream(inputFile);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    
    String line;
    int i = 0;
    
    while((line = br.readLine()) != null) {
      String[] tokens = line.split(" ");
      Clause clause = new Clause(i);
      for (String token : tokens) {
        Literal literal = new Literal(Integer.parseInt(token));
        clause.addLiteral(literal);
        prop.addAtom(literal);
      }
      prop.addClause(clause);
      
      i++;
    }
    
    br.close();
  }
  
  PropositionSet getPropositionSet() {
    return this.prop;
  }

}
