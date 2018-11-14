package com.hongtu.slice;

import com.hongtu.slice.db.util.DatabaseIO;
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
    DatabaseIO databaseIO;

    @Test
    public void testSelect() {
        String path=databaseIO.getSlicePathByID(1122);
        String path1=databaseIO.getSlicePathByID(1122);
        int i = 0;
    }
}
