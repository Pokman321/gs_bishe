package com.de.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gs
 * @date 2020/7/29 - 20:09
 */
public class OtherUtils {

    public static String default_avatar = "/admin/dist/img/avatar.png";

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static Date dataTrans(String data) throws ParseException {
        return simpleDateFormat.parse(data);

    }

}
