package com.de.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author gs
 * @date 2020/6/20 - 3:56
 */
public interface UploadFileSevice {

    public String fileupload(MultipartFile file);
}
