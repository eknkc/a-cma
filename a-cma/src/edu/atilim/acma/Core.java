package edu.atilim.acma;

import java.io.IOException;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.DesignReader;
import edu.atilim.acma.mess.TestClass;
import edu.atilim.acma.metrics.MetricTable;
import edu.atilim.acma.search.Algorithm;
import edu.atilim.acma.search.HillClimbing;
import edu.atilim.acma.transition.DesignWrapper;

public class Core {
	public int fixMePlease = 0;
	public int fixMeTooPlease = 0;
	
	public static void main(String[] args) throws IOException {
		// Kendini oku...
		DesignReader dr = new DesignReader("./bin");
		Design d = dr.read();
		
		// An illusion of dependency on edu.atilim.acma.mess.TestClass.canYouFixMe field
		// It will not attempt to decrease it's security to private because we are accessing it from here, another package.
		TestClass tc = new TestClass();
		tc.canYouFixMe = 6;
		
		MetricTable initMetrics = d.getMetrics();
		initMetrics.writeCSV("output/metrics_init.csv");
		System.out.printf("Initial Score of Design: %f\n", initMetrics.getWeightedSum());
		
		Algorithm alg = new HillClimbing(0, 0);
		
		DesignWrapper fin = (DesignWrapper)alg.run(DesignWrapper.wrap(d));
		
		MetricTable finalMetrics = fin.getDesign().getMetrics();
		finalMetrics.writeCSV("output/metrics_final.csv");
		System.out.printf("Initial Score of Design: %f\n", finalMetrics.getWeightedSum());
		
		System.out.println("Metric tables have been written to output folder within the project directory.");
		
		System.out.println("\nRequired actions to perform:");
		
		for (String mod : fin.getDesign().getModifications()) {
			System.out.println(mod);
		}
		
	}
}
