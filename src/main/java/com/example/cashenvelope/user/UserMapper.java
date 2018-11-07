package com.example.cashenvelope.user;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<User> findUsers();

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") String id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users (id, username, password, created_at, updated_at) VALUES (#{id}, #{username}, #{password}, #{created_at}, #{updated_at})")
    int save(User user);

    @Update("UPDATE users SET username=#{username}, updated_at=#{updated_at} WHERE id=#{id}")
    int update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int delete(@Param("id") String id);
}