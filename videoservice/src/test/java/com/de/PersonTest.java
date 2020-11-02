package com.de;

import com.de.dao.CameraMapper;
import com.de.dao.CurCameraMapper;
import com.de.dao.PersonMapper;
import com.de.dao.VideoMapper;
import com.de.entity.*;
import com.de.rabbittest.controller.MyProducer;
import com.de.rabbittest.dao.VideoMessageMapper;
import com.de.rabbittest.dao.VideoNoticeMapper;
import com.de.rabbittest.entity.VideoNotice;

//import com.de.dao.VideoNoticeMapper;
//import com.de.entity.VideoNotice;


import com.de.util.PageQueryUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author gs
 * @date 2020/7/8 - 14:30
 */
@RunWith(SpringRunner.class)
//加载主启动类
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonTest {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private CurCameraMapper curCameraMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoNoticeMapper videoNoticeMapper;

    @Test
    public void run1() throws Exception {

        Person person = new Person();
        person.setPersonId(1);
        person.setCameraId(2);
        person.setTime(System.currentTimeMillis());
        person.setImageUrl("E://1.jpg");
        person.setPosition("120#223#223#656");


//        personMapper.savePerson(person);


//        List<Person> people = personMapper.selectByCameraId(2);
//        List<Person> people = personMapper.selectByPersonId(1);
//        Date date = new Date();
//        System.out.println(date.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        long lt1 = Long.parseLong("1594201667844");
        long lt2 = Long.parseLong("1594201882137");

        Date date1 = new Date(lt1);
        Date date2 = new Date(lt2);
        String res1 = simpleDateFormat.format(date1);
        String res2 = simpleDateFormat.format(date2);

        System.out.println(res1 + "     " + res2);


        Date parse_start = simpleDateFormat.parse("2020-7-8 17:30:10");
        Date parse_end = simpleDateFormat.parse("2020-7-8 17:50:10");
        long start_time = parse_start.getTime();
        long end_time = parse_end.getTime();
        List<Person> people = personMapper.selectByTime(start_time, end_time);

//        List<Person> people = personMapper.selectByTime();
//
        for (Person person1 : people) {
            System.out.println(person1);
        }

//        Properties properties = new Properties();
//        InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties");
//        properties.load(in);
    }
    @Test
    public void run2() throws Exception{
        Integer[] a = {1,2};
        int i = personMapper.deleteByPrimaryKey(a);
        System.out.println(i);
    }

    @Test
    public void run3() throws Exception{
//        for(int i=0;i<10;i++){
//            Person person = new Person();
//            person.setPersonId(i);
//            person.setCameraId(i / 5);
//            person.setTime(System.currentTimeMillis());
//            person.setImageUrl("E://1.jpg");
//            person.setPosition("120#223#223#656");
//            System.out.println(personMapper.savePerson(person));
//        }
        List<Person> people = personMapper.selectByCameraId(0);
//        people.stream().map(person -> )
//        for(Person person:people){
//
//        }
        personMapper.deleteBatch(people);

    }

    @Test
    public void run4() throws Exception{

        Date date = new Date();

        for(int i=15;i<16;i++){
            Camera camera = new Camera();
//            camera.setCameraId(600);
            camera.setCameraName("相机"+i);
            camera.setCameraUrl("192.168.64.33/f");
            camera.setCameraCoverImage("E://"+i+".jpg");
            camera.setCameraCategoryId(1);
            camera.setCameraCategoryName("实验室");
            camera.setCreateTime(new Date(System.currentTimeMillis()));
            camera.setUpdateTime(new Date(System.currentTimeMillis()));
            camera.setCameraEnable((byte) 0);
            int i1 = cameraMapper.insertSelective(camera);
            System.out.println(i1);
            System.out.println(camera.getCameraId());
        }


//        cameraMapper.insertCameras()
    }

    @Test
    public void run5() throws Exception{
//        System.out.println(cameraMapper.selectByPrimaryKey(3));
        Camera camera3 = cameraMapper.selectByPrimaryKey(3);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        System.out.println(simpleDateFormat.format(camera3.getCreateTime()) );

        Integer[] a= {3,4,6,8};

        int i = cameraMapper.deleteBatch(a);
        System.out.println(i);
    }

    @Test
    public void run6() throws Exception{
//        System.out.println(cameraMapper.selectByPrimaryKey(1));
        Map<String,Object> map = new HashMap<>();
        map.put("page",1);
        map.put("limit",10);

        PageQueryUtil pageQueryUtil = new PageQueryUtil(map);
//        System.out.println(cameraMapper.deleteByPrimaryKey(5));
        List<Camera> allCameras = cameraMapper.findAllCameras(pageQueryUtil);
        for(Camera camera:allCameras){
            System.out.println(camera);
        }
    }

    @Test
    public void run7() throws Exception{
//        Map<String,Object> map = new HashMap<>();
//        map.put("page",1);
//        map.put("limit",10);
//        PageQueryUtil pageQueryUtil = new PageQueryUtil(map);
//
//        System.out.println(cameraMapper.getTotalCameras(pageQueryUtil));
        Camera camera = new Camera();
        camera.setCameraId(200);
        camera.setCameraEnable((byte) 1);
        System.out.println(cameraMapper.updateByPrimaryKeySelective(camera));

    }

    @Test
    public void run8() throws Exception{
//        Map<String,Object> map = new HashMap<>();
//        map.put("page",1);
//        map.put("limit",10);
//
//        PageQueryUtil pageQueryUtil = new PageQueryUtil(map);
//        System.out.println(cameraMapper.findEnableCameras(pageQueryUtil));
        Integer[] ids = {10,11,9};
        String categoryName = "食堂";
        int categoryId = 3;
        System.out.println(cameraMapper.updateCameraCategorys(categoryName, categoryId, ids));


    }

    @Test
    public void run9() throws Exception{

//        cameraMapper.updateCameraEnable(5);
        curCameraMapper.dropTable("camera1");
        if(curCameraMapper.existTable("camera")==0){
            String camera1 = "camera1";
            curCameraMapper.createNewTable(camera1,1);
        }
        String tableName = "camera1";
        for(int i=0;i<10;i++){
            CurImage curImage = new CurImage();
//            curImage.setId(2);
            curImage.setCameraId(1);
            Thread.sleep(100);
            curImage.setImageTime(System.currentTimeMillis());
            curImage.setImageUrl("E:\\"+i+".jpg");
            curImage.setPersonNum(i/3);
            System.out.println(curCameraMapper.addImage(tableName, curImage));

        }

    }

    @Test
    public void run10() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date parse_start = simpleDateFormat.parse("2020-7-10 13:58:10");
        Date parse_end = simpleDateFormat.parse("2020-7-10 13:58:40");
        long start_time = parse_start.getTime();
        long end_time = parse_end.getTime();
        System.out.println(curCameraMapper.selectByTime("camera1",start_time,end_time));
    }

    @Test
    public void run11() throws Exception{
//        System.out.println(curCameraMapper.selectByPrimaryKey("camera1",2));
//        System.out.println(curCameraMapper.selectAllImage("camera1"));
//        System.out.println(curCameraMapper.selectImageHasPerson("camera1"));
//        System.out.println(curCameraMapper.deleteBatch("camera1", Arrays.asList(new Long[]{1L, 2L, 4L})));
        List ids = new ArrayList();
        ids.add(4);
        ids.add(5);
        ids.add(8);
        Map params = new HashMap();
        params.put("tableName","camera1");
        params.put("ids",ids);
        System.out.println(curCameraMapper.deleteBatch(params));

    }

    @Test
    public void run12() throws Exception{
        UpdateVideo updateVideo = videoMapper.selectByPrimaryKey(0);
        System.out.println(updateVideo);
//        Camera camera = cameraMapper.selectByPrimaryKey(0);
//        System.out.println(camera);
//        List<Person> people = personMapper.selectAllKeys();
//        System.out.println(people);
    }

    @Test
    public void run13() throws Exception{
        UpdateVideo updateVideo = videoMapper.selectByPrimaryKey(1);
        System.out.println(updateVideo);
    }

    @Test
    public void run14() throws Exception{
        String path2 = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
        System.out.println("path2 = " + path2);
    }

    @Test
    public void run15(){

        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring-applicationContext.xml");
//        Object user= context.getBean("userController");
        String[] str=context.getBeanDefinitionNames();
        for (String string : str) {
            System.out.println("..."+string);
        }
//        System.out.println("----"+user);

    }

    @Test
    public void run16(){
        VideoNotice i = videoNoticeMapper.selectByPrimaryKey(1);
        System.out.println(i);
    }



}
