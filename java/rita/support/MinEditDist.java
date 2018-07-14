package rita.support;

/**
 * Minimum-Edit-Distance (or Levenshtein distance) is a measure of the similarity 
 * between two strings, the source string and the target string (t). The distance 
 * is the number of deletions, insertions, or substitutions required to transform 
 * the source into the target / avg_string_length<p> 
 * 
 * Adapted from Michael Gilleland's algorithm
 */
public class MinEditDist
{       
  /**
   * Compute min-edit-distance between 2 strings divided by their average length.
   */ 
  public float computeAdjusted(String source, String target)
  {
    float denominator = Math.max(source.length(), target.length());
    return computeRaw(source, target) / denominator;
  }
  
  /**
   * Compute min-edit-distance between 2 strings
   * @see MinEditDist#computeAdjusted(java.lang.String,java.lang.String)
   */ 
  public int computeRaw(String source, String target)
  {
    
    int matrix[][]; // matrix
    char sI; // ith character of s
    char tJ; // jth character of t
    int cost; // cost

    // Step 1 ----------------------------------------------
    int sourceLength = source.length();
    int targetLength = target.length();
    
    if (sourceLength == 0) return targetLength;
    
    if (targetLength == 0) return sourceLength;
    
    matrix = new int[sourceLength + 1][targetLength + 1];

    // Step 2 ----------------------------------------------

    for (int i = 0; i <= sourceLength; i++)
      matrix[i][0] = i;

    for (int j = 0; j <= targetLength; j++)    
      matrix[0][j] = j;

    // Step 3 ----------------------------------------------

    for (int i = 1; i <= sourceLength; i++)
    {

      sI = source.charAt(i - 1);

      // Step 4 --------------------------------------------

      for (int j = 1; j <= targetLength; j++)
      {
        tJ = target.charAt(j - 1);

        // Step 5 ------------------------------------------

        cost = (sI == tJ) ? 0 : 1;
        
        // Step 6 ------------------------------------------
        matrix[i][j] = min (matrix[i - 1][j] + 1, 
                            matrix[i][j - 1] + 1, 
                            matrix[i - 1][j - 1] + cost);
      }
    }

    // Step 7 ----------------------------------------------
    
    return matrix[sourceLength][targetLength];
  }
  
  /**
   * Computes min-edit-distance between 2 string arrays
   * where each array element either matches or does not
   * divided by their average (array) length.
   */ 
  public float computeAdjusted(String[] source, String[] target)
  {
    float denominator = Math.max(source.length, target.length);
    return computeRaw(source, target) / denominator;
  }
  
  /**
   * Computes min-edit-distance between 2 string arrays
   * where each array element either matches or does not.
   */ 
  public int computeRaw(String[] source, String[] target)
  {
    return  compute(source, target);
  }
  
  /*
   * Computes min-edit-distance between 2 string arrays
   * where each array element either matches or does not
   */ 
  protected int compute(String[] src, String[] target)
  {
    //System.out.println(Arrays.asList(source)+" "+Arrays.asList(target));
    int matrix[][]; // matrix
    String sI; // ith element of s
    String tJ; // jth element of t
    int cost; // cost

    // Step 1 ----------------------------------------------
    
    if ((src == null || src.length == 0) && (target == null || target.length == 0)) 
      return -1; // no inputs
    
    if (src == null || src.length == 0) return target.length;
    
    if (target == null || target.length == 0) return src.length;
    
    matrix = new int[src.length + 1][target.length + 1];

    // Step 2 ----------------------------------------------

    for (int i = 0; i <= src.length; i++)
      matrix[i][0] = i;

    for (int j = 0; j <= target.length; j++)    
      matrix[0][j] = j;

    // Step 3 ----------------------------------------------

    //String[] src = RiFreeTTSEngine.cleanPhonemes(source);    
    for (int i = 1; i <= src.length; i++)
    {
      sI = src[i - 1];
      
      // CMU Specific -- should not be here!
      //sI = stripStressMarks(sI);

      // Step 4 --------------------------------------------

      for (int j = 1; j <= target.length; j++)
      {
        tJ = target[j - 1];

        // Step 5 ------------------------------------------

        cost = (sI.equals(tJ)) ? 0 : 1;
        
        // Step 6 ------------------------------------------
        matrix[i][j] = min (matrix[i - 1][j] + 1, 
                            matrix[i][j - 1] + 1, 
                            matrix[i - 1][j - 1] + cost);
      }
    }

    // Step 7 ----------------------------------------------
    
    return matrix[src.length][target.length];
  }
  

  /*protected String stripStressMarks(String sI)
  {
    if (sI.charAt(sI.length()-1) == '1') {
      sI = sI.substring(0, sI.length()-1);
      //System.out.println(sI);
    }
    return sI;
  }*/
  
  private int min(int a, int b, int c)
  {
    int min = a;
    if (b < min) min = b;
    if (c < min) min = c;
    return min;
  }
  
  public static void main(String[] args)
  {
    MinEditDist MinEditDist = new MinEditDist();
    
/*    String a = "dogness";
    String b = "cat";
    System.out.println(MinEditDist.computeAdjusted(a.split("."),b.split(".")));
    
    a = "daxt";
    b = "cat";
    System.out.println(MinEditDist.computeAdjusted(a.split("."),b.split(".")));
    
    a = "casdf";
    b = "casdfat";
    System.out.println(MinEditDist.computeAdjusted(a.split("."),b.split(".")));
    
    a = "caadsfrt";
    b = "c";
    System.out.println(MinEditDist.computeAdjusted(a.split("."),b.split(".")));*/
    /*String[] ps = {"r", "iy", "g", "r", "ehj", "hy", "s"};
    RiPhrase p = new RiPhrase(null, "regress");
    String[] target = p.getPhonemeArray();
    System.out.println(Arrays.asList(target));
    MinEditDist med = new MinEditDist();
    int min = med.compute(ps, target);
    System.out.println("MED: "+min); 
    
    ps = med.stripStressMarks(ps);
    for (int i = 0; i < ps.length; i++)
      System.out.println(ps[i]);*/
    
     
  }

}// end
