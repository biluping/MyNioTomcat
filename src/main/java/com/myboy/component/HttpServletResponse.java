package com.myboy.component;

import com.myboy.uitl.ResponseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

@Data
public class HttpServletResponse {

    private ChannelHandlerContext ctx;
    private HttpResponseStatus status;

    public HttpServletResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void write(String out) {
        ResponseUtil.write(ctx, status, out);
    }
}
