package de.fxdiagram.core.layout;

import com.google.common.collect.Iterables;
import de.cau.cs.kieler.core.alg.BasicProgressMonitor;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KGraphData;
import de.cau.cs.kieler.core.kgraph.KGraphElement;
import de.cau.cs.kieler.core.kgraph.KGraphFactory;
import de.cau.cs.kieler.core.kgraph.KLabel;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.math.KVectorChain;
import de.cau.cs.kieler.kiml.AbstractLayoutProvider;
import de.cau.cs.kieler.kiml.graphviz.layouter.GraphvizLayoutProvider;
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout;
import de.cau.cs.kieler.kiml.klayoutdata.KInsets;
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory;
import de.cau.cs.kieler.kiml.klayoutdata.KPoint;
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout;
import de.cau.cs.kieler.kiml.options.EdgeLabelPlacement;
import de.cau.cs.kieler.kiml.options.EdgeRouting;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.MoveCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.layout.ConnectionMorphCommand;
import de.fxdiagram.core.layout.LayoutType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Layouter {
  @Extension
  private KLayoutDataFactory _kLayoutDataFactory = KLayoutDataFactory.eINSTANCE;
  
  @Extension
  private KGraphFactory _kGraphFactory = KGraphFactory.eINSTANCE;
  
  public Layouter() {
    AbstractLayoutProvider _layoutProvider = this.getLayoutProvider(LayoutType.DOT);
    _layoutProvider.dispose();
  }
  
  public LazyCommand createLayoutCommand(final LayoutType type, final XDiagram diagram, final Duration duration) {
    final LazyCommand _function = new LazyCommand() {
      @Override
      protected AbstractAnimationCommand createDelegate() {
        final HashMap<Object, KGraphElement> cache = CollectionLiterals.<Object, KGraphElement>newHashMap();
        diagram.layout();
        final KNode kRoot = Layouter.this.toKRootNode(diagram, cache);
        final AbstractLayoutProvider provider = Layouter.this.getLayoutProvider(type);
        try {
          BasicProgressMonitor _basicProgressMonitor = new BasicProgressMonitor();
          provider.doLayout(kRoot, _basicProgressMonitor);
          return Layouter.this.composeCommand(cache, duration);
        } finally {
          provider.dispose();
        }
      }
    };
    return _function;
  }
  
  protected AbstractLayoutProvider getLayoutProvider(final LayoutType type) {
    GraphvizLayoutProvider _graphvizLayoutProvider = new GraphvizLayoutProvider();
    final Procedure1<GraphvizLayoutProvider> _function = new Procedure1<GraphvizLayoutProvider>() {
      public void apply(final GraphvizLayoutProvider it) {
        String _string = type.toString();
        it.initialize(_string);
      }
    };
    return ObjectExtensions.<GraphvizLayoutProvider>operator_doubleArrow(_graphvizLayoutProvider, _function);
  }
  
  protected ParallelAnimationCommand composeCommand(final Map<Object, KGraphElement> map, final Duration duration) {
    final ParallelAnimationCommand composite = new ParallelAnimationCommand();
    Set<Map.Entry<Object, KGraphElement>> _entrySet = map.entrySet();
    for (final Map.Entry<Object, KGraphElement> entry : _entrySet) {
      {
        final Object xElement = entry.getKey();
        final KGraphElement kElement = entry.getValue();
        boolean _matched = false;
        if (!_matched) {
          if (xElement instanceof XNode) {
            _matched=true;
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            float _xpos = shapeLayout.getXpos();
            Bounds _layoutBounds = ((XNode)xElement).getLayoutBounds();
            double _minX = _layoutBounds.getMinX();
            double _minus = (_xpos - _minX);
            float _ypos = shapeLayout.getYpos();
            Bounds _layoutBounds_1 = ((XNode)xElement).getLayoutBounds();
            double _minY = _layoutBounds_1.getMinY();
            double _minus_1 = (_ypos - _minY);
            MoveCommand _moveCommand = new MoveCommand(((XShape)xElement), _minus, _minus_1);
            final Procedure1<MoveCommand> _function = new Procedure1<MoveCommand>() {
              public void apply(final MoveCommand it) {
                it.setExecuteDuration(duration);
              }
            };
            MoveCommand _doubleArrow = ObjectExtensions.<MoveCommand>operator_doubleArrow(_moveCommand, _function);
            composite.operator_add(_doubleArrow);
          }
        }
        if (!_matched) {
          if (xElement instanceof XConnection) {
            _matched=true;
            EList<KGraphData> _data = kElement.getData();
            Iterable<KEdgeLayout> _filter = Iterables.<KEdgeLayout>filter(_data, KEdgeLayout.class);
            final KEdgeLayout edgeLayout = IterableExtensions.<KEdgeLayout>head(_filter);
            final KVectorChain layoutPoints = edgeLayout.createVectorChain();
            XConnection.Kind _switchResult_1 = null;
            EdgeRouting _property = edgeLayout.<EdgeRouting>getProperty(LayoutOptions.EDGE_ROUTING);
            if (_property != null) {
              switch (_property) {
                case SPLINES:
                  XConnection.Kind _xifexpression = null;
                  int _size = layoutPoints.size();
                  int _minus = (_size - 1);
                  int _modulo = (_minus % 3);
                  boolean _equals = (_modulo == 0);
                  if (_equals) {
                    _xifexpression = XConnection.Kind.CUBIC_CURVE;
                  } else {
                    XConnection.Kind _xifexpression_1 = null;
                    int _size_1 = layoutPoints.size();
                    int _minus_1 = (_size_1 - 1);
                    int _modulo_1 = (_minus_1 % 2);
                    boolean _equals_1 = (_modulo_1 == 0);
                    if (_equals_1) {
                      _xifexpression_1 = XConnection.Kind.QUAD_CURVE;
                    } else {
                      _xifexpression_1 = XConnection.Kind.POLYLINE;
                    }
                    _xifexpression = _xifexpression_1;
                  }
                  _switchResult_1 = _xifexpression;
                  break;
                default:
                  _switchResult_1 = XConnection.Kind.POLYLINE;
                  break;
              }
            } else {
              _switchResult_1 = XConnection.Kind.POLYLINE;
            }
            final XConnection.Kind newKind = _switchResult_1;
            final Function1<KVector, Point2D> _function = new Function1<KVector, Point2D>() {
              public Point2D apply(final KVector it) {
                return new Point2D(it.x, it.y);
              }
            };
            List<Point2D> _map = ListExtensions.<KVector, Point2D>map(layoutPoints, _function);
            ConnectionMorphCommand _connectionMorphCommand = new ConnectionMorphCommand(((XConnection)xElement), newKind, _map);
            final Procedure1<ConnectionMorphCommand> _function_1 = new Procedure1<ConnectionMorphCommand>() {
              public void apply(final ConnectionMorphCommand it) {
                it.setExecuteDuration(duration);
              }
            };
            ConnectionMorphCommand _doubleArrow = ObjectExtensions.<ConnectionMorphCommand>operator_doubleArrow(_connectionMorphCommand, _function_1);
            composite.operator_add(_doubleArrow);
          }
        }
      }
    }
    return composite;
  }
  
  protected KNode toKRootNode(final XDiagram it, final Map<Object, KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kRoot = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      KInsets _createKInsets = this._kLayoutDataFactory.createKInsets();
      shapeLayout.setInsets(_createKInsets);
      shapeLayout.<Float>setProperty(LayoutOptions.SPACING, Float.valueOf(60f));
      EList<KGraphData> _data = kRoot.getData();
      _data.add(shapeLayout);
      cache.put(it, kRoot);
      ObservableList<XNode> _nodes = it.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          EList<KNode> _children = kRoot.getChildren();
          KNode _kNode = Layouter.this.toKNode(it, cache);
          _children.add(_kNode);
        }
      };
      IterableExtensions.<XNode>forEach(_nodes, _function);
      ObservableList<XConnection> _connections = it.getConnections();
      final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          Layouter.this.toKEdge(it, cache);
        }
      };
      IterableExtensions.<XConnection>forEach(_connections, _function_1);
      _xblockexpression = kRoot;
    }
    return _xblockexpression;
  }
  
  protected KNode toKNode(final XNode it, final Map<Object, KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kNode = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      Bounds _layoutBounds = it.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = it.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      shapeLayout.setSize(((float) _width), ((float) _height));
      EList<KGraphData> _data = kNode.getData();
      _data.add(shapeLayout);
      cache.put(it, kNode);
      _xblockexpression = kNode;
    }
    return _xblockexpression;
  }
  
  protected KEdge toKEdge(final XConnection it, final Map<Object, KGraphElement> cache) {
    KEdge _xblockexpression = null;
    {
      XNode _source = it.getSource();
      final KGraphElement kSource = cache.get(_source);
      XNode _target = it.getTarget();
      final KGraphElement kTarget = cache.get(_target);
      KEdge _xifexpression = null;
      boolean _and = false;
      if (!(kSource instanceof KNode)) {
        _and = false;
      } else {
        _and = (kTarget instanceof KNode);
      }
      if (_and) {
        KEdge _xblockexpression_1 = null;
        {
          final KEdge kEdge = this._kGraphFactory.createKEdge();
          EList<KEdge> _outgoingEdges = ((KNode) kSource).getOutgoingEdges();
          _outgoingEdges.add(kEdge);
          EList<KEdge> _incomingEdges = ((KNode) kTarget).getIncomingEdges();
          _incomingEdges.add(kEdge);
          final KEdgeLayout edgeLayout = this._kLayoutDataFactory.createKEdgeLayout();
          KPoint _createKPoint = this._kLayoutDataFactory.createKPoint();
          edgeLayout.setSourcePoint(_createKPoint);
          KPoint _createKPoint_1 = this._kLayoutDataFactory.createKPoint();
          edgeLayout.setTargetPoint(_createKPoint_1);
          EList<KGraphData> _data = kEdge.getData();
          _data.add(edgeLayout);
          cache.put(it, kEdge);
          ObservableList<XConnectionLabel> _labels = it.getLabels();
          final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
            public void apply(final XConnectionLabel it) {
              KLabel _kLabel = Layouter.this.toKLabel(it, cache);
              _kLabel.setParent(kEdge);
            }
          };
          IterableExtensions.<XConnectionLabel>forEach(_labels, _function);
          _xblockexpression_1 = kEdge;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected KLabel toKLabel(final XConnectionLabel it, final Map<Object, KGraphElement> cache) {
    KLabel _xblockexpression = null;
    {
      final KLabel kLabel = this._kGraphFactory.createKLabel();
      String _elvis = null;
      Text _text = null;
      if (it!=null) {
        _text=it.getText();
      }
      String _text_1 = null;
      if (_text!=null) {
        _text_1=_text.getText();
      }
      if (_text_1 != null) {
        _elvis = _text_1;
      } else {
        _elvis = "";
      }
      kLabel.setText(_elvis);
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      Bounds _layoutBounds = it.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = it.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      shapeLayout.setSize(
        ((float) _width), 
        ((float) _height));
      shapeLayout.<Integer>setProperty(LayoutOptions.FONT_SIZE, Integer.valueOf(12));
      shapeLayout.<EdgeLabelPlacement>setProperty(LayoutOptions.EDGE_LABEL_PLACEMENT, EdgeLabelPlacement.CENTER);
      EList<KGraphData> _data = kLabel.getData();
      _data.add(shapeLayout);
      cache.put(it, kLabel);
      _xblockexpression = kLabel;
    }
    return _xblockexpression;
  }
}
