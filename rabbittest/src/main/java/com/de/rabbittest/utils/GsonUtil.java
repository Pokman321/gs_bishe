package com.de.rabbittest.utils;

import com.de.rabbittest.entity.AjaxResult;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gs
 * @date 2020/8/14 - 15:00
 */
@Slf4j
public class GsonUtil {

    private GsonUtil(){

    }

    public static final String LONG_DATETIME="yyyy-MM-dd HH:mm:ss";

    /**
     * 对象转为字符串
     * @param detail
     * @return
     */

    public static String objectToJsonStr(Object detail){
        if(detail==null){
            return null;
        }
        Gson gson = new GsonBuilder().setDateFormat(LONG_DATETIME).create();
        String ret = null;
        try{
            ret = gson.toJson(detail);
        } catch (Exception e){
            log.error("objectToJson error,detail: "+detail.toString());
            throw new RuntimeException("objectToJson error,detail: "+detail.toString());
        }
        return ret;

    }

    /**
     * json字符串转为对象
     */
    public static <T> T jsonToObject(String jsonStr,Class t){
        Gson gson = new GsonBuilder().setDateFormat(LONG_DATETIME).create();
        Type type = TypeToken.get(t).getType();
        try{
            return gson.fromJson(jsonStr,type);
        } catch (Exception e){
            return jsonToObject2(jsonStr,t);
        }

    }

    private static <T> T jsonToObject2(String jsonStr, Class t){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class,(JsonDeserializer<Date>) (jsonElement,type1,context) ->new Date(jsonElement.getAsJsonPrimitive().getAsLong()))
                .create();
        Type type = TypeToken.get(t).getType();
        try{
            return gson.fromJson(jsonStr,type);
        } catch (Exception e){
            log.error("转化字符串失败,jsonStr: "+jsonStr,e);
            return null;
        }
    }

    public static <T> T jsonToObject(String jsonStr, Type t){
        Gson gson = new GsonBuilder().setDateFormat(LONG_DATETIME).create();
        Type type = TypeToken.get(t).getType();
        try{
            return gson.fromJson(jsonStr,type);
        } catch (Exception e){
            log.error("转化字符串失败,jsonStr: "+jsonStr,e);
            return null;
        }
    }

    /**
     * json to map
     * @param json
     * @return
     */
    public static Map<String,Object> jsonToMap(String json){
        Gson gson = new GsonBuilder().setDateFormat(LONG_DATETIME).enableComplexMapKeySerialization().create();
        return gson.fromJson(json,new TypeToken<Map<String,Object>>(){
        }.getType());

    }

    /**
     * json to list
     * @param json
     * @param <T>
     * @return
     */

    public static <T> List<T> jsonToList(String json){
        return new GsonBuilder().setDateFormat(LONG_DATETIME).create().fromJson(
                json,new TypeToken<List<T>>(){
                }.getType());
    }

    /**
     * 将 detail 中的属性都复制到 对象 t中
     * 并创建 新的 t 实例
     * @param detail
     * @param t
     * @param <T>
     * @return
     */

    public static <T> T deepClone(Object detail,Class t){
        return GsonUtil.jsonToObject(GsonUtil.objectToJsonStr(detail),t);
    }

}
