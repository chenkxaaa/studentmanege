package com.example.controller;

import com.example.common.Result;
import com.example.domain.Course;
import com.example.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//第一步写controller
//第二步写service
//第三步写courseMapper

@RestController
@RequestMapping("/course")
public class CourseController {
    //若出现404表示接口写错了或者后台没重启或者拼写错误，去controller查看接口路径
    //若出现500因为后台报错了：看控制台是什么错误
    @Resource
    private CourseService courseService;

    @GetMapping("selectPage")  //标注查询接口
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Course course) {  //?name =xx and n
        PageInfo<Course> pageInfo = courseService.selectPage(pageNum, pageSize, course);
        return Result.success(pageInfo);
    }
    //新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Course course){   //传一个Json数据
        courseService.add(course);
        return  Result.success();
    }

    //编辑数据
    @PutMapping ("/update")
    public Result update(@RequestBody Course course){   //传一个Json数据
        courseService.updateById(course);
        return  Result.success();
    }

    //删除数据
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){   //PathVariable注解可以附带属性值
        courseService.deleteById(id);
        return Result.success();
    }

}
