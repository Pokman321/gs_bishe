package com.de.dao;

import com.de.entity.Person;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/22 - 13:03
 */
@Repository
public interface PersonMapper {

    int savePerson(final Person person);

    List<Person> selectByCameraId(int cameraId);

    List<Person> selectByPersonId(int personId);

    List<Person> selectByTime(@Param("start_time") long start_time,@Param("end_time") long end_time);

    List<Person> selectAllKeys();


    int deleteByPrimaryKey(Integer[] ids);

    int deleteBatch(List<Person> person);
    //增加

//    int insertPerson(Person person);

//    int insertPersonSelective(Person person);

}
