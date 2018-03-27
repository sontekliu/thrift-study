package com.javaliu.thrift.server;

import com.javaliu.thrift.service.StudentService;
import com.javaliu.thrift.service.impl.StudentServiceImpl;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class ThriftServer {

    public static void main(String[] args) throws Exception{
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8888);
        THsHaServer.Args server = new THsHaServer.Args(socket)
                .minWorkerThreads(2)
                .maxWorkerThreads(4);
        StudentService.Processor<StudentServiceImpl> processor = new StudentService.Processor<StudentServiceImpl>(new StudentServiceImpl());

        server.protocolFactory(new TCompactProtocol.Factory());
        server.transportFactory(new TFramedTransport.Factory());
        server.processorFactory(new TProcessorFactory(processor));

        TServer tServer = new THsHaServer(server);
        System.out.println("Server is running");

        tServer.serve();
    }
}
