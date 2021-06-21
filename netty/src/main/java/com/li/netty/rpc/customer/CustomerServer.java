package com.li.netty.rpc.customer;

import com.li.netty.rpc.common.HelloService;
import com.li.netty.rpc.server.NettyClient;

public class CustomerServer {

    public static final String providerName = "HelloService#";

    public static void main(String[] args) {

        // 创建一个消费者
        NettyClient customer = new NettyClient();

        // 创建代理对象
        HelloService helloService = (HelloService) customer.getBean(HelloService.class, providerName);

        // 通过代理对象调用服务提供者的方法
        Object result = helloService.hello("hello, rpc");
        System.out.println("调用的结果：" + result);
    }
}
