package com.easy.dashboard.mapper;

import com.easy.dashboard.domain.SendEmail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.scheduling.annotation.Async;

@Mapper
public interface SendEmailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SendEmail record);

    int insertSelective(SendEmail record);

    SendEmail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SendEmail record);

    int updateByPrimaryKey(SendEmail record);
}