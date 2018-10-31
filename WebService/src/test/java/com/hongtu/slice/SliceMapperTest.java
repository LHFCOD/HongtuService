package com.hongtu.slice;

import com.hongtu.slice.db.entity.TbSlice;
import com.hongtu.slice.db.mapper.SliceMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SliceMapperTest {
    @Autowired
    private SliceMapper sliceMapper;

    @Test
    public void testSelect(){
        List<TbSlice> sliceList=sliceMapper.getAll();
        int i=0;
    }
}
