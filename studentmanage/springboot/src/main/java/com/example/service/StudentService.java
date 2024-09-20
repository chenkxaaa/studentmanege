package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.RoleEnum;
import com.example.domain.Account;
import com.example.domain.Student;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;
    //登录
    //传入Admin的admin的实体类通过数据库查出user的数据
    public Account login(Account account){
        Account dbstudent = studentMapper.selectByUsername(account.getUsername());
        if (dbstudent==null){
            //无用户
            throw new CustomException("账号或密码错啦");
        }
        if (!account.getPassword().equals(dbstudent.getPassword())){
            throw new CustomException("账号或密码错啦");
        }
        return dbstudent;
    }

    //注册
    public void register(Account account){
        Student student=new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        this.add(student);
    }

    public void add(Student student) {
        //根据student的username查询数据
        Student dbstudent = studentMapper.selectByUsername(student.getUsername());
        if(dbstudent!=null){//数据重复了
            throw new CustomException("账号已存在");
        }
        if(ObjectUtil.isEmpty(student.getName())){
            student.setName(student.getUsername());//默认名称设置

        }
        student.setRole(RoleEnum.STUDENT.name());
        studentMapper.insert((student));
    }

    //删除
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);

    }

    //更新数据
    public void updateById(Student student) {

        studentMapper.updateById(student);
    }


    public PageInfo<Student> selectPage(Integer pageNum, Integer pageSize, Student student) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.selectAll(student);
        return PageInfo.of(studentList);//调用方法返回数据
    }
}
