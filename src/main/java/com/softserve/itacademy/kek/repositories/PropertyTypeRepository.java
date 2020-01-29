package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.PropertyType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTypeRepository extends CrudRepository<PropertyType, Integer> {
}
