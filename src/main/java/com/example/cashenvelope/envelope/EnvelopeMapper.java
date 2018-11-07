package com.example.cashenvelope.envelope;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;;

@Mapper
public interface EnvelopeMapper {
    @Select("SELECT * FROM envelopes WHERE env_id = #{id}")
    Envelope findById(@Param("id") String id);

    @Insert("INSERT INTO envelopes (env_id, name, value, notes, owner_id, created_at, updated_at) VALUES (#{id}, #{name}, #{value}, #{notes}, #{ownerId}, #{created_at}, #{updated_at})")
    int save(Envelope envelope);
}