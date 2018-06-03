package com.kindol.o2o.util;

import com.kindol.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 编写底层基础服务类，用于封装Thumbnails的方法
 * Thumbnails可以看github的指导手册
 */
public class ImageUtil {

    /*
    watermark.jpg的basePath经常使用，所以设为共有变量，
    是通过当前线程的类加载器逆推上去得到资源的路径的
     */
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    /*
    设置新生成文件名的格式
    定义全局随机类
     */
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    /**
     * 处理用户传过来的图片流，加水印，存储图片，返回相对值路径
     * @param thumbnail 文件流
     * @param targetAddr 图片存储路径
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr){
        //生成新文件名（唯一文件名）
        String realFileName = getRandomFilName();
        //获取文件扩展名
        String extension = getFileExtension(thumbnail.getImageName());

        //确保文件路径存在
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

        try{
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 处理详情图，并返回新生成图片的相对路径（跟上一个函数差不多）
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr){
        //生成新文件名（唯一文件名）
        String realFileName = getRandomFilName();
        //获取文件扩展名
        String extension = getFileExtension(thumbnail.getImageName());

        //确保文件路径存在
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

        try{
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     * @return 文件名
     */
    public static String getRandomFilName(){
        //获取随机的五位数
        int ranNum = r.nextInt(89999) + 10000;
        //返回现在的时间
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + ranNum;
    }

    /**
     * 获取输入文件流的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及的目录，举个例子home/work/kindol/xxx.jpg
     * 那么home work kindol这三个文件夹都得自动创建
     * @param targetAddr 相对路径
     */
    private static void makeDirPath(String targetAddr){
        //获取全路径
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);

        //创建了file对象但不代表着就已经创建了对应的目录，这只是个虚对象
        //调用mkdir将会逐层创建目录
        if (!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件路径则删除该文件
     * 为目录的路径则删除给目录下所有文件，需要先遍历里面的文件进行删除，再删除目录文件本身
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()){
            //为目录
            if (fileOrPath.isDirectory()){
                File[] files = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++)
                    files[i].delete();
            }
            fileOrPath.delete();
        }
    }
}
