package at.universalnet.signature.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Utils {

	private Utils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable
	 * type.
	 *
	 * @param type		the type
	 * @return the underlying class
	 */
    /*
     * Original Code by Ian Robertson (June 23, 2007)
     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
     */
	public static Class<?> getClass(final Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
