package com.example.mapper;


import com.example.domain.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CourseMapper {
    //分页查询接口
    @Select("select * from course where name like concat('%',#{name},'%') and no like concat('%',#{no},'%') and teacher like concat('%',#{teacher},'%') order by id desc ")
    //三个条件的模糊查询
    List<Course> selectAll(Course course);


    //新增课程数据
    @Insert("insert into course (name,no,see,times,teacher) values(#{name},#{no},#{see},#{times},#{teacher})")
    void insert(Course course);

    //修改数据
    @Update("update course set name = #{name} ,no = #{no}, see = #{see} , times = #{times}, teacher = #{teacher} where id = #{id} ")
    void updateById(Course course);

    //删除数据
    @Delete("delete from course where id = #{id}")
    void deleteById(Integer id);


}
