package com.example.melodycampus.mapper;

import com.example.melodycampus.model.Music;
import com.example.melodycampus.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.mapper
 * @author: Fenghuiyu
 * @date: 2024/4/10 23:44
 * @description:
 */

@Mapper
public interface CommentMapper {
    List<Comment> findCommentListByMusicId(int musicId, int limit, int offset);

    int addComment(int userId, int musicId, String content, String time, String userName);

    int deleteCommentById(int id);

    Comment findCommentById(int id);

    int getCommentCount(int musicId);
}
