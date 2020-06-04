package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service(value = "iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        //abc.jpg
        String fileName= file.getOriginalFilename();
        //扩展名
        //123.jpg -> jpg
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        //不能使用上传原名，防止多人传同名不同照片是互相覆盖
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径是:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        //判断文件夹是否存在
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //路径+文件名
        File targetFile = new File(path,uploadFileName);
        try {
            //上传开始，若未发生异常，则上传成功
            file.transferTo(targetFile);
            //将targetfile上传到FTP服务器上
            //Lists.newArrayList(),直接新建arraylist并add这个targetFile
            FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
            //传完之后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
