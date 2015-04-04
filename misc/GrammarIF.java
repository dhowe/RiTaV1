package rita.support;

import java.io.PrintStream;

import rita.RiGrammarEditor;

public interface GrammarIF
{
  public static final String PROB_PATTERN = "(.*[^ ]) *\\[([^]]+)\\](.*)", START = "<start>";
  public static final String EXEC_CHAR = "`", EXEC_POST = ")" + EXEC_CHAR;

  public RiGrammarEditor openEditor();

  public RiGrammarEditor openEditor(int width, int height);

  public GrammarIF loadFromFile(String grammarFileName);

  public GrammarIF addRule(String name, String rule);

  public GrammarIF addRule(String key, String rule, float prob);

  public String expand();

  public String expandFrom(String rule);

  public String expandWith(String literalString, String ruleName);

  public String getGrammar();

  public boolean hasRule(String name);

  public GrammarIF load(String grammarRulesAsString);

  public GrammarIF removeRule(String s);

  public GrammarIF print();

  public GrammarIF print(PrintStream ps);

  public GrammarIF reset();

}