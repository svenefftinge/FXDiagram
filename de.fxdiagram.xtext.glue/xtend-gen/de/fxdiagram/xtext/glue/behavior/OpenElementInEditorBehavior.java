package de.fxdiagram.xtext.glue.behavior;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.OpenBehavior;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class OpenElementInEditorBehavior extends AbstractHostBehavior<XShape> implements OpenBehavior {
  public OpenElementInEditorBehavior(final XShape host) {
    super(host);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return OpenBehavior.class;
  }
  
  protected void doActivate() {
    XShape _host = this.getHost();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        int _clickCount = it.getClickCount();
        boolean _equals = (_clickCount == 2);
        if (_equals) {
          OpenElementInEditorBehavior.this.open();
        }
      }
    };
    _host.<MouseEvent>addEventHandler(MouseEvent.MOUSE_CLICKED, _function);
  }
  
  public void open() {
    XtextDomainObjectDescriptor<?> _domainObject = this.getDomainObject();
    _domainObject.openInEditor(true);
  }
  
  private XtextDomainObjectDescriptor<?> getDomainObject() {
    DomainObjectDescriptor _switchResult = null;
    XShape _host = this.getHost();
    final XShape it = _host;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof XNode) {
        _matched=true;
        _switchResult = ((XNode)it).getDomainObject();
      }
    }
    if (!_matched) {
      if (it instanceof XConnection) {
        _matched=true;
        _switchResult = ((XConnection)it).getDomainObject();
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return ((XtextDomainObjectDescriptor<?>) _switchResult);
  }
}
