package com.example.melodycampus.mapper;

import com.example.melodycampus.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.mapper
 * @author: Fenghuiyu
 * @date: 2024/2/23 21:26
 * @description:
 */
@Mapper
public interface MusicMapper {

    /**
     * 插入音乐
     * @param title
     * @param singer
     * @param time
     * @param url
     * @param userid
     * @return
     */
    int insert(String title,String singer,String time,String url,int userid);


    /**
     * 查询当前id的音乐是否存在
     */
    Music findMusicById(int id);

    /**
     * 删除当前iD的音乐
     */
    int deleteMusicById(int id);

    /**
     * 查询所有的音乐
     */
    List<Music> findMusic();

    /**
     * 查询指定的音乐
     */
    List<Music> findMusicByName(String musicName);

    /**
     * 歌曲喜欢数量+1
     * @param id
     * @return
     */
    int addLikesCount(int id);

    /**
     * 歌曲喜欢数量-1
     * @param id
     * @return
     */
    int reduceLikesCount(int id);

    /**
     * 查询我上传的音乐
     * @param musicName
     * @return
     */
    List<Music> findMyMusicByUidAndName(int userId, String musicName);

    List<Music> findMyMusicByUid(int userId);

    List<Music> findMusicList(int limit, int offset);

    int getMusicCount();
}
