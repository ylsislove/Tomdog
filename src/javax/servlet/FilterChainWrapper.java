package javax.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterChainWrapper implements FilterChain {

	
	//将要执行的过滤器下标（过滤器链）
    private int pos = 0;
    
    //存储过滤器的容器（tomcat中使用的是一个数组进行存储的）
    private List<Filter> list = new ArrayList<Filter>();
	
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

    	//判断是否还有要执行的filter
        if(pos<list.size()){
            //每次调用过滤器链的doFilter时，都要将pos坐标+1
            //注意pos++的位置，如果调用完doFilter在pos++ 就出现死循环了
            list.get(pos++).doFilter(request, response, this);
            
        }
    	
    }

}

