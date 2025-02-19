package de.fxdiagram.core.command;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.util.Duration;

@SuppressWarnings("all")
public class CommandContext {
  private XRoot root;
  
  private AnimationQueue animationQueue = new AnimationQueue();
  
  private Duration defaultExecuteDuration = DurationExtensions.millis(1000);
  
  private Duration defaultUndoDuration = DurationExtensions.millis(150);
  
  public CommandContext(final XRoot root) {
    this.root = root;
  }
  
  public XRoot getRoot() {
    return this.root;
  }
  
  public AnimationQueue getAnimationQueue() {
    return this.animationQueue;
  }
  
  public Duration getDefaultExecuteDuration() {
    return this.defaultExecuteDuration;
  }
  
  public Duration getDefaultUndoDuration() {
    return this.defaultUndoDuration;
  }
}
