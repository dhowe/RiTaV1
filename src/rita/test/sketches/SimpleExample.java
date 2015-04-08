package rita.test.sketches;

import rita.*;

public class SimpleExample {
  
  public static void main(String[] args) {
    
    RiString rs = new RiString("The elephant took a bite!");
    System.out.println(rs.features());
  }
}
