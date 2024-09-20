package com.example.service;

import com.example.domain.StudentCourse;
import com.example.exception.CustomException;
import com.example.mapper.StudentCourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentCourseService {
    @Resource
    StudentCourseMapper studentCourseMapper;

    //学生选课
    public void add(StudentCourse studentCourse) {
        //筛选学生是否选课
        StudentCourse course = studentCourseMapper.selectByCondition(studentCourse);
        if (course != null) {
            throw new CustomException("已经选过这门课啦");
        }
        studentCourseMapper.insert(studentCourse);
    }

    public PageInfo<StudentCourse> selectPage(Integer pageNum, Integer pageSize, StudentCourse studentCourse) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentCourse> list = studentCourseMapper.selectAll(studentCourse);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        studentCourseMapper.deleteById(id);
    }
}
