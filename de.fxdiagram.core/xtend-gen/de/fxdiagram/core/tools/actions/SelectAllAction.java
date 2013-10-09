package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.ForeachExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectAllAction implements DiagramAction {
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    Iterable<XShape> _allShapes = _diagram.getAllShapes();
    final Procedure1<XShape> _function = new Procedure1<XShape>() {
      public void apply(final XShape it) {
        boolean _isSelectable = it.isSelectable();
        if (_isSelectable) {
          it.setSelected(true);
        }
      }
    };
    ForeachExtensions.<XShape>forEachExt(_allShapes, _function);
  }
}
