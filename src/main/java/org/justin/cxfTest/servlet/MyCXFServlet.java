package org.justin.cxfTest.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.justin.cxfTest.interceptor.MyJAXRSOutInterceptor;
import org.justin.cxfTest.service.Service;
import org.justin.cxfTest.service.Service2;
import org.justin.cxfTest.service.impl.ServiceImpl;
import org.justin.cxfTest.service.impl.ServiceImpl2;

public class MyCXFServlet extends CXFNonSpringServlet {

	@Override
	protected void loadBus(ServletConfig sc) {
		List<Interceptor<? extends Message>> list = new ArrayList<Interceptor<? extends Message>>();
		MyJAXRSOutInterceptor jAXRSOutInterceptor = new MyJAXRSOutInterceptor(Phase.PRE_STREAM);
		list.add(jAXRSOutInterceptor);
		super.loadBus(sc);
		Bus bus =this.getBus();
		BusFactory.setDefaultBus(bus);
		ServerFactoryBean factory = new ServerFactoryBean();
		factory.setServiceClass(Service.class);
		factory.setServiceBean(new ServiceImpl());
		factory.setOutInterceptors(list);
		factory.setAddress("/soap");
		factory.create();
		
		SpringJAXRSServerFactoryBean restFactory = new SpringJAXRSServerFactoryBean();
		restFactory.setServiceClass(Service2.class);
		restFactory.setServiceBean(new ServiceImpl2());
		restFactory.setAddress("/rest");
//		list.add(jAXRSOutInterceptor);
//		restFactory.setOutInterceptors(list);
		restFactory.create();
		
	}
	
}
