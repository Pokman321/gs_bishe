package com.de.service;

import com.de.dao.PersonMapper;
import com.de.entity.AllPerson;
import com.de.entity.CameraLink;
import com.de.entity.Person;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gs
 * @date 2020/9/3 - 1:47
 */

public interface PersonService {

    int getPersonCount();

    PageResult getPersonPage(PageQueryUtil pageUtil);


    Boolean savePerson(AllPerson person);

    AllPerson selectById(Integer id);

    Boolean updateLink(AllPerson person);

    Boolean deleteBatch(Integer[] ids);

}
