package com.hongtu.slice.db.mapper;

import com.hongtu.slice.db.entity.TbCatalog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CatalogMapper {
    @Select("select * from tb_catalog")
    @Results({
            @Result(property = "name",column = "name")
    })
    List<TbCatalog> getAll();
    @Select("select * from tb_catalog where id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "idcode",column = "idcode")
    })
    List<TbCatalog> selectCatalogByID(@Param("id") Integer id);
}
