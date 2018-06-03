package com.kindol.o2o.util;

public class PathUtil {

    //获取当前操作系统下文件的分割符（windows和linux不同）
    private static String seperator = System.getProperty("file.separator");

    //返回项目图片的根路径
    public static String getImgBasePath(){
        //取得操作系统的名字
        String os = System.getProperty("os.name");
        String basePath = "";

        /*
        指定图片绝对路径，而不将图片保存到项目路径classpath下，
        因为保存到classpath的时候，如果工程重新部署，新生成的图片将会删除

        也有一些其他的做法，比如把图片放在别的服务器上，
        通过URL来获取图片

         后面还要替换文件分隔符
         */
        if (os.toLowerCase().startsWith("win")){
            basePath = "F:/ssm_Vedio/image";
        }else {
            basePath = "/home/kindol/image";
        }
        basePath = basePath.replace("/", seperator);

        return basePath;
    }

    //得到项目图片根路径后，在此根路径下返回项目图片的子路径
    public static String getShopImgPath(long shopId){
        String imagePath = "/upload/item/shop/" + shopId + "/";

        //最后的返回结果记得替换文件分隔符
        return imagePath.replace("/", seperator);
    }
}
