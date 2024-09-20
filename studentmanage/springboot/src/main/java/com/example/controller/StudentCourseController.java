package com.example.controller;

import com.example.common.Result;
import com.example.domain.Course;
import com.example.domain.StudentCourse;
import com.example.service.StudentCourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/studentCourse")
public class StudentCourseController {

    @Resource
    StudentCourseService studentCourseService;

    /**
     * 学生选课
     **/
    @PostMapping("/add")
    //选课新增
    //RequestBody包装呈JSON
    public Result add(@RequestBody StudentCourse studentCourse) {
        studentCourseService.add(studentCourse);
        return Result.success();
    }

    @GetMapping("selectPage")  //标注查询接口
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             StudentCourse studentCourse) {  //?name =xx and n
        PageInfo<StudentCourse> pageInfo = studentCourseService.selectPage(pageNum, pageSize, studentCourse);
        return Result.success(pageInfo);
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        studentCourseService.deleteById(id);
        return Result.success();
    }


}
