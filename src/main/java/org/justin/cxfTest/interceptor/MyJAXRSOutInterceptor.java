package org.justin.cxfTest.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cxf.attachment.DelegatingInputStream;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class MyJAXRSOutInterceptor  extends AbstractPhaseInterceptor< Message>{



	public MyJAXRSOutInterceptor(String phase) {
		super(phase);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
        try { 
        	DelegatingInputStream ins = message.getContent(DelegatingInputStream.class);
        	InputStream in = message.getContent(InputStream.class);
        	String str = IOUtils.readStringFromStream(in);
        	System.out.println(str);
        	 OutputStream os = message.getContent(OutputStream.class); 
        	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        message.setContent(OutputStream.class, baos);
            message.getInterceptorChain().doIntercept(message);  
            ByteArrayOutputStream baos1 = (ByteArrayOutputStream) message.getContent(OutputStream.class);  
            String xml = new String(baos1.toByteArray(), "utf-8");
             xml ="<?xml version='1.0' encoding='UTF-8'?>"+xml;
             xml= xml.replaceAll("soap:", "soapenv:").replaceAll(":soap", ":soapenv").replaceAll("ns2:", "ns:").replaceAll(":ns2", ":ns").replaceAll("return>", "ns:return>").replaceAll("&#xd;", "").replaceFirst("<soapenv:Body>", "<soapenv:Header/><soapenv:Body>");
          
             //这里对xml做处理，处理完后同理，写回流中  
            IOUtils.copy(new ByteArrayInputStream(xml.getBytes()), os);  
              
            baos.close();  
            os.flush();  
  
            message.setContent(OutputStream.class, baos1);  
  
  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
	}
	
}
