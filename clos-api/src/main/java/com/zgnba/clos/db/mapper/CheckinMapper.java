package com.zgnba.clos.db.mapper;

import com.zgnba.clos.db.domain.Checkin;
import com.zgnba.clos.db.domain.CheckinExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Mapper
@Repository
public interface CheckinMapper {
    long countByExample(CheckinExample example);

    int deleteByExample(CheckinExample example);

    int deleteByPrimaryKey(String id);

    int insert(Checkin record);

    int insertSelective(Checkin record);

    List<Checkin> selectByExample(CheckinExample example);

    Checkin selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Checkin record, @Param("example") CheckinExample example);

    int updateByExample(@Param("record") Checkin record, @Param("example") CheckinExample example);

    int updateByPrimaryKeySelective(Checkin record);

    int updateByPrimaryKey(Checkin record);
}