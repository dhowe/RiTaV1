package rita.test;

import static rita.support.Constants.EventType.ColorTo;
import static rita.support.Constants.EventType.FadeIn;
import static rita.support.Constants.EventType.FadeOut;
import static rita.support.Constants.EventType.TextEntered;
import static rita.support.Constants.EventType.TextTo;
import static rita.support.Constants.EventType.Timer;
import static rita.support.Constants.EventType.Unknown;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;

import org.junit.Test;

import rita.RiTaEvent;

public class RiTaEventTest {

  @Test
  public void testdata() {
    equal(new RiTaEvent(this).data(), null);
    equal(new RiTaEvent(this, TextTo).data(), null);
    equal(new RiTaEvent(this, null, this).data(), this);
    equal(new RiTaEvent(this, TextTo, this).data(), this);
    equal(new RiTaEvent(this, ColorTo, this).data(), this);
    equal(new RiTaEvent(this, FadeOut, this).data(), this);
  }

  @Test
  public void testIsType() {
    equal(new RiTaEvent(this).isType(Unknown), true);
    equal(new RiTaEvent(this, TextTo).isType(TextTo), true);
    equal(new RiTaEvent(this, ColorTo).isType(ColorTo), true);
    equal(new RiTaEvent(this, FadeOut).isType(ColorTo), false);
  }
  
  @Test
  public void testIsTypeString() {
    equal(new RiTaEvent(this).isType(Unknown), true);
    equal(new RiTaEvent(this, TextTo).isType("TextTo"), true);
    equal(new RiTaEvent(this, ColorTo).isType("ColorTo"), true);
    equal(new RiTaEvent(this, FadeOut).isType("ColorTo"), false);
  }
  

  @Test
  public void testRiTaEvent() {
    ok(new RiTaEvent(this));
    ok(new RiTaEvent(this, Timer));
  }

  @Test
  public void testSource() {
    ok(new RiTaEvent(this).source());
    ok(new RiTaEvent(this).source());
    equal(new RiTaEvent(this, Timer).source(), this);
    equal(new RiTaEvent(this, TextEntered).source(), this);
    Object o = new Object();
    equal(new RiTaEvent(o, FadeIn).source(), o);
  }

  @Test
  public void testToString() {
    // System.out.println(new RiTaEvent(this,TEXT_ENTERED).toString());
    ok(new RiTaEvent(this, Timer).toString().contains("Timer"));

    ok(new RiTaEvent(this, TextEntered).toString().contains("TextEntered"));
    ok(new RiTaEvent(this, FadeOut).toString().contains("FadeOut"));
  }

  @Test
  public void testType() {
    ok(new RiTaEvent(this).type());
    ok(new RiTaEvent(this).type());
    ok(new RiTaEvent(this, FadeOut).type());
    ok(new RiTaEvent(this, Timer).type());

    equal((new RiTaEvent(this, TextEntered)).type(), "TextEntered");
    equal(new RiTaEvent(this, FadeIn).type(), "FadeIn");
    equal(new RiTaEvent(this, Timer).type(), "Timer");
  }

}
