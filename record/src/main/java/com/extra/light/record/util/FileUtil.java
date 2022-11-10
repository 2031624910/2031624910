package com.extra.light.record.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author rookie
 * @version 1.0
 * @date 2019/11/18 22:53
 * @description 文件处理类
 */
@Slf4j
@Component
public class FileUtil {

    private static final String filePath = "/uploadVersionFile/";


    /**
     * 获取网络中的文件
     *
     * @param keyStorePath 网络文件地址
     * @return 返回文件输入流
     * @throws IOException
     */
    public static InputStream getInputStreamByUrl(String keyStorePath) throws IOException {
        URL url = new URL(keyStorePath);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();
        httpUrl.getInputStream();
        return httpUrl.getInputStream();
    }

    /**
     * 返回服务器文件上传目录
     *
     * @param request
     * @return
     */
    public File fileUploadPath(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath(filePath);
        File dir = new File(realPath);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 服务器文件下载路径
     *
     * @param fileName   文件名
     * @param fileSuffix 文件后缀 如.doc
     * @return
     */
    public String fileDownloadPath(String fileName, String fileSuffix) {
        //考虑到分布式，返回相对路径
        /*String downLoadFilePath = request.getScheme() + "://" +
                request.getServerName() + ":"
                + request.getServerPort()
                + "/uploadVersionFile/" + fileName + fileSuffix;*/
        return filePath + fileName + fileSuffix;
    }

    /**
     * 文件上传
     *
     * @param uploadFile 需要上传的文件
     * @param request
     * @return 返回文件下载路径
     */
    public String fileUpload(MultipartFile uploadFile, HttpServletRequest request) {
        log.info("调用文件上传-->>" + uploadFile);
        //获取上传路径
        File dir = fileUploadPath(request);
        //上传的文件名（包含后缀）
        String filename = uploadFile.getOriginalFilename();
        //服务端保存的文件对象
        File fileServer = new File(dir, filename);
        log.info("文件真实路径:" + fileServer.getAbsolutePath());
        if (fileServer.exists()) {
            fileServer.delete();
        }
        //下载路径
        String filePath;
        try {
            //实现上传
            uploadFile.transferTo(fileServer);
            filePath = fileDownloadPath(filename, "");
        } catch (IOException e) {
            log.error("文件上传错误-->>>" + e.getMessage());
            return null;
        }
        return filePath;
    }

}