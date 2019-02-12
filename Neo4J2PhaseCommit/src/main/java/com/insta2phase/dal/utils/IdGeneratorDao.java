package com.insta2phase.dal.utils;

import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface IdGeneratorDao extends Neo4jRepository<IdGenerator, Long> {

}
