package com.example.cashenvelope.envelope;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;;

@Mapper
public interface EnvelopeMapper {
    @Select("SELECT * FROM envelopes WHERE env_id = #{id}")
    Envelope findById(@Param("id") String id);

    @Select("SELECT * FROM envelopes WHERE owner_id = #{id}")
    List<Envelope> findByUserId(@Param("id") String id);

    @Select("SELECT * FROM envelopes WHERE name LIKE #{name} OR notes LIKE #{notes}")
    List<Envelope> search(@Param("name") String name, @Param("notes") String notes);

    @Insert("INSERT INTO envelopes (env_id, name, value, notes, owner_id, created_at, updated_at) VALUES (#{id}, #{name}, #{value}, #{notes}, #{ownerId}, #{created_at}, #{updated_at})")
    int save(Envelope envelope);

    @Update("UPDATE envelopes SET name=#{name}, value=#{value}, notes=#{notes}, updated_at=#{updated_at} WHERE env_id=#{id}")
    int update(Envelope envelope);

    @Delete("DELETE FROM envelopes WHERE env_id = #{id}")
    int delete(@Param("id") String id);

    @Delete("DELETE FROM envelopes WHERE owner_id = #{id}")
    void deleteAll(@Param("id") String id);
}