package rita.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rita.RiTa;
import rita.RiTaException;

public class MacSpeechEngine { 

  /** The default mac tts voice sets */
  public final static String[] AVAILABLE_VOICES = {
    "Victoria", "Agnes", "Kathy", "Princess", "Vicki", 
    "Bruce", "Fred", "Junior", "Ralph", "Albert",
    "Bad News", "Bahh", "Bells", "Boing", "Bubbles", 
    "Cellos", "Deranged", "Good News", "Hysterical", 
    "Pipe Organ", "Trinoids", "Whisper", "Zarvox",
  };

  public final static String DEFAULT_VOICE = AVAILABLE_VOICES[0];
  
  protected String voiceName;
  private List speakThreads;
  
  public MacSpeechEngine() {
    this(null);
  }

  public MacSpeechEngine(String voiceName) {
    this.voiceName = voiceName;
    if (voiceName == null)
      this.voiceName = DEFAULT_VOICE;
  }

  public void dispose() {    
    if (speakThreads != null) {
      for (Iterator i = speakThreads.iterator(); i.hasNext();) {
        TTSThread t = (TTSThread) i.next();
        if (t != null) t.dispose();
        t = null;     
      }
      speakThreads.clear();
    }
  }

  public String voiceToString(String name)
  {
    String br = System.getProperty("line.separator");
    String info = br+"Name: " +name + br
     + "\tDescription: Default MacTTS voice" + br
     + "\tOrganization: apple" + br
     + "\tDomain: general" + br
     + "\tLocale: en_US " + br    
     + "\tStyle: standard" + br;
    return info;
  }
  
  public String[] getVoiceDescriptions() {
    String[] descs = new String[AVAILABLE_VOICES.length];
    for (int i = 0; i < descs.length; i++) 
      descs[i] = voiceToString(AVAILABLE_VOICES[i]);    
    return descs;
  }

  public String voice() {
    return voiceName;
  }

  public void voice(String voiceName) {
    this.voiceName = voiceName;
  }

  class TTSThread extends Thread {
    private boolean running;
    private Process p = null;
    private String voice, text;
     
    public TTSThread(String text, String voice) {
      this.voice = voice;
      this.text = text;
      this.running = true;
    }    
    
    public void dispose() {
      
      try {
        if (p != null) p.destroy();
      } catch (Throwable e) {
        // ignore
      }
    }

    public void run() {      
      
      if (!running) return;     
      
      String [] args = { "say", "-v" + voice, text };
      try {
        p = Runtime.getRuntime().exec(args);
        p.waitFor();          
      } catch (Throwable e) {
        throw new RiTaException(e);
      }
      
      if (running) {
        int exitCode = 0;
        if (p != null)
          exitCode = p.exitValue();
        /*if (exitCode == 0) {
          parent.fireSpeechCompletedEvent(text);
        }*/
        else {
          if (exitCode != 143) {
            String err = getOutput(p, true);
            System.err.println(err+"\n[WARN] RiMacSpeechEngine aborting with exit-code: "+p.exitValue());
          }
        }
      }
    }
  }
  
  private static String getOutput(Process proc, boolean error) 
  {
    String line, s="";    
    BufferedReader br = null;
    try {       
      if (error)
        br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
      else
        br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      while ((line = br.readLine()) != null) {
        s += line+"\n";
        if (error) s = "[WARN] " + s;
      }
    } catch (Throwable e) {
      //throw new RiTaException(e);
      // ignore
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException e) {}
    }
    return s;
  } 

  public void speak(final String text) {    
    
    TTSThread t = new TTSThread(text, voiceName);
    if (speakThreads == null)
      speakThreads = new ArrayList();
    speakThreads.add(t);
    t.start();
  }
  
  public void stop() {
    this.dispose();
  }

  public String[] getVoiceNames() {
    return AVAILABLE_VOICES;
  }

  public String getVoiceDescription() {
    return voiceToString(voiceName);
  } 

   
  public static String[] querySystemForVoices() {
    
    String [] cmd = { "/bin/ls","/System/Library/Speech/Voices/" };
    String[] voices = AVAILABLE_VOICES;     
    try {
      Process p = Runtime.getRuntime().exec(cmd);
      int rc = p.waitFor();
      if (rc == 0)  {
        String result = getOutput(p, false);
        voices = result.split("\\.SpeechVoice\n"); 
        Arrays.sort(voices, new Comparator() {
          public int compare(Object o1, Object o2) {
            return ((o1.equals("Victoria")) ? -1 : 1);
        }});
      }
      else {        
        System.err.println(getOutput(p, true));
      }       
    } catch (Throwable e) {
      throw new RiTaException(e);
    }  
    return voices;
  }

  public static void main(String[] args) {
    System.out.println(RiTa.asList(querySystemForVoices()));
    new MacSpeechEngine().speak("Dinner at the Y");
  }

}// end
