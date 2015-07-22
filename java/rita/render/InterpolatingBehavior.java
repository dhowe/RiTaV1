package rita.render;

import rita.RiText;

public abstract class InterpolatingBehavior extends RiTextBehavior
{
  protected Interpolater interpolater;
  protected float[] targets;

  public InterpolatingBehavior(RiText text, float startOffsetInSec, float durationInSeconds)
  {
    super(text, startOffsetInSec, durationInSeconds);
  }

  public void setMotionType(int motionType)
  {
    interpolater.setMotionType(motionType);
  }

  public void update()
  {
    if (duration <= 0 || completed || isPaused())
    {
      // System.out.println("aborting... dur="+duration+" comp="+completed+" paused="+paused);
      return;
    }

    if (interpolater.update()) // true if running
    {
      if (!initd)
      {
        initd = true; // do once
        getStartValueFromParent(rt, interpolater);
      }
      updateParentValues(rt, interpolater.getValues());
    }
    checkForCompletion();
  }

  public abstract void updateParentValues(RiText rt, float[] values);

  public abstract void getStartValueFromParent(RiText rt, Interpolater interpolater);

  public float[] getTarget()
  {
    return interpolater.getTarget();
  }

  public void finish()
  {
    if (interpolater != null)
      interpolater.finish();
    completed = true;
  }

  public void stop()
  {
    // System.out.println("InterpolatingBehavior.stop()");
    if (interpolater != null)
      interpolater.stop();
    running = false;
    duration = -1;
  }

  public void checkForCompletion()
  {
    // System.out.println("InterpolatingBehavior.checkForCompletion()");
    this.completed = interpolater.isCompleted();
    
    // if (completed) System.out.println("checkForCompletion.completed()");
    if (running && completed && !isPaused())
      fireCallback();
  }

  public void reset(float durationSec)
  {
    super.reset(durationSec); // reset duration, leaving state (current/target)
                              // the same
    interpolater.reset(interpolater.getValues(), interpolater.getTarget(), 0, (int) (durationSec * 1000));
  }

  public void resetTarget(float[] start, float[] target, float startOffsetSec, float durationSec)
  {
    // System.out.println("InterpolatingBehavior.resetTarget("+RiTa.asList(start)
    // + ", " + RiTa.asList(target)+", "+startOffsetSec+", "+durationSec+")");
    super.reset(durationSec);
    this.interpolater.reset(start, target, toOffsetMs(startOffsetSec), (int) (durationSec * 1000));
  }

}// end
