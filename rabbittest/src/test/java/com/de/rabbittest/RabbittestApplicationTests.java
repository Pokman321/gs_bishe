package com.de.rabbittest;

import com.de.rabbittest.dao.VideoNoticeMapper;
import com.de.rabbittest.entity.VideoNotice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbittestApplicationTests {

    @Autowired
    public VideoNoticeMapper videoNoticeMapper;

    @Test
    void contextLoads() {

        VideoNotice videoNotice = videoNoticeMapper.selectUserId(1);

        System.out.println(videoNotice);
    }



}
