package com.de.vo;

import java.io.Serializable;

/**
 * @author gs
 * @date 2020/6/15 - 0:32
 */
public class SimpleCameraListVO implements Serializable {
    private int cameraId;
    private String cameraName;

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
