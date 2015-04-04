package rita.render;

import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import processing.core.PApplet;
import rita.RiGrammar;
import rita.RiTa;

/**
 * Provides a live edit-able view of a RiGrammar text file
 * that can be dynamically loaded into a sketch without
 * stopping and restarting it. Only one additional line
 * is needed:<pre> 
    RiGrammar rg = new RiGrammar(this, "mygrammar.json");
    rg.openGrammarEditor();  // add this line
    println(rg.expand());</pre>
 *   
 * 
 * @invisible
 */
public class RiGrammarEditor extends RiEditorWindow {
 
  public static String DEFAULT_GRAMMAR = "{\n  \"<start>\" : \"hello world!\"\n}";

  /** @invisible */
  public RiGrammar rg;  

  public RiGrammarEditor(final RiGrammar grammar) {
    this(grammar, 100, 100, 600, 600);
  }

  public RiGrammarEditor(final RiGrammar grammar, int x, int y, int width, int height)
  {
    super("RiGrammarEditor", x, y, width, height);
    
    this.rg = grammar;   
    if (grammar.parent != null)
      this.parent = (PApplet) grammar.parent;
    
    String gramStr = DEFAULT_GRAMMAR;
    
    if (grammar.grammarUrl != null) {
      
      if (grammar.grammarUrl instanceof URL)
        gramStr = RiTa.loadString((URL) grammar.grammarUrl);
      
      else if (grammar.grammarUrl instanceof String)
        gramStr = RiTa.loadString((String) grammar.grammarUrl, grammar.parent);
    }

    String title = getTitle();
    if (grammar.grammarUrl != null)
      title += ": " + grammar.grammarUrl;
    setTitle(title);
    
    setText(gramStr);
    if (rg._rules.size() < 1) // use the editor's default 
      rg.load(gramStr);
    
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent ev) {
        
        checkForUnsavedEdits();
      }
    });
    
    this.dirty = false;
  }
  
  public static String jsonToString(String filename) {
    
    return jsonToString(filename, null);
  }
  
  public static String jsonToString(String filename, Object parent) {
    
    String s = RiTa.loadString("myfile.json", parent);
    s = s.replaceAll("([\\r\\n]*\\s)*", ""); // remove line-break
    s = s.replaceAll("\"", "\\\\\"");       // escape all quotes
    s = "String grammar = \"" + s + "\";"; // add header & footer
    return s;
  }

  protected void checkForUnsavedEdits()
  {
    //System.out.println("RiGrammarEditor.checkForUnsavedEdits()");
    
    if (this.dirty) {
      
      int option = JOptionPane.showConfirmDialog(null, "Do you want to save your changes?");
      if (option == JOptionPane.YES_OPTION)
      {
        if (writeFile() == JFileChooser.APPROVE_OPTION)
            this.setVisible(false);
      }
      else if (option == JOptionPane.NO_OPTION)
        this.setVisible(false);
    }
    else 
      this.setVisible(false);
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