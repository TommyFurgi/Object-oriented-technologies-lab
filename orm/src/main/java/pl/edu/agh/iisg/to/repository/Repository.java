package pl.edu.agh.iisg.to.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> add(T student);

    Optional<T> getById(int id);

    List<T> findAll();

    void remove(T student);


}