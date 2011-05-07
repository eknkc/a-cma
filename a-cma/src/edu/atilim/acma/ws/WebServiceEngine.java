package edu.atilim.acma.ws;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.metadata.XmlRpcSystemImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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
	
	Server webServer;
	
	private WebServiceEngine() throws Exception {
		initialize(8081);
	}
	
	private void initialize(int port) throws Exception {
		System.out.println("Starting web server...");
		
		webServer = new Server(port);
		
		ServletContextHandler requestHandler = new ServletContextHandler();
		requestHandler.setContextPath("/");
		requestHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		requestHandler.addServlet(new ServletHolder(new RequestServlet()), "/xmlrpc/*");
		
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setResourceBase("./web");
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		
		HandlerList handlers = new HandlerList();
		handlers.addHandler(requestHandler);
		handlers.addHandler(resourceHandler);
		
		webServer.setHandler(handlers);
	}
	
	private static class RequestServlet extends XmlRpcServlet {
		private static final long serialVersionUID = 1L;

		@Override
		protected XmlRpcHandlerMapping newXmlRpcHandlerMapping() throws XmlRpcException {
			PropertyHandlerMapping phm = new PropertyHandlerMapping();
			phm.addHandler("acma", WebService.class);
			XmlRpcSystemImpl.addSystemHandler(phm);
			
			return phm;
		}
	}

	@Override
	public void run() {
		try {
			webServer.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
