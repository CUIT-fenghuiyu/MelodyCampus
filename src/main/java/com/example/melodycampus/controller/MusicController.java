package com.example.melodycampus.controller;

import com.example.melodycampus.common.ResponseBodyMessage;
import com.example.melodycampus.common.SessionUtil;
import com.example.melodycampus.mapper.LoveMusicMapper;
import com.example.melodycampus.mapper.MusicMapper;
import com.example.melodycampus.model.Music;
import com.example.melodycampus.model.User;
import com.sun.deploy.net.HttpResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.binding.BindingException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.plugin.dom.core.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.controller
 * @author: Fenghuiyu
 * @date: 2024/2/23 21:34
 * @description:
 */
@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    @Value("${music.local.path}")
    private String UPLOAD_PATH;

    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> uploadMusic(@RequestParam String singer,
                                                    @RequestParam("filename") MultipartFile file,
                                                    HttpServletRequest request,
                                                    HttpServletResponse resp) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {

        // 获取文件全名＋文件类型
        String fileNameAndType = file.getOriginalFilename();

        // 获取文件路径
        String filePath = UPLOAD_PATH + fileNameAndType;

        // 获取文件类型
        String fileType = fileNameAndType.substring(fileNameAndType.lastIndexOf(".") + 1);

        // 获取文件对象
        File dest = new File(filePath);

        // 校验文件类型是否为 MP3
        // TODO: 2024/2/24 底层判断文件类型是否为 MP3
        if ("mp3".equalsIgnoreCase(fileType)) {

            //  判断文件父目录是否存在，不存在则创建文件父目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                // 保存文件
                file.transferTo(dest);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseBodyMessage<>(-1, "upload failed", false);
            }
           // return new ResponseBodyMessage<>(0, "upload success", true);
        } else {
            return new ResponseBodyMessage<>(-1, "文件类型不是MP3格式，请重新上传", false);
        }

        // 歌曲落库
        // 获取歌曲名
        int index = fileNameAndType.lastIndexOf(".");//lastIndexOf
        String title = fileNameAndType.substring(0,index);

        // 获取用户id
        User user = SessionUtil.getLoginUser(request);
        int userid = user.getId();

        // 存入音乐文件http请求路径
        String url = "/music/get?path="+title;

        // 上传时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sf.format(new Date());

        try {
            int ret = musicMapper.insert(title,singer,time,url,userid);
            if(ret == 1) {
                //这里应该跳转到音乐列表页面
                //resp.sendRedirect("/list.html");
                // TODO: 2024/2/24 list.html 
                return new ResponseBodyMessage<>(0, "storage success", true);
            }else {
                dest.delete();
                return new ResponseBodyMessage<>(-1,"storage failed",false);
            }
        }catch (BindingException e) {
            dest.delete();
            return new ResponseBodyMessage<>(-1,"storage failed！",false);
        }
    }

    /**
     * 获取播放音乐：/music/get?path=xxx.mp3
     * @param path
     * @return
     */

    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(String path) {

        byte[] musicStream = null;
        try {
            // 读取音乐文件比特流
            File file = new File(UPLOAD_PATH+"/"+path);
            musicStream = Files.readAllBytes(file.toPath());
            if(musicStream == null) {
                // 返回400状态码
                return ResponseEntity.badRequest().build();
            }
            // 返回状态码和比特流
            return ResponseEntity.ok(musicStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 删除单个音乐
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteMusic(@RequestParam int id) {
        // 检查音乐是否存在
        Music music = musicMapper.findMusicById(id);
        if(music == null) {
            System.out.println("没有这个id的音乐");
            return new ResponseBodyMessage<>(-1,"所要删除的音乐不存在",false);
        }else {
            // 删除数据库中的记录
            int ret = musicMapper.deleteMusicById(id);
            if(ret == 1) {
                // 删除服务器上的数据
                String fileName = music.getTitle();
                File file = new File(UPLOAD_PATH+"/"+fileName+".mp3");

                System.out.println("当前的路径："+file.getPath());

                if(file.delete()) {
                    return new ResponseBodyMessage<>(0,"Service music delete success",true);
                }else {
                    return new ResponseBodyMessage<>(-1,"Service music delete filed",false);
                }
            }else {
                return new ResponseBodyMessage<>(-1,"Storage music delete failed",false);
            }
        }

    }

    /**
     * 批量删除音乐
     * @param id
     * @return
     */

    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteMusicSel(@RequestParam("id[]") List<Integer> id) {
        System.out.println("所有要删除的音乐ID ： "+ id);

        for (int i = 0; i < id.size(); i++) {
            int musicId = id.get(i);
            Music music = musicMapper.findMusicById(musicId);
            if(music == null) {
                System.out.println("音乐id："+musicId+" 不存在！");
                return new ResponseBodyMessage<>(-1, "音乐id："+musicId+" 不存在！", false);
            }
            int ret = musicMapper.deleteMusicById(musicId);
            if(ret == 1) {
                // 删除服务器上的数据
                String fileName = music.getTitle();
                File file = new File(UPLOAD_PATH+"/"+fileName+".mp3");

                System.out.println("当前的路径："+file.getPath());

                if(file.delete()) {
                    //同步删除用户lovemusic表中的该条音乐数据
                    loveMusicMapper.deleteLoveMusicByMusicId(musicId);
                }else {
                    return new ResponseBodyMessage<>(-1,"Service lovemusic delete filed",false);
                }
            }else {
                return new ResponseBodyMessage<>(-1,"Storage music delete failed",false);
            }
        }

        return new ResponseBodyMessage<>(0,"music delete success",true);
    }

    /**
     * 查询音乐(根据有无音乐名返回)
     * @param musicName
     * @return
     */

    @RequestMapping("findMusic")
    public ResponseBodyMessage<List<Music>> findMusic(@RequestParam(required = false) String musicName) {
        List<Music> musicList = null;
        if (musicName != null){
            musicList = musicMapper.findMusicByName(musicName);
        }else {
            musicList = musicMapper.findMusic();
        }
        return new ResponseBodyMessage<>(0, "find success", musicList);
    }

    /**
     * 查询我上传的音乐（根据是否有音乐名返回）
     * @param musicName
     */
    @RequestMapping("findMyMusic")
    public ResponseBodyMessage<List<Music>> findMyMusic(@RequestParam(required = false) String musicName,
                                                        HttpServletRequest request) {
        int userId = SessionUtil.getLoginUser(request).getId();
        List<Music> musicList = null;
        if (musicName != null){
            musicList = musicMapper.findMyMusicByUidAndName(userId,musicName);
        }else {
            musicList = musicMapper.findMyMusicByUid(userId);
        }

        return new ResponseBodyMessage<>(0, "find my musicList success", musicList);
    }


}
