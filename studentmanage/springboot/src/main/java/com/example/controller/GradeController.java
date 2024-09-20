package com.example.controller;


import com.example.common.Result;
import com.example.domain.Grade;
import com.example.service.GradeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/grade")
//成绩接口
public class GradeController {


    @Resource
    GradeService gradeService;

    //增加打分记录
    @PostMapping("/add")
    public Result add(@RequestBody Grade grade) {
        gradeService.add(grade);
        return Result.success();

    }

    //删除打分记录
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){   //PathVariable注解可以附带属性值
        gradeService.deleteById(id);
        return Result.success();
    }

    //分页查询接口
    @GetMapping("selectPage")  //标注查询接口
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Grade grade) {  //?username =xx and n
        PageInfo<Grade> pageInfo = gradeService.selectPage(pageNum, pageSize, grade);   //alt+enter建返回数据
        return Result.success(pageInfo);
    }

    //编辑数据
    @PutMapping("/update")
    public Result update(@RequestBody Grade grade) {   //传一个Json数据
        gradeService.update(grade);
        return Result.success();
    }


}
