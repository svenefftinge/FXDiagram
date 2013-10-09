package de.fxdiagram.core.extensions;

import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Avoids conflicts of Java8's Iterable#forEachExt with {@link IterableExtensions#forEachExt}
 */
@SuppressWarnings("all")
public class ForeachExtensions {
  /**
   * Applies {@code procedure} for each element of the given iterable.
   * 
   * @param iterable
   *            the iterable. May not be <code>null</code>.
   * @param procedure
   *            the procedure. May not be <code>null</code>.
   */
  public static <T extends Object> void forEachExt(final Iterable<T> iterable, final Procedure1<? super T> procedure) {
    IterableExtensions.<T>forEach(iterable, procedure);
  }
}
