package edu.atilim.acma.ws;

import java.io.IOException;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class WebServiceEngine implements Runnable {
	private static WebServiceEngine instance;
	
	public static WebServiceEngine getInstance() {
		if (instance == null) {
			try {
				instance = new WebServiceEngine();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		}
		
		return instance;
	}
	
	WebServer server;
	
	private WebServiceEngine() throws Exception {
		server = new WebServer(81);
		XmlRpcServer xmlrpcServer = server.getXmlRpcServer();
		
		PropertyHandlerMapping phm = new PropertyHandlerMapping();
		phm.addHandler("ACMA", WebService.class);
		
		xmlrpcServer.setHandlerMapping(phm);
		
		XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl)xmlrpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
	}

	@Override
	public void run() {
		try {
			server.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
