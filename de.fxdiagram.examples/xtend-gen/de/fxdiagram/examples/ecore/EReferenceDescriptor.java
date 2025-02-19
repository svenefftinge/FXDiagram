package de.fxdiagram.examples.ecore;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.CachedDomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.Extension;

@ModelNode
@SuppressWarnings("all")
public class EReferenceDescriptor extends CachedDomainObjectDescriptor<EReference> {
  public EReferenceDescriptor(final EReference eReference, @Extension final EcoreDomainObjectProvider provider) {
    super(eReference, provider.getId(eReference), provider.getFqn(eReference), provider);
  }
  
  public EReference resolveDomainObject() {
    EReference _xblockexpression = null;
    {
      String _id = this.getId();
      final URI uri = URI.createURI(_id);
      URI _trimFragment = uri.trimFragment();
      String _string = _trimFragment.toString();
      final EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(_string);
      String _fragment = uri.fragment();
      final int posEquals = _fragment.indexOf("=");
      String _xifexpression = null;
      if ((posEquals == (-1))) {
        _xifexpression = uri.fragment();
      } else {
        String _fragment_1 = uri.fragment();
        _xifexpression = _fragment_1.substring(0, posEquals);
      }
      final String fragment = _xifexpression;
      Resource _eResource = ePackage.eResource();
      EObject _eObject = _eResource.getEObject(fragment);
      _xblockexpression = ((EReference) _eObject);
    }
    return _xblockexpression;
  }
  
  public int hashCode() {
    EReference _domainObject = this.getDomainObject();
    int _hashCode = _domainObject.hashCode();
    EReference _elvis = null;
    EReference _domainObject_1 = this.getDomainObject();
    EReference _eOpposite = _domainObject_1.getEOpposite();
    if (_eOpposite != null) {
      _elvis = _eOpposite;
    } else {
      EReference _domainObject_2 = this.getDomainObject();
      _elvis = _domainObject_2;
    }
    int _hashCode_1 = _elvis.hashCode();
    return (_hashCode + _hashCode_1);
  }
  
  public boolean equals(final Object other) {
    if ((other instanceof EReferenceDescriptor)) {
      boolean _or = false;
      EReference _domainObject = ((EReferenceDescriptor)other).getDomainObject();
      EReference _domainObject_1 = this.getDomainObject();
      boolean _equals = Objects.equal(_domainObject, _domainObject_1);
      if (_equals) {
        _or = true;
      } else {
        EReference _domainObject_2 = ((EReferenceDescriptor)other).getDomainObject();
        EReference _domainObject_3 = this.getDomainObject();
        EReference _eOpposite = _domainObject_3.getEOpposite();
        boolean _equals_1 = Objects.equal(_domainObject_2, _eOpposite);
        _or = _equals_1;
      }
      return _or;
    } else {
      return false;
    }
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public EReferenceDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
