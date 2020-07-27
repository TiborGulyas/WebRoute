import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        MyHandler myHandler = new MyHandler();
        server.createContext("/test", myHandler);
        server.createContext("/hello", myHandler);
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = methodSelector(t.getRequestURI().getPath());
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


    public static String methodSelector(String identifier) {
        Routes routes = new Routes();
        String message = "";
        for (Method m : Routes.class.getMethods()) {
            if (m.isAnnotationPresent(WebRoute.class)) {
                WebRoute annotation = m.getAnnotation(WebRoute.class);
                if (annotation.url().equals(identifier)) {
                    try {
                        message = (String) m.invoke(routes);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        System.out.println("message: "+message);
        return message;
    }
}

