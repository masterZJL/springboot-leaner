package com.zjl.study.boottwo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Value("${boot.upload.path}")
    private String uploadDir;

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        for (int i = 0; i < files.size(); i++) {
            saveFile(files.get(i));
        }
        return String.format("上传成功%d个文件!", files.size());
    }

    private void saveFile(MultipartFile file) throws IOException {
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String fullFileName = uploadDir + File.separator + UUID.randomUUID() + fileName;
                File dest = new File(fullFileName);
                if (!dest.getParentFile().exists())
                    dest.getParentFile().mkdirs();
                file.transferTo(dest);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response, @RequestParam("file") String fileName)  {

        try {
            String fullFileName = uploadDir + File.separator + fileName;
            File file = new File(fullFileName);
            FileInputStream inReader = new FileInputStream(file);

            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

            ServletOutputStream outputStream = response.getOutputStream();

            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inReader.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
            inReader.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
