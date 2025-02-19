package de.fxdiagram.examples.slides;

import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.examples.slides.SlideDiagram;

@SuppressWarnings("all")
public class SlideNavigation extends AbstractHostBehavior<SlideDiagram> implements NavigationBehavior {
  public SlideNavigation(final SlideDiagram host) {
    super(host);
  }
  
  protected void doActivate() {
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return NavigationBehavior.class;
  }
  
  public boolean next() {
    SlideDiagram _host = this.getHost();
    return _host.next();
  }
  
  public boolean previous() {
    SlideDiagram _host = this.getHost();
    return _host.previous();
  }
}
