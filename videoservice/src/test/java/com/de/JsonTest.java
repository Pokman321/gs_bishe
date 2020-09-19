package com.de;

//import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gs
 * @date 2020/6/22 - 13:43
 */
public class JsonTest {



    public void createJson(Map<String,Object> map2,String primary_num,String pId,String position,String time,String cId){

        Map<String,Object> map = new HashMap<>();

        map.put("personId",pId);
        map.put("position",position);
        map.put("time",time);
        map.put("cameraId",cId);

        JSONObject obj1 = new JSONObject(map);

        map2.put(primary_num,obj1);


    }


    public void releaseJson(JSONObject json) throws JSONException {
        Iterator<String> it = json.keys();
        while(it.hasNext()) {
            // 获得key
            String key = it.next();
            JSONObject value = (JSONObject) (json.get(key));
            System.out.println(value.getString("position"));

        }
    }

    public static void main(String[] args) throws JSONException {
        JsonTest test = new JsonTest();
        Map<String,Object> map2 = new HashMap<>();

        test.createJson(map2,"1","1","100#200#220#300","2020343242","2");
        test.createJson(map2,"2","2","110#200#223#320","2020343242","2");
        test.createJson(map2,"3","3","150#230#223#320","2020343242","2");

        JSONObject json_res = new JSONObject(map2);
//        System.out.println(json_res);
        test.releaseJson(json_res);
    }

}
