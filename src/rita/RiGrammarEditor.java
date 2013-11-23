package rita;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import rita.support.GrammarIF;
import rita.support.RiEditorWindow;

/**
 * Provides a live edit-able view of a RiGrammar text file
 * that can be dynamically loaded into a sketch without
 * stopping and restarting it. Only one additional line
 * is needed:<pre> 
    RiGrammar rg = new RiGrammar(this, "mygrammar.g");
    rg.openGrammarEditor();  // add this line
    println(rg.expand());</pre>
 *   
 * 
 * @invisible
 */
public class RiGrammarEditor extends RiEditorWindow {
 
  /** @invisible */
  public GrammarIF rg;

  public RiGrammarEditor(final RiGrammar grammar) {
    this(grammar, 100, 100, 600, 600);
  }

  private RiGrammarEditor(final RiGrammar grammar, int x, int y, int width, int height)
  {
    super("RiGrammarEditor", x, y, width, height);
    this.rg = grammar;   
    if (grammar.fileName != null) {
      String contents = loadFileByName(null, grammar.fileName);
      rg.load(contents);
      System.out.println(rg.getGrammar());
    }
  }

  public void addButtons(JToolBar jtbToolBar) {
    
    super.addButtons(jtbToolBar);
    
    // add refresh button
    JButton jbnToolbarButtons = new JButton("refresh");
    jbnToolbarButtons.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        displayInTextArea("refresh");
        rg.load(textArea.getText());
      }
    });
    jtbToolBar.add(jbnToolbarButtons);

    jtbToolBar.addSeparator();
  }


}// end