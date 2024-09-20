package com.example.controller;

import com.example.common.Result;
import com.example.domain.Student;
import com.example.service.StudentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    StudentService studentService;

    //增加数据
    @PostMapping("/add")
    public Result add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    //删除数据
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        studentService.deleteById(id);
        return Result.success();
    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    //分页查询
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Student student) {  //?name =xx and n
        PageInfo<Student> pageInfo = studentService.selectPage(pageNum, pageSize, student);   //alt+enter建返回数据
        return Result.success(pageInfo);
    }

}
