package com.example.melodycampus.controller;

import com.example.melodycampus.common.ResponseBodyMessage;
import com.example.melodycampus.common.SessionUtil;
import com.example.melodycampus.mapper.LoveMusicMapper;
import com.example.melodycampus.mapper.MusicMapper;
import com.example.melodycampus.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.controller
 * @author: Fenghuiyu
 * @date: 2024/2/24 20:18
 * @description:
 */
@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private LoveMusicMapper loveMusicMapper;

    /**
     * 喜欢/取消喜欢音乐接口
     * @param musicId
     * @param request
     * @return
     */
    @RequestMapping("/likeMusic")
    public ResponseBodyMessage<Boolean> likeMusic(@RequestParam int musicId,
                                                  HttpServletRequest request) {
        int userId = SessionUtil.getLoginUser(request).getId();

        Music music = loveMusicMapper.findLoveMusicByMusicIdAndUserId(userId,musicId);
        if(music != null) {
            //取消喜欢
            int res = loveMusicMapper.deleteLoveMusic(userId,musicId);
            if(res == 1) {
                musicMapper.reduceLikesCount(musicId); // 歌曲喜欢数量-1
                System.out.println(musicId + ": " + music.getLikes());
                return new ResponseBodyMessage<>(0,"取消收藏成功",true);

            }
            return new ResponseBodyMessage<>(-1, "取消收藏失败", false);
        }

        boolean effect = loveMusicMapper.insertLoveMusic(userId,musicId);
        if(effect) {
            musicMapper.addLikesCount(musicId); // 歌曲喜欢数量+1
            System.out.println(musicId + ": " + musicMapper.findMusicById(musicId).getLikes());
            return new ResponseBodyMessage<>(0,"收藏成功！",true);
        }else {
            return new ResponseBodyMessage<>(-1,"收藏失败！",false);
        }
    }

    @RequestMapping("/findLoveMusic")
    public ResponseBodyMessage<List<Music>> findLoveMusic(@RequestParam(required = false) String musicName,
                                                      HttpServletRequest request) {
        int userId = SessionUtil.getLoginUser(request).getId();
        List<Music> musicList = null;

        if (musicName == null) {
            musicList = loveMusicMapper.findLoveMusicByUserId(userId);
        }else {
            musicList = loveMusicMapper.findLoveMusicBykeyAndUID(userId,musicName);
        }
        return new ResponseBodyMessage<>(0, "find loveMusic success", musicList);
    }

    @RequestMapping("/getLoveMusicIdList")
    public ResponseBodyMessage<List<Integer>> getLoveMusicIdList(HttpServletRequest request) {
        int userId = SessionUtil.getLoginUser(request).getId();
        List<Integer> musicIdList = loveMusicMapper.findLoveMusicIdByUserId(userId);

        return new ResponseBodyMessage<>(0, "get loveMusicIdList success", musicIdList);
    }

}
