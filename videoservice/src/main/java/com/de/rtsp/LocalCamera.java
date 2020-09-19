//package com.de.rtsp;
//
///**
// * @author gs
// * @date 2020/9/8 - 9:56
// */
//import org.bytedeco.javacpp.BytePointer;
//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacpp.opencv_imgcodecs;
//import org.bytedeco.javacv.*;
//import org.bytedeco.javacv.Frame;
//
//import org.opencv.core.CvType;
//import org.opencv.core.MatOfByte;
//import org.opencv.imgcodecs.Imgcodecs;
//import redis.clients.jedis.Jedis;
//
//import javax.imageio.ImageIO;
//import javax.imageio.stream.FileImageOutputStream;
//import javax.naming.Name;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferByte;
//import java.io.*;
//import java.nio.Buffer;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Date;
//
///**
// * @author gs
// * @date 2020/4/9 - 1:42
// */
//public class LocalCamera {
//
//
//    public void testCamera() throws InterruptedException, IOException {
//
//        Jedis jedis = new Jedis();
//
////        jedis.del("username");
////        jedis.del("number");
//        jedis.del("hash_table");
//        jedis.del("key_list");
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//        System.out.println("111111111111111111111");
//        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//        grabber.start();   //开始获取摄像头数据
//        System.out.println(grabber);
//        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
//        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        canvas.setAlwaysOnTop(false);
////        int ex=0;
//        while (true) {
//            if (!canvas.isDisplayable()) {//窗口是否关闭
//                grabber.stop();//停止抓取
//                System.exit(-1);//退出
//            }
//
//            Frame frame = grabber.grab();
//
//            canvas.showImage(grabber.grabFrame());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
//            opencv_core.Mat mat_new = converter.convertToMat(grabber.grabFrame());
////            ex++;
////            File file = new File("E:\\Liao_java\\new_camera_test\\image_res");
//
//
////            if (!file.exists()) {
////                file.mkdirs();
////            }
//
//
//            BufferedImage bufferedImage = mat2BufImg(mat_new);
//            //将mat转为Byte[]
//            byte[] bytes1 = bufImg2Bytes(bufferedImage);
//
//            //byte[]转为图片
////            byte2image(bytes1,"E:\\Liao_java\\new_camera_test\\image_res\\7.jpg");
//
//            final Base64.Decoder decoder = Base64.getDecoder();
//            final Base64.Encoder encoder = Base64.getEncoder();
//
//            //将byte[]转为base64的字符串
//            String encodedText = encoder.encodeToString(bytes1);
//            String name = timestamp();
//
//            jedis = redis_save(name,encodedText,jedis);
//
//            System.out.println("***************");
//
//            //将base64解码为byte[]
//            byte[] new_byte = decoder.decode(encodedText);
//
//            //byte[]转为图片
//            byte2image(new_byte,"E:\\Liao_java\\new_camera_test\\lib\\7.jpg");
//
////
//            Thread.sleep(100);//50毫秒刷新一次图像
//        }
//
//
//    }
//
//    @Test
//    public void testCamera1() throws FrameGrabber.Exception, InterruptedException {
//        VideoInputFrameGrabber grabber = VideoInputFrameGrabber.createDefault(0);
//        grabber.start();
//        CanvasFrame canvasFrame = new CanvasFrame("摄像头");
//        canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        canvasFrame.setAlwaysOnTop(true);
//        while (true) {
//            if (!canvasFrame.isDisplayable()) {
//                grabber.stop();
//                System.exit(-1);
//            }
//            Frame frame = grabber.grab();
//            canvasFrame.showImage(frame);
//            Thread.sleep(30);
//        }
//    }
//
//    public void byte2File(byte[] bytes,String filePath,String fileName){
//        BufferedOutputStream bos=null;
//        FileOutputStream fos=null;
//        File file=null;
//        try{
//            File dir=new File(filePath);
//            if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
//                dir.mkdirs();
//            }
//            file=new File(filePath+"\\"+fileName);
//            fos=new FileOutputStream(file);
//            bos=new BufferedOutputStream(fos);
//            bos.write(bytes);
//        }
//        catch(Exception e){
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        finally{
//            try{
//                if(bos != null){
//                    bos.close();
//                }
//                if(fos != null){
//                    fos.close();
//                }
//            }
//            catch(Exception e){
//                System.out.println(e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void byte2image(byte[] data,String path){
//        if(data.length<3||path.equals("")) return;
//        try{
//            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
//            imageOutput.write(data, 0, data.length);
//            imageOutput.close();
//            System.out.println("Make Picture success,Please find image in " + path);
//        } catch(Exception ex) {
//            System.out.println("Exception: " + ex);
//            ex.printStackTrace();
//        }
//    }
//    public static BufferedImage mat2BufImg (opencv_core.Mat matrix) {
//        opencv_core.Mat tempMat=matrix;
////        cvtColor(matrix,tempMat,COLOR_BGRA2RGBA);//先要转bgra->rgba
//        OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat();
//        Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
//        return java2DConverter.convert(openCVConverter.convert(tempMat));
//    }
//
//    public static byte[] bufImg2Bytes(BufferedImage original){
//        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(original, "png", bStream);
//        } catch (IOException e) {
//            throw new RuntimeException("bugImg读取失败:"+e.getMessage(),e);
//        }
//        return bStream.toByteArray();
//    }
//
//    public static BufferedImage bytes2bufImg(byte[] imgBytes){
//        BufferedImage tagImg = null;
//        try {
//            tagImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
//            return tagImg;
//        } catch (IOException e) {
//            throw new RuntimeException("bugImg写入失败:"+e.getMessage(),e);
//        }
//    }
//    /**
//     * BufferedImage 转 mat
//     * 参考https://github.com/bytedeco/javacv-examples/blob/master/OpenCV_Cookbook/src/main/scala/opencv_cookbook/OpenCVUtils.scala
//     * @param original
//     * @return
//     */
//    public static opencv_core.Mat bufImg2Mat (BufferedImage original) {
//        OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat();
//        Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
//        opencv_core.Mat mat= openCVConverter.convert(java2DConverter.convert(original));
//        return mat;
//    }
//
//    private String timestamp(){
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        Date now = new Date();
//        return timeFormat.format(now);
//    }
//
//    public Jedis redis_save(String img_name, String img, Jedis jedis){
//        jedis.rpush("key_list", img_name);
//        jedis.hset("hash_table",img_name,img);
//        jedis.close();
//        return jedis;
//
//    }
//
//}
//
