package com.h2o_execution.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IoIRedisRepository extends CrudRepository<IoIRedisList, String>
{
}
