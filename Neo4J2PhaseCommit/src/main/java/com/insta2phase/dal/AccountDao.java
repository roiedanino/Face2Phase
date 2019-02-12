package com.insta2phase.dal;

import com.insta2phase.entities.Account;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AccountDao extends Neo4jRepository<Account, String> {
}
