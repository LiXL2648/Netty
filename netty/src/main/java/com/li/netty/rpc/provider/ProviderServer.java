package com.li.netty.rpc.provider;

import com.li.netty.rpc.server.NettyServer;

public class ProviderServer {

    private static final String hostname = "127.0.0.1";

    private static final Integer port = 2648;

    public static void main(String[] args) throws Exception {

        NettyServer.runServer(hostname, port);
    }
}
