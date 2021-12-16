package com.myboy.servlet;

import com.myboy.component.HttpServlet;
import com.myboy.component.HttpServletResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.io.IOException;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpRequest request, HttpServletResponse response) throws IOException {
        response.write("ok");
    }
}
