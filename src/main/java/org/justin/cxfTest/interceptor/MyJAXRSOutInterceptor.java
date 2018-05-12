package org.justin.cxfTest.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cxf.attachment.DelegatingInputStream;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class MyJAXRSOutInterceptor extends AbstractPhaseInterceptor<Message> {

	public MyJAXRSOutInterceptor(String phase) {
		super(phase);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		 handle1( message) ;
//		try {
////			handle2(message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private void handle2(Message message) throws Fault, IOException {
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
		message.setContent(OutputStream.class, byteArrayOutput);
		message.getInterceptorChain().doIntercept(message);//通过拦截器将byteArrayOutput字节流注入进去
		ByteArrayOutputStream newByteArrayOutput =  (ByteArrayOutputStream) message.getContent(OutputStream.class);
		String str = new String(newByteArrayOutput.toByteArray(),"UTF-8");//输出流转化为String
		str = "<?xml version='1.0' encoding='UTF-8'?>" + str;
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(str.getBytes("UTF-8"));//String转化为输入流
		OutputStream os = message.getContent(OutputStream.class);
		IOUtils.copy(byteArrayInput, os)	;//写回到输出流中

	}

	private void handle1(Message message) throws Fault {
		try {
			OutputStream os = message.getContent(OutputStream.class);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			message.setContent(OutputStream.class, baos);
			message.getInterceptorChain().doIntercept(message);
			ByteArrayOutputStream baos1 = (ByteArrayOutputStream) message.getContent(OutputStream.class);
			String xml = new String(baos1.toByteArray(), "utf-8");
			xml = "<?xml version='1.0' encoding='UTF-8'?>" + xml;
			xml = xml.replaceAll("soap:", "soapenv:").replaceAll(":soap", ":soapenv").replaceAll("ns2:", "ns:")
					.replaceAll(":ns2", ":ns").replaceAll("return>", "ns:return>").replaceAll("&#xd;", "")
					.replaceFirst("<soapenv:Body>", "<soapenv:Header/><soapenv:Body>");
			// 这里对xml做处理，处理完后同理，写回流中
			IOUtils.copy(new ByteArrayInputStream(xml.getBytes()), os);
//			baos.close();
//			os.flush();
//			message.setContent(OutputStream.class, baos1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
