package com.myboy.handler;

import com.myboy.Tomcat;
import com.myboy.component.HttpServlet;
import com.myboy.component.HttpServletResponse;
import com.myboy.uitl.ResponseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DispatcherHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    private static final Map<String, HttpServlet> handlerMapping = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws IOException {

        String className = Tomcat.properties.getProperty(request.uri());
        if (className == null) {
            throw new RuntimeException("404");
        }
        HttpServlet httpServlet;
        if (!handlerMapping.containsKey(request.uri())) {
            try {
                httpServlet = (HttpServlet) Class.forName(className).newInstance();
                handlerMapping.put(request.uri(), httpServlet);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("初始化类" + className + "失败");
            }
        } else {
            httpServlet = handlerMapping.get(request.uri());
        }

        HttpServletResponse response = new HttpServletResponse(ctx);
        httpServlet.doService(request, response);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if ("404".equals(cause.getMessage())){
            ResponseUtil.write(ctx, HttpResponseStatus.NOT_FOUND, HttpResponseStatus.NOT_FOUND.toString());
        }else {
            ResponseUtil.writeError(ctx, cause.getMessage());
        }
    }
}
