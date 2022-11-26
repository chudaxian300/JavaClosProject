package com.zgnba.clos.db.mapper;

import com.zgnba.clos.db.domain.Collect;
import com.zgnba.clos.db.domain.CollectExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CollectMapper {
    long countByExample(CollectExample example);

    int deleteByExample(CollectExample example);

    int deleteByPrimaryKey(String id);

    int insert(Collect record);

    int insertSelective(Collect record);

    List<Collect> selectByExampleWithBLOBs(CollectExample example);

    List<Collect> selectByExample(CollectExample example);

    Collect selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Collect record, @Param("example") CollectExample example);

    int updateByExampleWithBLOBs(@Param("record") Collect record, @Param("example") CollectExample example);

    int updateByExample(@Param("record") Collect record, @Param("example") CollectExample example);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKeyWithBLOBs(Collect record);
}