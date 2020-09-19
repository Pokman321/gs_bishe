package com.de.rabbittest.direct;

import org.springframework.stereotype.Service;

/**
 * @author gs
 * @date 2020/8/4 - 20:16
 */
@Service
public class TestService {

    private static int cnt = 0;


    public int increase(){
        cnt++;
        return cnt;
    }


}
