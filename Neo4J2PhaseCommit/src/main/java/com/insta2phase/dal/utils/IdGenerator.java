package com.insta2phase.dal.utils;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

public class IdGenerator {

    @Id
    @GeneratedValue
    private Long id;

    public IdGenerator() {
    }

    public IdGenerator(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
