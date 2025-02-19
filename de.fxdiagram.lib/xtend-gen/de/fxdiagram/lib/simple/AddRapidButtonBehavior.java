package de.fxdiagram.lib.simple;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRapidButtonAction;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.lib.simple.SimpleNode;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import de.fxdiagram.lib.tools.CubeChooser;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRapidButtonBehavior<T extends XShape> extends AbstractHostBehavior<T> {
  private List<XRapidButton> rapidButtons;
  
  private Procedure1<? super AbstractChooser> choiceInitializer;
  
  public AddRapidButtonBehavior(final T host) {
    super(host);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return AddRapidButtonBehavior.class;
  }
  
  public Procedure1<? super AbstractChooser> setChoiceInitializer(final Procedure1<? super AbstractChooser> choiceInitializer) {
    return this.choiceInitializer = choiceInitializer;
  }
  
  public void doActivate() {
    T _host = this.getHost();
    final XNode host = ((XNode) _host);
    final XRapidButtonAction _function = new XRapidButtonAction() {
      @Override
      public void perform(final XRapidButton button) {
        final SimpleNode target = new SimpleNode("New Node");
        final XNode source = button.getHost();
        final XConnection connection = new XConnection(source, target);
        XRapidButton.Placer _placer = button.getPlacer();
        double _xPos = _placer.getXPos();
        double _minus = (_xPos - 0.5);
        double _multiply = (200 * _minus);
        double _layoutX = source.getLayoutX();
        double _plus = (_multiply + _layoutX);
        target.setLayoutX(_plus);
        XRapidButton.Placer _placer_1 = button.getPlacer();
        double _yPos = _placer_1.getYPos();
        double _minus_1 = (_yPos - 0.5);
        double _multiply_1 = (150 * _minus_1);
        double _layoutY = source.getLayoutY();
        double _plus_1 = (_multiply_1 + _layoutY);
        target.setLayoutY(_plus_1);
        XRoot _root = CoreExtensions.getRoot(host);
        CommandStack _commandStack = _root.getCommandStack();
        XDiagram _diagram = CoreExtensions.getDiagram(host);
        AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(_diagram, target, connection);
        _commandStack.execute(_newAddCommand);
      }
    };
    final XRapidButtonAction addAction = _function;
    final XRapidButtonAction _function_1 = new XRapidButtonAction() {
      @Override
      public void perform(final XRapidButton button) {
        Pos _chooserPosition = button.getChooserPosition();
        final CarusselChooser chooser = new CarusselChooser(host, _chooserPosition);
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = CoreExtensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final XRapidButtonAction chooseAction = _function_1;
    final XRapidButtonAction _function_2 = new XRapidButtonAction() {
      @Override
      public void perform(final XRapidButton button) {
        Pos _chooserPosition = button.getChooserPosition();
        final CubeChooser chooser = new CubeChooser(host, _chooserPosition);
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = CoreExtensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final XRapidButtonAction cubeChooseAction = _function_2;
    final XRapidButtonAction _function_3 = new XRapidButtonAction() {
      @Override
      public void perform(final XRapidButton button) {
        Pos _chooserPosition = button.getChooserPosition();
        final CoverFlowChooser chooser = new CoverFlowChooser(host, _chooserPosition);
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = CoreExtensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final XRapidButtonAction coverFlowChooseAction = _function_3;
    SVGPath _filledTriangle = ButtonExtensions.getFilledTriangle(Side.TOP, "Add node");
    XRapidButton _xRapidButton = new XRapidButton(host, 0.5, 0, _filledTriangle, cubeChooseAction);
    SVGPath _filledTriangle_1 = ButtonExtensions.getFilledTriangle(Side.BOTTOM, "Add node");
    XRapidButton _xRapidButton_1 = new XRapidButton(host, 0.5, 1, _filledTriangle_1, coverFlowChooseAction);
    SVGPath _filledTriangle_2 = ButtonExtensions.getFilledTriangle(Side.LEFT, "Add node");
    XRapidButton _xRapidButton_2 = new XRapidButton(host, 0, 0.5, _filledTriangle_2, chooseAction);
    SVGPath _filledTriangle_3 = ButtonExtensions.getFilledTriangle(Side.RIGHT, "Add node");
    XRapidButton _xRapidButton_3 = new XRapidButton(host, 1, 0.5, _filledTriangle_3, addAction);
    this.rapidButtons = Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
    XDiagram _diagram = CoreExtensions.getDiagram(host);
    ObservableList<XRapidButton> _buttons = _diagram.getButtons();
    Iterables.<XRapidButton>addAll(_buttons, this.rapidButtons);
  }
  
  protected void addChoices(final AbstractChooser chooser) {
    this.choiceInitializer.apply(chooser);
  }
}
