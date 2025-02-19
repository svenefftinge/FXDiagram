package de.fxdiagram.core.model;

import javafx.beans.property.Property;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class CrossRefData {
  private final String _href;
  
  public String getHref() {
    return this._href;
  }
  
  private final Property<?> _property;
  
  public Property<?> getProperty() {
    return this._property;
  }
  
  private final int _index;
  
  public int getIndex() {
    return this._index;
  }
  
  public CrossRefData(final String href, final Property<?> property, final int index) {
    super();
    this._href = href;
    this._property = property;
    this._index = index;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._href== null) ? 0 : this._href.hashCode());
    result = prime * result + ((this._property== null) ? 0 : this._property.hashCode());
    result = prime * result + this._index;
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
    CrossRefData other = (CrossRefData) obj;
    if (this._href == null) {
      if (other._href != null)
        return false;
    } else if (!this._href.equals(other._href))
      return false;
    if (this._property == null) {
      if (other._property != null)
        return false;
    } else if (!this._property.equals(other._property))
      return false;
    if (other._index != this._index)
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
