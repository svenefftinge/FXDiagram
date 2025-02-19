package de.fxdiagram.lib.simple;

import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.animation.Animation;
import javafx.util.Duration;

@SuppressWarnings("all")
public class OpenDiagramCommand implements AnimationCommand {
  private OpenableDiagramNode host;
  
  public OpenDiagramCommand(final OpenableDiagramNode host) {
    this.host = host;
  }
  
  public Animation getExecuteAnimation(final CommandContext context) {
    Duration _defaultExecuteDuration = context.getDefaultExecuteDuration();
    return this.host.openDiagram(_defaultExecuteDuration);
  }
  
  public Animation getUndoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.host.closeDiagram(_defaultUndoDuration);
  }
  
  public Animation getRedoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.host.openDiagram(_defaultUndoDuration);
  }
  
  public boolean clearRedoStackOnExecute() {
    return true;
  }
}
