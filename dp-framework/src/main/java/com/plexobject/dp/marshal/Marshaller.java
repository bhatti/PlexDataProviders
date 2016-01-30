package com.plexobject.dp.marshal;

/**
 * This interface marshals and unmarshals objects
 * 
 * @author shahzad bhatti
 *
 * @param <F>
 * @param <T>
 */
public interface Marshaller<F, T> {
    T marshal(F from);

    F unmarshal(T from);
}
