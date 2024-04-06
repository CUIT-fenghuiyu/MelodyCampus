package com.example.melodycampus.controller;

import com.example.melodycampus.common.ResponseBodyMessage;
import com.example.melodycampus.mapper.MusicEventMapper;
import com.example.melodycampus.model.MusicEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.controller
 * @author: Fenghuiyu
 * @date: 2024/3/11 22:01
 * @description:
 */
@RestController
@RequestMapping("/musicevent")
public class MusicEventController {
    @Autowired private MusicEventMapper musicEventMapper;

    @Value("${music.event.path}")
    private String UPLOAD_PATH;

    @RequestMapping("/carousel")
    public ResponseBodyMessage<List<MusicEvent>> getCarousel() {
        List<MusicEvent> carouselList = musicEventMapper.findMusicEventCarousel();
        return new ResponseBodyMessage<>(0, "find carousel success", carouselList);
    }

    @RequestMapping("/deleteEvent")
    public ResponseBodyMessage delete(@ RequestParam("id") int id) {
        int res = musicEventMapper.deleteMusicEvent(id);
        return new ResponseBodyMessage(0, "delete success", id);
    }

    @RequestMapping("/addEvent")
    public ResponseBodyMessage add(@RequestParam("eventName") String eventName,
                                   @RequestParam("eventDesc") String eventDesc,
                                   @RequestParam("pictureName") MultipartFile file) throws UnsupportedEncodingException {
        // 获取文件全名＋文件类型
        String fileNameAndType = file.getOriginalFilename();

        // 获取文件路径
        String filePath = UPLOAD_PATH + fileNameAndType;

        // 获取文件类型
        String fileType = fileNameAndType.substring(fileNameAndType.lastIndexOf(".") + 1);

        if (!fileType.equals("jpg") && !fileType.equals("png")) {
            return new ResponseBodyMessage(-1, "文件格式错误", fileNameAndType);
        }

        // 获取文件对象
        File dest = new File(filePath);

        // 判断文件是否已存在
        if (dest.exists()) {
            return new ResponseBodyMessage<>(-1, "活动图片已存在，请勿重复上传", fileNameAndType);
        }

        //  判断文件父目录是否存在，不存在则创建文件父目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        // 保存文件到服务器中
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBodyMessage<>(-1, "upload failed", false);
        }

        // 保存活动图片请求路径
        String imageUrl = "/musicevent/get?path=" + fileNameAndType;

        // 在发送文件路径给前端之前,对其进行编码
        imageUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString());

        // 落库
        int res = musicEventMapper.addMusicEvent(eventName, eventDesc, imageUrl);
        if (res == 1){
            return new ResponseBodyMessage(0, "add success", fileNameAndType);
        }else {
            dest.delete();
            return new ResponseBodyMessage(-1, "add failed", fileNameAndType);
        }

    }

    /**
     * 获取活动图片文件流显示在前端页面
     * @param path
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(@RequestParam("path") String path) {
        byte[] eventStream = null;
        try {
            // 获取活动图片文件流
            File file = new File(UPLOAD_PATH+"/"+path);
            eventStream = Files.readAllBytes(file.toPath());
            if(eventStream == null) {
                // 返回400状态码
                return ResponseEntity.badRequest().build();
            }
            // 返回状态码和比特流
            return ResponseEntity.ok(eventStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 获取图片详情
     * @param path
     * @return
     */

    @RequestMapping("/getImageFile")
    public ResponseEntity<Resource> getImageFile(@RequestParam("path") String path) {
        try {
            // 根据请求路径构建实际的文件路径
            String filePath = "F:\\project\\GraduationDesign\\MusicCampus\\event\\" + path;
            File file = new File(filePath);

            if (file.exists()) {
                Resource resource = new UrlResource(file.toURI());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("image/jpeg"))
                        .body(resource);
            }
        } catch (Exception e) {
            // 处理异常
        }

        // 如果文件不存在,返回适当的响应
        return ResponseEntity.notFound().build();
    }

}
