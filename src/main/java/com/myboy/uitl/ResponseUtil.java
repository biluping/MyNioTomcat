package com.myboy.uitl;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    public static void write(ChannelHandlerContext ctx, HttpResponseStatus status, String out){
        if (out==null) {
            out = "";
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(out.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", "text/html; charset=utf-8");
        ctx.writeAndFlush(response);
        ctx.close();
    }

    public static void writeOk(ChannelHandlerContext ctx, String out){
        write(ctx, HttpResponseStatus.OK, out);
    }

    public static void writeError(ChannelHandlerContext ctx, String out){
        write(ctx, HttpResponseStatus.BAD_REQUEST, out);
    }
}
