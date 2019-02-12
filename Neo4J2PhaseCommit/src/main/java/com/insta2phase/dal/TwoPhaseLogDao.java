package com.insta2phase.dal;

import com.insta2phase.entities.TwoPhaseLog;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TwoPhaseLogDao extends Neo4jRepository<TwoPhaseLog, Long> {
    List<TwoPhaseLog> findAll();
}
