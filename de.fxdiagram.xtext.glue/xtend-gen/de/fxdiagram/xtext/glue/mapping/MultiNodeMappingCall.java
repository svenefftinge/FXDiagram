package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class MultiNodeMappingCall<T extends Object, ARG extends Object> extends AbstractNodeMappingCall<T, ARG> {
  private final Function1<? super ARG, ? extends Iterable<? extends T>> _selector;
  
  public Function1<? super ARG, ? extends Iterable<? extends T>> getSelector() {
    return this._selector;
  }
  
  private final NodeMapping<T> _nodeMapping;
  
  public NodeMapping<T> getNodeMapping() {
    return this._nodeMapping;
  }
  
  public MultiNodeMappingCall(final Function1<? super ARG, ? extends Iterable<? extends T>> selector, final NodeMapping<T> nodeMapping) {
    super();
    this._selector = selector;
    this._nodeMapping = nodeMapping;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this._selector== null) ? 0 : this._selector.hashCode());
    result = prime * result + ((this._nodeMapping== null) ? 0 : this._nodeMapping.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    if (!super.equals(obj))
      return false;
    MultiNodeMappingCall other = (MultiNodeMappingCall) obj;
    if (this._selector == null) {
      if (other._selector != null)
        return false;
    } else if (!this._selector.equals(other._selector))
      return false;
    if (this._nodeMapping == null) {
      if (other._nodeMapping != null)
        return false;
    } else if (!this._nodeMapping.equals(other._nodeMapping))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
