package com.de.service.impl;

import com.de.dao.PersonMapper;
import com.de.entity.AllPerson;
import com.de.service.PersonService;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gs
 * @date 2020/9/3 - 1:51
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;


    @Override
    public int getPersonCount() {
        return 0;
    }

    @Override
    public PageResult getPersonPage(PageQueryUtil pageUtil) {
        return null;
    }

    @Override
    public Boolean savePerson(AllPerson person) {
        return null;
    }

    @Override
    public AllPerson selectById(Integer id) {
        return null;
    }

    @Override
    public Boolean updateLink(AllPerson person) {
        return null;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return null;
    }
}
