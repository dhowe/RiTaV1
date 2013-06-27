package rita.support;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import processing.core.PApplet;
import rita.RiTa;

public class RiEditorWindow extends JFrame {

  protected static ImageIcon save, open;
  public JTextArea textArea;
  //protected PApplet parent;
  
  static {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.err.println("[WARN] " + e.getMessage());
    }
  }

  public RiEditorWindow
    (String title, /*PApplet p,*/ int xPos, int yPos, int width, int height)
  {
    //this.parent = p;
    this.textArea = new JTextArea(6, 20);

    JToolBar jtbMainToolbar = new JToolBar();
    jtbMainToolbar.setFloatable(false);
    addButtons(jtbMainToolbar);

    JScrollPane scroller = new JScrollPane(textArea);
    JPanel content = new JPanel();

    content.setLayout(new BorderLayout());
    content.add(jtbMainToolbar, BorderLayout.NORTH);
    content.add(scroller, BorderLayout.CENTER);
    setContentPane(content);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setTitle(title);
    pack();
    setBounds(xPos, yPos, width, height);
    setSize(width, height + 39);
    setVisible(true);
  }

  public void openFile() {
    final JFileChooser fc = new JFileChooser();
    String dataDir = ".";//(parent == null) ? "." : parent.dataPath("");
    //System.out.println("RiEditorWindow.openFile.data="+dataDir);
    if (!setDirectory(fc, dataDir)) {
      if (!setDirectory(fc, "data"))
        setDirectory(fc, ".");    
    }
    int rv = fc.showOpenDialog(this);
    if (rv == JFileChooser.APPROVE_OPTION) {
      String fileName = fc.getSelectedFile().toString();
      loadFileByName(null, fileName);
    }
  }

  private boolean setDirectory(final JFileChooser fc, String dirName) {
    File dir = new File(dirName);
    if (dir == null) return false;
    fc.setCurrentDirectory(dir);//parent.dataPath("")));
    File cd = fc.getCurrentDirectory();
    if (cd == null) return false;
    return (cd.getAbsolutePath().equals(dir.getAbsolutePath()));
  }

  protected String loadFileByName(PApplet p, String fname) {
    
    System.out.println(fname + ": " + p);
    String[] contents = RiTa.loadStrings(fname);
    String gramStr = join(contents, "\n");

    textArea.setText(gramStr);
    textArea.setCaretPosition(0);

    String name = fname;
    int idx = fname.lastIndexOf(OS_SLASH);
    if (idx > -1 && fname.length() > idx)
      name = fname.substring(idx + 1);
    //System.out.println("[INFO] Opened: " + fname + " as " + name);
    setTitle(getTitle() + ": " + name);

    return gramStr;
  }
  
  /** @invisible */
  public static String OS_SLASH="/";
  static {
    try {
      OS_SLASH = System.getProperty("file.separator");
    } 
    catch (Throwable e) {
      OS_SLASH = "/";
    }    
  }
  
  private static String join(String[] full, String delim)
  {
    StringBuilder result = new StringBuilder();
    if (full != null) {
      for (int index = 0; index < full.length; index++) {
        if (index == full.length - 1)
          result.append(full[index]);
        else
          result.append(full[index] + delim);
      }
    }
    return result.toString();
  }

  public void addButtons(JToolBar jtbToolBar) 
  {
    JButton jbnToolbarButtons = null;

    if (open == null)
      open = new ImageIcon(getClass().getResource("open.gif"));
    jbnToolbarButtons = new JButton(open);
    jbnToolbarButtons.setToolTipText("open");
    jbnToolbarButtons.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openFile();
      }
    });
    jtbToolBar.add(jbnToolbarButtons);

    if (save == null)
      save = new ImageIcon(getClass().getResource("save.gif"));
    jbnToolbarButtons = new JButton(save);
    jbnToolbarButtons.setToolTipText("save");
    jbnToolbarButtons.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        displayInTextArea("save");
        writeFile();
      }
    });
    jtbToolBar.add(jbnToolbarButtons);

    // We can add separators to group similar components
    jtbToolBar.addSeparator();
  }

  protected void writeFile() {
    final JFileChooser chooser = new JFileChooser();
    
    String dataDir = ".";//(parent == null) ? "." : parent.dataPath("");
    //System.out.println("RiEditorWindow.writeFile.data="+dataDir);
    try {
      chooser.setCurrentDirectory(new File(dataDir));
    }
    catch (Exception e1)
    {
      try {
        chooser.setCurrentDirectory(new File("../src/data"));
      } catch (Exception e) {
        chooser.setCurrentDirectory(new File("."));
      }
    }
    int retVal = chooser.showSaveDialog(this);

    if (retVal == JFileChooser.APPROVE_OPTION) {
      try {
        FileWriter fWriter = new FileWriter(chooser.getSelectedFile());
        BufferedWriter writer = new BufferedWriter(fWriter);
        String writeString = textArea.getText();

        writer.write(writeString, 0, writeString.length());
        writer.close();
      } catch (Exception e) {
        System.err.println("[WARN] " + e.getMessage());
      }
    }
  }

  // for testing...
  protected void displayInTextArea(String actionDescription) {
    // textArea.append(actionDescription + newline);
    // System.out.println(actionDescription);
  }
}
