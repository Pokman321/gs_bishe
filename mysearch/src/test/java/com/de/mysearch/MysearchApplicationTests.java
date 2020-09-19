package com.de.mysearch;

import com.de.mysearch.dao.EsProductDao;
import com.de.mysearch.domain.EsProduct;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
//加载主启动类
//@SpringBootTest(classes = MysearchApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MysearchApplicationTests {

    @Autowired
    private EsProductDao esProductDao;

    @Test
    void contextLoads() {
    }
    @Test
    public void run1(){
        System.out.println(esProductDao.getAllEsProductList(1));
    }

}
