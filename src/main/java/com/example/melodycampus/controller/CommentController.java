package com.example.melodycampus.controller;

import com.example.melodycampus.common.ResponseBodyMessage;
import com.example.melodycampus.common.SessionUtil;
import com.example.melodycampus.mapper.CommentMapper;
import com.example.melodycampus.mapper.MusicMapper;
import com.example.melodycampus.model.Music;
import com.example.melodycampus.model.User;
import com.example.melodycampus.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.controller
 * @author: Fenghuiyu
 * @date: 2024/4/10 23:43
 * @description:
 */

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMappper;

    @Autowired
    private MusicMapper musicMapper;

    // 添加评论
    @RequestMapping("/addComment")
    public ResponseBodyMessage<Boolean> addComment(@RequestParam("musicId") int musicId, @RequestParam("content") String content,
                                                   HttpServletRequest request){
        Music music = musicMapper.findMusicById(musicId);

        if (music == null){
            return new ResponseBodyMessage<>(-1,"该音乐不存在",false);
        }

        if (content == null || content.length()>100){
            return new ResponseBodyMessage<>(-1,"评论不符合规范",false);
        }

        User loginUser = SessionUtil.getLoginUser(request);

        if (loginUser == null){
            return new ResponseBodyMessage<>(-1,"未登录",false);
        }

        int userId = loginUser.getId();
        String userName = loginUser.getUsername();

        // 上传时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sf.format(new Date());

        int res = commentMappper.addComment(userId,musicId,content,time,userName);

        if (res == 1){
            return new ResponseBodyMessage<>(0,"评论成功", true);
        }else {
            return new ResponseBodyMessage<>(-1,"评论失败",false);
        }

    }

    //查询分页音乐列表
    @RequestMapping("/getCommentList")
    public ResponseBodyMessage<List<Comment>> findCommentListByMusicId(@RequestParam("musicId") int musicId,
                                                                    @RequestParam("page") int page,
                                                                    @RequestParam("size") int size){
        Music music = musicMapper.findMusicById(musicId);
        if (music == null){
            return new ResponseBodyMessage<>(-1,"该音乐不存在",null);
        }

        if(page>0 && size>0) {
            // 计算limit和offset的值
            int limit = size;
            int offset = size * (page - 1);
            List<Comment> comments = commentMappper.findCommentListByMusicId(musicId, limit, offset);

            return new ResponseBodyMessage<>(0,"查询成功",comments);
        }
        return new ResponseBodyMessage<>(0,"查询失败，分页参数不合法",null);
    }

    @RequestMapping("/totalpage")
    public ResponseBodyMessage<Integer> getCommentTotalPage(@RequestParam("musicId") int musicId,
                                                        @RequestParam("size") int size){
        Music music = musicMapper.findMusicById(musicId);
        if (music == null){
            return new ResponseBodyMessage<>(-1,"该音乐不存在",null);
        }

        if (size <= 0){
            return new ResponseBodyMessage<>(-1,"分页参数不合法",null);
        }

        int commentCount = commentMappper.getCommentCount(musicId);
        int totalPage = (int)Math.ceil(commentCount*1.0/size); //通过Math.ceil(小数会转换成大的整数)计算出总页数

        return new ResponseBodyMessage<>(0,"查询成功",totalPage);
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @RequestMapping("/deleteComment")
    public ResponseBodyMessage<Boolean> deleteComment(@RequestParam("id") int id,  HttpServletRequest request){
        // 判断登录用户是否 是该评论的作者|管理员
        Comment comment = commentMappper.findCommentById(id);
        if (comment == null){
            return new ResponseBodyMessage<>(-1,"该评论不存在",false);
        }

        User loginUser = SessionUtil.getLoginUser(request);
        if (loginUser == null){
            return new ResponseBodyMessage<>(-1,"未登录",false);
        }

        if (loginUser.getId() != comment.getUserId() && loginUser.getUserType() == 0){
            return new ResponseBodyMessage<>(-1,"无删除权限",false);
        }

        int res = commentMappper.deleteCommentById(id);
        if (res == 1){
            return new ResponseBodyMessage<>(0,"删除成功",true);
        }else {
            return new ResponseBodyMessage<>(-1,"删除失败",false);
        }
    }
}
