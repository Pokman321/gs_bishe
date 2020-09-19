package com.de.util;

/**
 * @author gs
 * @date 2020/7/7 - 19:59
 */
import org.jim2mov.core.*;
import org.jim2mov.utils.MovieUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class FilesToMov implements ImageProvider, FrameSavedListener {
    // 文件数组
    private ArrayList<String> fileArray = null;
    // 文件类型
    private int type = MovieInfoProvider.TYPE_QUICKTIME_JPEG;

    private LinkedBlockingDeque<String> path_queue = new LinkedBlockingDeque<>();

    private String base_path;

    private String save_dir;

//    private String video_path;

    public FilesToMov(LinkedBlockingDeque<String> path_queue,String base_path,String save_path){
        this.path_queue = path_queue;
        this.base_path = base_path;
        this.save_dir = save_path;
    }

    public String Trans2Mov() throws MovieSaveException {
        ArrayList<String> fileArray = new ArrayList<>();
        for(String time:path_queue){
            fileArray.add(base_path+"\\"+time+".jpg");
        }
        String video_path = save_dir+"\\"+path_queue.peekFirst()+".mp4";
        new FilesToMov(fileArray, MovieInfoProvider.TYPE_QUICKTIME_JPEG, video_path );
        System.out.println("视频生成完毕");
        return video_path;
    }

//    // 主函数
//    public static void main(String[] args) throws MovieSaveException {
//        ArrayList<String> fileArray = new ArrayList<>();
//        File[] listFiles = new File("D:\\TestPic\\pic").listFiles();
//
//        for (int i = 0; i < listFiles.length; i++) {
//            fileArray.add(listFiles[i].getAbsolutePath());
//        }
//        new FilesToMov(fileArray, MovieInfoProvider.TYPE_QUICKTIME_JPEG, "Test2.mp4");
//    }

    /**
     * 图片转视频
//     * @param filePaths 文件路径数组
     * @param type 格式
     * @param path 文件名
//     * @throws MovieSaveException
     */
    public FilesToMov(ArrayList<String> fileArray, int type, String path) throws MovieSaveException {
        this.fileArray = fileArray;
        this.type = type;
        DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(path);
        // 设置帧频率
        dmip.setFPS(24);
        // 设置帧数--一张图片一帧
        dmip.setNumberOfFrames(fileArray.size());
        // 设置视频高度
        dmip.setMWidth(320);
        // 设置视频宽度
        dmip.setMHeight(240);
        new Jim2Mov(this, dmip, this).saveMovie(this.type);
    }

    @Override
    public void frameSaved(int frameNumber) {
        System.out.println("Saved frame: " + frameNumber);
    }

    @Override
    public byte[] getImage(int frame) {
        try {
            return MovieUtils.convertImageToJPEG(new File(fileArray.get(frame)), 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
