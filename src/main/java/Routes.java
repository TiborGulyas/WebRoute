public class Routes {

    @WebRoute(url = "/test")
    public String test1(){
        return "test1";
    }

    @WebRoute(url = "/hello")
    public String test2(){
        return "hellooooo";
    }



}
