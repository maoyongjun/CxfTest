package org.justin.cxfTest.service.impl;

import javax.ws.rs.GET;

import org.justin.cxfTest.service.Service2;

public class ServiceImpl2 implements Service2{

	@GET
	public String sayHi() {
		StringBuilder s = new StringBuilder();
		for(int i=0;i<2;i++){
			s.append("abcqweszxfqwedzwerpjewrjwpcsdfaewrjpzfjweorhpuwerjpjfsjdpofweriufjpsjerpjeottttttttt"+i+";");
		}
		return s.toString();
	}

}
