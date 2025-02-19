package de.fxdiagram.core.auxlines;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import java.util.Collection;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AuxiliaryLineMap<T extends Object> {
  private Multimap<Integer, AuxiliaryLine> store = HashMultimap.<Integer, AuxiliaryLine>create();
  
  private Map<XNode, AuxiliaryLine> node2entry = CollectionLiterals.<XNode, AuxiliaryLine>newHashMap();
  
  private double threshold;
  
  public AuxiliaryLineMap() {
    this(1);
  }
  
  public AuxiliaryLineMap(final double threshold) {
    this.threshold = threshold;
  }
  
  public void add(final AuxiliaryLine line) {
    XNode[] _relatedNodes = line.getRelatedNodes();
    if (((Iterable<XNode>)Conversions.doWrapArray(_relatedNodes))!=null) {
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          AuxiliaryLineMap.this.removeByNode(it);
        }
      };
      IterableExtensions.<XNode>forEach(((Iterable<XNode>)Conversions.doWrapArray(_relatedNodes)), _function);
    }
    double _position = line.getPosition();
    int _key = this.getKey(_position);
    this.store.put(Integer.valueOf(_key), line);
    XNode[] _relatedNodes_1 = line.getRelatedNodes();
    if (((Iterable<XNode>)Conversions.doWrapArray(_relatedNodes_1))!=null) {
      final Procedure1<XNode> _function_1 = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          AuxiliaryLineMap.this.node2entry.put(it, line);
        }
      };
      IterableExtensions.<XNode>forEach(((Iterable<XNode>)Conversions.doWrapArray(_relatedNodes_1)), _function_1);
    }
  }
  
  public AuxiliaryLine removeByNode(final XNode node) {
    AuxiliaryLine _xblockexpression = null;
    {
      final AuxiliaryLine line = this.getByNode(node);
      AuxiliaryLine _xifexpression = null;
      boolean _notEquals = (!Objects.equal(line, null));
      if (_notEquals) {
        AuxiliaryLine _xblockexpression_1 = null;
        {
          double _position = line.getPosition();
          final int key = this.getKey(_position);
          this.store.remove(Integer.valueOf(key), line);
          _xblockexpression_1 = this.node2entry.remove(node);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Collection<AuxiliaryLine> getByPosition(final double position) {
    int _key = this.getKey(position);
    return this.store.get(Integer.valueOf(_key));
  }
  
  public AuxiliaryLine getByNode(final XNode node) {
    return this.node2entry.get(node);
  }
  
  public boolean containsKey(final double position) {
    int _key = this.getKey(position);
    return this.store.containsKey(Integer.valueOf(_key));
  }
  
  protected int getKey(final double position) {
    return ((int) ((position / this.threshold) + 0.5));
  }
}
