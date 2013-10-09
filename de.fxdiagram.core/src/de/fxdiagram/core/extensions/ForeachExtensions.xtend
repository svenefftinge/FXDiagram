package de.fxdiagram.core.extensions

/**
 * Avoids conflicts of Java8's Iterable#forEachExt with {@link IterableExtensions#forEachExt}
 */
class ForeachExtensions {
		
	/**
	 * Applies {@code procedure} for each element of the given iterable.
	 * 
	 * @param iterable
	 *            the iterable. May not be <code>null</code>.
	 * @param procedure
	 *            the procedure. May not be <code>null</code>.
	 */
	def static <T> void forEachExt(Iterable<T> iterable, (T)=>void procedure) {
		IterableExtensions.forEach(iterable, procedure)
	}
	
}