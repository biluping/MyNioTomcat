package com.myboy.component;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.io.IOException;

public abstract class HttpServlet {

    public void doService(HttpRequest request, HttpServletResponse response) throws IOException {
        if (request.method().equals(HttpMethod.GET)){
            doGet(request, response);
        }else {
            doPost(request, response);
        }
    }

    protected abstract void doGet(HttpRequest request, HttpServletResponse response) throws IOException;
    protected abstract void doPost(HttpRequest request, HttpServletResponse response) throws IOException;

}
