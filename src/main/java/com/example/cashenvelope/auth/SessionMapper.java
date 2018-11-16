package com.example.cashenvelope.auth;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SessionMapper {
    @Select("SELECT * FROM sessions WHERE payload = #{payload}")
    Session findByPayload(String payload);

    @Insert("INSERT INTO sessions (id, payload, created_at, updated_at) VALUES (#{id}, #{payload}, #{created_at}, #{updated_at})")
    void save(Session session);

    @Delete("DELETE FROM sessions WHERE id = #{id}")
    void delete(String id);

    /**
     * this should be changed to updated_at instead of created_at when the
     * SessionController updates instead of deleting foundSession (in /login)
     *
     * @param date String formatted as date (LocalTimeDate) in UTC (00)
     */
    @Delete("DELETE FROM sessions WHERE created_at < #{date}")
    void clear(String date);
}