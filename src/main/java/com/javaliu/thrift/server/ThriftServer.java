package com.javaliu.thrift.server;

import com.javaliu.thrift.service.UserService;
import com.javaliu.thrift.service.impl.UserServiceImpl;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class ThriftServer {

    public static void main(String[] args) throws Exception{
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8888);
        THsHaServer.Args server = new THsHaServer.Args(socket)
                .minWorkerThreads(2)
                .maxWorkerThreads(4);
        UserService.Processor<UserServiceImpl> processor = new UserService.Processor<UserServiceImpl>(new UserServiceImpl());

        server.protocolFactory(new TCompactProtocol.Factory());
        server.transportFactory(new TFastFramedTransport.Factory());
        server.processorFactory(new TProcessorFactory(processor));

        TServer tServer = new THsHaServer(server);
        System.out.println("Server is running");

        tServer.serve();
    }
}
