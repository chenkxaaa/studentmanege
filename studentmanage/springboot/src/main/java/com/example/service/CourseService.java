package com.example.service;

import com.example.domain.Course;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseService {


    @Resource  //调用mapper方法
    private CourseMapper courseMapper;

    //total 查询总数 list是数据列表
    //pageNum的当前页码   pageSize是每业的个数
    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize, Course course) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courseList = courseMapper.selectAll(course);
        return PageInfo.of(courseList);//调用方法返回数据
    }

    //新增数据
    public void add(Course course) {
        courseMapper.insert(course);
    }

    //修改数据
    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    //删除数据
    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }
}
