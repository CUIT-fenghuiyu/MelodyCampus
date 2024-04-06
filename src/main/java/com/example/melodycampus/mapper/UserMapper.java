package com.example.melodycampus.mapper;

import com.example.melodycampus.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.mapper
 * @author: Fenghuiyu
 * @date: 2024/2/22 23:35
 * @description:
 */
@Mapper
public interface UserMapper {
    User login(User loginUser);

    User selectByName(String username);

    int add(String username, String password);

    List<User> selectAll();

    int delete(int id);

    int updatePassword(int userId, String newEncryptPassword);
}
