package com.h2o_execution.persistence;

import com.h2o_execution.domain.IoI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@RedisHash("ioi")
@AllArgsConstructor
class IoIRedisList implements Serializable
{
    @Id
    private String symbol;
    private List<IoI> ioIs;
}
