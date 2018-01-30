package com.sam.graduation.design.gdemailserver.dao;

import com.sam.graduation.design.gdemailserver.model.pojo.EmailCode;
import org.apache.ibatis.annotations.Param;

public interface EmailCodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EmailCode record);

    int insertSelective(EmailCode record);

    EmailCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmailCode record);

    int updateByPrimaryKey(EmailCode record);

    EmailCode selectByEmailAndCodeOrderByGenerateTimeDesc(@Param("email") String email);

    int selectCountByEmailBetweenTime(@Param("email") String email, @Param("from") String from, @Param("to") String to);
}