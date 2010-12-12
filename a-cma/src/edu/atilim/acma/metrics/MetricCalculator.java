package edu.atilim.acma.metrics;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Package;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.util.CollectionHelper;
import edu.atilim.acma.util.Delegate;
import edu.atilim.acma.util.Log;
import edu.atilim.acma.util.Pair;

public class MetricCalculator {
	private static final int METHOD_PACK_ROW = 0;
	private static final int METHOD_PACK_TABLE = 1;
	private static final int METHOD_TYPE_ROW = 2;
	private static final int METHOD_TYPE_TABLE = 3;
	
	static {
		initialize();
	}
	
	private static ArrayList<Pair<Method, Integer>> methods;
	private static ArrayList<Metric> metrics;
	
	static List<Metric> getMetrics() {
		return Collections.unmodifiableList(metrics);
	}
	
	static double getWeight(String metric) {
		for (Metric m : metrics)
			if (m.name.equals(metric))
				return m.weight;
		return 0;
	}
	
	public static MetricTable calculate(Design d) {
		List<Type> types = d.getTypes();
		List<Package> packages = d.getPackages();
		
		List<String> packNames = CollectionHelper.map(packages, new Delegate.Func1P<String, Package>() {
			@Override
			public String run(Package in) {
				return in.toString();
			}
		});
		
		List<String> rowNames = CollectionHelper.map(types, new Delegate.Func1P<String, Type>() {
			@Override
			public String run(Type in) {
				return in.toString();
			}
		});
		
		rowNames.addAll(packNames);
		
		List<String> colNames = CollectionHelper.map(metrics, new Delegate.Func1P<String, Metric>() {
			@Override
			public String run(Metric in) {
				return in.getName();
			}
		});
		
		MetricTable table = new MetricTable(rowNames, colNames);
		
		for (Pair<Method, Integer> pmt : methods) {
			Method m = pmt.getFirst();
			int mt = pmt.getSecond();
			
			for (int i = 0; i < types.size(); i++) {
				Type t = types.get(i);

				try {
					if (mt == METHOD_TYPE_TABLE)
						m.invoke(null, t, table);
					else if (mt == METHOD_TYPE_ROW)
						m.invoke(null, t, table.row(t));
				} catch (Exception e) {
					// This really should never happen.
					e.printStackTrace();
				}
			}
			
			for (int i = 0; i < packages.size(); i++) {
				Package p = packages.get(i);

				try {
					if (mt == METHOD_PACK_TABLE)
						m.invoke(null, p, table);
					else if (mt == METHOD_PACK_ROW)
						m.invoke(null, p, table.row(p));
				} catch (Exception e) {
					// This really should never happen.
					e.printStackTrace();
				}
			}
		}
		
		return table;
	}
	
	private static void initialize() {
		metrics = new ArrayList<MetricCalculator.Metric>();
		HashSet<String> classNames = new HashSet<String>();
		
		try {
			Log.config("Loading metrics.xml for metric configuration.");
			/* Equivalent of following three beautiful Java lines... in C#:
			 * XDocument.Load("data/metrics.xml"); */
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(new File("./data/metrics.xml"));
	        
	        NodeList categories = doc.getElementsByTagName("category");
	        for (int i = 0 ; i < categories.getLength(); i++) {
	        	Node category = categories.item(i);
	        	
	        	// Equivalent of this, in C#, would be: category.Attribute("name").Value
	        	String className = category.getAttributes().getNamedItem("class").getTextContent();
	        	// String name = category.getAttributes().getNamedItem("name").getTextContent(); // what are we gonna do with this?
	        	
	        	classNames.add(className);
	        	
	        	// BTW, I'm still wondering why this "NodeList" does not implement any of the Collection interfaces.
	        	// Or at least Iterable!
	        	NodeList cmetrics = ((Element)category).getElementsByTagName("metric");
	        	
	        	for (int j = 0; j < cmetrics.getLength(); j++) {
	        		Node metric = cmetrics.item(j);
	        		
		        	String mname = metric.getAttributes().getNamedItem("name").getTextContent();
		        	String mweight = metric.getAttributes().getNamedItem("weight").getTextContent();
		        	// C# has TryParse methods on these... In case you don't want to try and catch stuff all the time.
		        	double dmweight = Double.parseDouble(mweight);
		        	boolean pmetric = metric.getAttributes().getNamedItem("packagemetric") != null;
		        	
		        	metrics.add(new Metric(mname, dmweight, pmetric));
	        	}
	        }
	        
	        Log.config("Found %d defined metrics.", metrics.size());
	        
	        methods = new ArrayList<Pair<Method,Integer>>();
	        
	        for (String cname : classNames) {
	        	Class<?> c = Class.forName("edu.atilim.acma.metrics." + cname);
	        	
	        	for (Method m : c.getDeclaredMethods()) {
	        		Class<?>[] paramTypes = m.getParameterTypes();
	        		if (paramTypes.length != 2)	throw new Exception("Metric calculator methods should accept two parameters.");

	        		if (m.isAnnotationPresent(TypeMetric.class)) {
	        			if (paramTypes[0] != Type.class) throw new Exception("Type metric calculator methods should accept a type parameter.");
	        			int type = 0;
	        			if (paramTypes[1] == MetricTable.MetricRow.class)
	        				type = METHOD_TYPE_ROW;
	        			else if (paramTypes[1] == MetricTable.class)
	        				type = METHOD_TYPE_TABLE;
	        			else
	        				throw new Exception("Type metric calculator methods should accept a MetricTable or MetricRow.");
	        			
	        			methods.add(Pair.create(m, type));
	        		}
	        		
	        		if (m.isAnnotationPresent(PackageMetric.class)) {
	        			if (paramTypes[0] != Package.class) throw new Exception("Package metric calculator methods should accept a package parameter.");
	        			int type = 0;
	        			if (paramTypes[1] == MetricTable.MetricRow.class)
	        				type = METHOD_PACK_ROW;
	        			else if (paramTypes[1] == MetricTable.class)
	        				type = METHOD_PACK_TABLE;
	        			else
	        				throw new Exception("Package metric calculator methods should accept a MetricTable or MetricRow.");
	        			
	        			methods.add(Pair.create(m, type));
	        		}
	        	}
	        }
	        
	        Log.config("Found %d defined methods for metric calculations. Metric calculator is ready.", methods.size());
		} catch(Exception e) {
			System.out.println("Could not initialize metric calculator. Details:");
			e.printStackTrace();
		}
	}
	
	static class Metric {
		private String name;
		private double weight;
		private boolean packageMetric;
		
		public String getName() {
			return name;
		}
		
		public double getWeight() {
			return weight;
		}
		
		public boolean isPackageMetric() {
			return packageMetric;
		}

		public Metric(String name, double weight, boolean packageMetric) {
			this.name = name;
			this.weight = weight;
			this.packageMetric = packageMetric;
		}
	}
}
