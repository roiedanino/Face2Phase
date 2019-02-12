package com.insta2phase.services.coordinator;

import com.insta2phase.crudQueries.Query;

import java.util.Optional;

public interface CoordinatorService {
    <T> Optional<T> executeQuery(Query query, Class<T> clazz);
}
