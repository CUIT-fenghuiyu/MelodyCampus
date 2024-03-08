package com.example.melodycampus.mapper;

import com.example.melodycampus.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.mapper
 * @author: Fenghuiyu
 * @date: 2024/2/24 20:22
 * @description:
 */
@Mapper
public interface LoveMusicMapper {

    /**
     * 查询喜欢的音乐
     * @param userId
     * @param musicId
     * @return
     */
    Music findLoveMusicByMusicIdAndUserId(int userId, int musicId);

    /**
     * 添加到喜欢的音乐
     * @param userId
     * @param musicId
     * @return
     */
    boolean insertLoveMusic(int userId,int musicId);


    /**
     * 查询这个用户，收藏过的所有的音乐
     * @param userId
     * @return
     */
    List<Music> findLoveMusicByUserId(int userId);


    /**
     * 查询当前用户，指定为musicName的音乐，支持模糊查询
     * @param musicName
     * @param userId
     * @return
     */
    List<Music> findLoveMusicBykeyAndUID(int userId,String musicName);

    /**
     * 移除某个用户喜欢的音乐
     * @param userId 用户的ID
     * @param musicId 音乐的ID
     * @return 受影响的行数
     */
    int deleteLoveMusic(int userId,int musicId);

    /**
     * 根据音乐的ID 进行删除
     * @param musicId 音乐的ID
     * @return
     */
    int deleteLoveMusicByMusicId(int musicId);

    /**
     * 查询当前用户收藏的所有音乐ID
     * @param userId
     */
    List<Integer>findLoveMusicIdByUserId(int userId);

}

