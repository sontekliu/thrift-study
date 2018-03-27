package com.javaliu.thrift.server;

import com.javaliu.thrift.service.Student;
import com.javaliu.thrift.service.StudentService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClient {

    public static void main(String[] args) {
        TTransport tTransport = new TFramedTransport(new TSocket("localhost", 8888), 600);
        TProtocol protocol = new TCompactProtocol(tTransport);

        StudentService.Client client = new StudentService.Client(protocol);

        try{
            tTransport.open();
            Student student = client.findStudentById(2);
            System.out.println(student.getId());
            System.out.println(student.getName());
            System.out.println(student.isSex());

            System.out.println("-----------");

            Student student1 = new Student();
            student1.setId(23);
            student1.setName("李四");
            student1.setSex(false);
            client.saveStudent(student1);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            tTransport.close();
        }
    }
}
