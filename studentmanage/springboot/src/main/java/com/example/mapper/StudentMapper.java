package com.example.mapper;

import com.example.domain.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StudentMapper {
    @Select("select * from student where username = #{username}")
    Student selectByUsername(String username);
    @Insert("insert into student (username,password,name,phone,email,sex,birth,avatar,role) " +
            "values(#{username},#{password},#{name},#{phone},#{email},#{sex},#{birth},#{avatar},#{role})")

    void insert(Student student);

    //删除数据
    @Delete("delete from student where id = #{id}")
    void deleteById(Integer id);

    //修改数据
    @Update("update student set username = #{username}, password= #{password}," +
            "name = #{name},phone = #{phone},email = #{email},sex = #{sex}" +
            ",birth = #{birth},avatar = #{avatar} where id = #{id} ")
    void updateById(Student student);

    //分页查询
    @Select("select * from student where username like concat('%',#{username},'%') order by id desc")
    List<Student> selectAll(Student student);
}
