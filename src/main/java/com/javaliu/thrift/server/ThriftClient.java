package com.javaliu.thrift.server;

import com.javaliu.thrift.service.User;
import com.javaliu.thrift.service.UserService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClient {

    public static void main(String[] args) {
        TTransport tTransport = new TFastFramedTransport(new TSocket("localhost", 8888), 600);
        TProtocol protocol = new TCompactProtocol(tTransport);

        UserService.Client client = new UserService.Client(protocol);

        try{
            tTransport.open();
            User user = client.findUserById(2);
            System.out.println(user.getId());
            System.out.println(user.getCode());
            System.out.println(user.getName());
            System.out.println(user.getEmail());

            System.out.println("-----------");

            User user1 = new User();
            user1.setId(23);
            user1.setCode("lisi");
            user1.setName("李四");
            user1.setEmail("lisi@163.com");
            client.saveUser(user1);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            tTransport.close();
        }
    }
}
