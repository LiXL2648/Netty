package com.li.netty.test;

import io.netty.util.NettyRuntime;
import org.junit.Test;

public class NettyTest {

    @Test
    public void testAvailableProcessors() {
        int processors = NettyRuntime.availableProcessors();
        System.out.println(processors);
    }


    @Test
    public void testBitOption() {
        System.out.println(5&4);
    }
}

