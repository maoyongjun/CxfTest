package org.justin.cxfTest.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;


public class SoapOutInterceptor extends AbstractPhaseInterceptor<Message> {  
      
  
	  
	    public SoapOutInterceptor() {  
	        //这儿使用pre_stream，意思为在流关闭之前  
	        super(Phase.PRE_STREAM);  
	    }  
	    

		public void handleMessage(Message message) {  
	  
	        try { 
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
	  
	    private class CachedStream extends CachedOutputStream {  
	  
	        public CachedStream() {  
	  
	            super();  
	  
	        }  
	  
	        protected void doFlush() throws IOException {  
	  
	            currentStream.flush();  
	  
	        }  
	  
	        protected void doClose() throws IOException {  
	  
	        }  
	  
	        protected void onWrite() throws IOException {  
	  
	        }  
	  
	    }

//		@Override
//		public void handleMessage(SoapMessage msg) throws Fault {
//			 HttpServletResponse response = (HttpServletResponse)msg.get(AbstractHTTPDestination.HTTP_RESPONSE);  
//			 try {
//				ServletOutputStream out = response.getOutputStream();
//				out.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//		}  
	  
}  
