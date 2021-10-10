package dev.melick.repositories.hibernate;

import java.util.List;

// A generic type is a class or interface that is parameterizes over types.
// We use angle brackets to specify the parameter type
public interface CrudRepository<T> {

    // Create
    T add(T t);

    // Read
    T getById(Integer id);

    List<T> getAll();

    // Update
    void update(T t);

    // Delete
    void delete(Integer id);
}
