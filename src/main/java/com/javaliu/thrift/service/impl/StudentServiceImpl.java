package com.javaliu.thrift.service.impl;

import com.javaliu.thrift.service.ServiceException;
import com.javaliu.thrift.service.Student;
import com.javaliu.thrift.service.StudentService;
import org.apache.thrift.TException;

public class StudentServiceImpl implements StudentService.Iface{

    public Student findStudentById(int id) throws ServiceException, TException {
        System.out.println("findStudentById is invoke");
        Student student = new Student();
        student.setId(id);
        student.setName("张三");
        student.setSex(true);
        return student;
    }

    public void saveStudent(Student student) throws ServiceException, TException {
        System.out.println("saveStudent is invoke");
        System.out.println(student.getId());
        System.out.println(student.getName());
        System.out.println(student.isSex());
    }
}
