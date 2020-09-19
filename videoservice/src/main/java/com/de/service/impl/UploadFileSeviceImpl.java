package com.de.service.impl;

import com.de.config.Constants;
import com.de.service.UploadFileSevice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author gs
 * @date 2020/6/20 - 3:18
 */
@Service
public class UploadFileSeviceImpl implements UploadFileSevice {
    public String fileupload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        String wholepath =  Constants.FILE_UPLOAD_DIC + newFileName;
        //创建文件
        File destFile = new File(wholepath);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
