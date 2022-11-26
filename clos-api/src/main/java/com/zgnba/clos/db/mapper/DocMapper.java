package com.zgnba.clos.db.mapper;

import com.zgnba.clos.db.domain.Doc;
import com.zgnba.clos.db.domain.DocExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DocMapper {
    long countByExample(DocExample example);

    int deleteByExample(DocExample example);

    int deleteByPrimaryKey(String id);

    int insert(Doc record);

    int insertSelective(Doc record);

    List<Doc> selectByExample(DocExample example);

    Doc selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByExample(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByPrimaryKeySelective(Doc record);

    int updateByPrimaryKey(Doc record);

    void increaseViewCount(@Param("id") String id);

    void increaseVoteCount(@Param("id") String id);
}