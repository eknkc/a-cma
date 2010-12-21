package edu.atilim.acma.metrics;

import java.util.List;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Package;
import edu.atilim.acma.design.Method.Parameter;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable.MetricRow;

public final class CouplingMetrics {
	@TypeMetric
	public static void calculateAttrMetrics(Type type, MetricRow row) {
		row.set("eC_Attr", type.getDependentFields().size());
		
		row.set("iC_Attr", 0);
		
		for (Field field : type.getFields()) {
			if (field.getType() != type) {
				row.increase("iC_Attr");
			}
		}
 	}
	
	@TypeMetric
	public static void calculateMethodMetrics(Type type, MetricRow row) {
		row.set("eC_Par", type.getDependentMethodsAsParameter().size());
		
		row.set("iC_Par", 0);
		
		for (Method method : type.getMethods()) {
			for (Parameter parameter : method.getParameters()) {
				if (type != parameter.getType())
					row.increase("iC_Par");
			}
		}
 	}
	
	@TypeMetric
	public static void calculateDependenceMetrics(Type type, MetricRow row) {
		
		int res=0;
		int numOfDepAttr= type.getDependentFields().size();
		int numOfDepMetAsParameter= type.getDependentMethodsAsParameter().size(); 
		int numOfDepMetAsInstantiator = type.getDependentMethodsAsInstantiator().size();
		res= numOfDepAttr + numOfDepMetAsParameter+numOfDepMetAsInstantiator ;
		
		row.set("Dep_In", res);
		
		row.set("Dep_Out", 0);
		
		for (Field field : type.getFields()) {
			if (field.getType() != type) {
				row.increase("Dep_Out");
			}
		}
		
		for (Method method : type.getMethods()) {
			for (Parameter parameter : method.getParameters()) {
				if (type != parameter.getType())
					row.increase("Dep_Out");
			}
		}
		
		
	}
	
	@TypeMetric
	public static void calculateAssocElementsMetrics(Package pack, MetricRow row) {
		
		List<Type> packTypes= pack.getTypes();
		int res_ssc=0;
		int numOfDepAttr_ssc=0;
		int numOfDepMetAsParameter_ssc=0;
		int res_nsb=0;
		int numOfDepAttr_nsb=0;
		int numOfDepMetAsParameter_nsb=0;
		
		
		for (Type type : pack.getTypes()) {
			for(Field field:type.getFields())
			{
				if(field.getType()!=type && packTypes.contains(field.getType()))
					numOfDepAttr_ssc++;
			}
			
			for (Method method : type.getMethods()) {
				for (Parameter parameter : method.getParameters()) {
					if (type != parameter.getType() && packTypes.contains(parameter.getType()))
						numOfDepMetAsParameter_ssc++;
				}
			}
			
		}
		
		res_ssc=numOfDepAttr_ssc+ numOfDepMetAsParameter_ssc;
		row.set("NumAssEl_ssc", res_ssc);
		
		
		for (Type type : pack.getTypes()) {
			for(Field field:type.getFields())
			{
				if(field.getType()!=type && !packTypes.contains(field.getType()))
					numOfDepAttr_nsb++;
			}
			
			for (Method method : type.getMethods()) {
				for (Parameter parameter : method.getParameters()) {
					if (type != parameter.getType() && !packTypes.contains(parameter.getType()))
						numOfDepMetAsParameter_nsb++;
				}
			}
			
		}
		
		res_nsb=numOfDepAttr_nsb+ numOfDepMetAsParameter_nsb;
		row.set("NumAssEl_nsb", res_nsb);
		
	}
}
