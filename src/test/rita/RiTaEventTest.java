package test.rita;

import static test.QUnitStubs.equal;
import static test.QUnitStubs.ok;

import org.junit.Test;

import rita.RiTa;
import rita.RiTaEvent;

public class RiTaEventTest
{
  @Test
  public void testRiTaEvent()
  {
    ok(new RiTaEvent(this));
    ok(new RiTaEvent(this, RiTa.TIMER));
  }

  @Test
  public void testSource()
  {
    ok(new RiTaEvent(this).source());
    ok(new RiTaEvent(this).source());
    equal(new RiTaEvent(this, RiTa.TIMER).source(),this);
    equal(new RiTaEvent(this, RiTa.TEXT_ENTERED).source(),this);
    Object o = new Object();
    equal(new RiTaEvent(o, RiTa.FADE_IN).source(),o);
  }
  
  @Test
  public void testToString()
  {
    //System.out.println(new RiTaEvent(this, RiTa.TEXT_ENTERED).toString());
    ok(new RiTaEvent(this, RiTa.TIMER).toString().contains("timer".toUpperCase()));
    ok(new RiTaEvent(this, RiTa.TEXT_ENTERED).toString().contains("text_entered".toUpperCase()));
    ok(new RiTaEvent(this, RiTa.FADE_OUT).toString().contains("fade_out".toUpperCase()));
  }

  @Test
  public void testType()
  {
    ok(new RiTaEvent(this).type());
    ok(new RiTaEvent(this).type());
    ok(new RiTaEvent(this, RiTa.FADE_OUT).type());
    ok(new RiTaEvent(this, RiTa.TIMER).type());
    equal(new RiTaEvent(this, RiTa.TIMER).type(),RiTa.TIMER);
    equal(new RiTaEvent(this, RiTa.TEXT_ENTERED).type(),RiTa.TEXT_ENTERED);
    equal(new RiTaEvent(this, RiTa.FADE_IN).type(),RiTa.FADE_IN);
  }

}
