package com.zgnba.clos.db.mapper;

import com.zgnba.clos.db.domain.Audit;
import com.zgnba.clos.db.domain.AuditExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuditMapper {
    long countByExample(AuditExample example);

    int deleteByExample(AuditExample example);

    int deleteByPrimaryKey(String id);

    int insert(Audit record);

    int insertSelective(Audit record);

    List<Audit> selectByExample(AuditExample example);

    Audit selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Audit record, @Param("example") AuditExample example);

    int updateByExample(@Param("record") Audit record, @Param("example") AuditExample example);

    int updateByPrimaryKeySelective(Audit record);

    int updateByPrimaryKey(Audit record);
}