package com.myboy;

import com.myboy.handler.DispatcherHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Tomcat {

    private static final Logger logger = LoggerFactory.getLogger(Tomcat.class);

    public static final Properties properties = new Properties();

    static {
        InputStream inputStream = Tomcat.class.getClassLoader().getResourceAsStream("tomcat.properties");
        if (inputStream == null && logger.isDebugEnabled()) {
            logger.debug("配置文件tomcat.properties不存在，使用默认配置");
        } else {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("加载配置文件失败");
            }
        }

        properties.putIfAbsent("server.port", "80");


    }

    public void start() {
        int port = Integer.parseInt(properties.getProperty("server.port"));
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ChannelFuture future = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new DispatcherHandler());
                        }
                    }).bind(port).sync();

            if (logger.isInfoEnabled()) {
                logger.info("Tomcat 启动成功，端口号：{}", port);
            }

            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
