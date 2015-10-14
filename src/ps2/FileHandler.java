package ps2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHandler {
  private File inputFile;
  
  public FileHandler(String filePath) {
    inputFile = new File(filePath);
  }
  
  public void read() throws IOException{
    FileInputStream fstream = new FileInputStream(inputFile);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    
    String line;
    while((line = br.readLine()) != null) {
      String[] tokens = line.split(" ");
      for (String token : tokens) {
        System.out.println(token);
        Literal literal = new Literal(Integer.parseInt(token));
        
      }
      
    }    
  }

}
