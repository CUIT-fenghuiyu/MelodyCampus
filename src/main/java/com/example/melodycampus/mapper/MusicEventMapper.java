package com.example.melodycampus.mapper;

import com.example.melodycampus.model.MusicEvent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface MusicEventMapper {
    /**
     * 查询所有音乐事件
     * @return
     */
    List<MusicEvent> findMusicEventCarousel();

    int deleteMusicEvent(int id);

    int addMusicEvent(String eventName, String eventDesc, String imageUrl);
}
