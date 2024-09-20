package com.example.service;


import com.example.domain.Grade;
import com.example.exception.CustomException;
import com.example.mapper.GradeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class GradeService {

    @Resource
    GradeMapper gradeMapper;

    public void add(Grade grade) {
        Grade dbGrade = gradeMapper.selectByCondition(grade); //获取数据库数据
        if(dbGrade != null ){  //打过分了
            throw new CustomException("你已经打过分了");
        }
        gradeMapper.insert(grade);
    }




    //分页查询
    //total 查询总数 list是数据列表
    //pageNum的当前页码   pageSize是煤
    // 业的个数
    public PageInfo<Grade> selectPage(Integer pageNum, Integer pageSize, Grade grade) {
        PageHelper.startPage(pageNum, pageSize);
        List<Grade> gradeList = gradeMapper.selectAll(grade);
        return PageInfo.of(gradeList);
    }

    public void update(Grade grade) {
        gradeMapper.update(grade);
    }

    public void deleteById(Integer id) {
        gradeMapper.deleteById(id);
    }
}
