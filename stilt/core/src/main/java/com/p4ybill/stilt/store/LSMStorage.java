package com.p4ybill.stilt.store;

/**
 * Represents an abstract Log-Structured Merge Tree Storage
 * @param <T> document type
 */
public interface LSMStorage<T> {
    public T get(int id);

    public boolean put(int id, T doc);

    public T remove(int id);
}
