package javax.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterChainWrapper implements FilterChain {

	
	//��Ҫִ�еĹ������±꣨����������
    private int pos = 0;
    
    //�洢��������������tomcat��ʹ�õ���һ��������д洢�ģ�
    private List<Filter> list = new ArrayList<Filter>();
	
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

    	//�ж��Ƿ���Ҫִ�е�filter
        if(pos<list.size()){
            //ÿ�ε��ù���������doFilterʱ����Ҫ��pos����+1
            //ע��pos++��λ�ã����������doFilter��pos++ �ͳ�����ѭ����
            list.get(pos++).doFilter(request, response, this);
            
        }
    	
    }

}

