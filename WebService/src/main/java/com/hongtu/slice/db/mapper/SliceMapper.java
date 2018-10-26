package com.hongtu.slice.db.mapper;

import com.hongtu.slice.db.entity.TbSlice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SliceMapper {
    @Select("select * from tb_slice")
    @Results({
          @Result(property = "name",column = "name")
    })
    List<TbSlice> getAll();
}
