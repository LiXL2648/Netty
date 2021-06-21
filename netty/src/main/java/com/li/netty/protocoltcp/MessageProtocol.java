package com.li.netty.protocoltcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageProtocol {

    private int len;
    private byte[] content;

    public MessageProtocol(byte[] content) {
        this.len = content.length;
        this.content = content;
    }
}
