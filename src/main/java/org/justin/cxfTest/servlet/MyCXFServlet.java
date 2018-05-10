package org.justin.cxfTest.servlet;

import javax.servlet.ServletConfig;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.justin.cxfTest.service.Service;
import org.justin.cxfTest.service.Service2;
import org.justin.cxfTest.service.impl.ServiceImpl;
import org.justin.cxfTest.service.impl.ServiceImpl2;

public class MyCXFServlet extends CXFNonSpringServlet {

	@Override
	protected void loadBus(ServletConfig sc) {
		super.loadBus(sc);
		Bus bus =this.getBus();
		BusFactory.setDefaultBus(bus);
		ServerFactoryBean factory = new ServerFactoryBean();
		factory.setServiceClass(Service.class);
		factory.setServiceBean(new ServiceImpl());
		factory.setAddress("/soap");
		factory.create();
		
		SpringJAXRSServerFactoryBean RestFactory = new SpringJAXRSServerFactoryBean();
		RestFactory.setServiceClass(Service2.class);
		RestFactory.setServiceBean(new ServiceImpl2());
		RestFactory.setAddress("/rest");
		RestFactory.create();
	}
	
}
