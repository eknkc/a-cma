package edu.atilim.acma;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.ZIPDesignReader;
import edu.atilim.acma.search.Algorithm;
import edu.atilim.acma.search.BeamSearch;
import edu.atilim.acma.search.HillClimbing;
import edu.atilim.acma.search.ModSimAnn;
import edu.atilim.acma.transition.DesignWrapper;
import edu.atilim.acma.ui.MainWindow;

public class Core {
	public static final String version = "0.0.0.5a";
	
	public static void main(String[] args) throws IOException {
		System.out.printf("A-CMA Software Refactoring Tool - version %s\n\n", version);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				int resp = JOptionPane.showConfirmDialog(null, "Test GUI mode?", "GUI mi acaba?", JOptionPane.YES_NO_OPTION);
				if (resp == JOptionPane.YES_OPTION) {
					MainWindow.getInstance().setVisible(true);
				} else {
					try {
						consoleRun();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private static void consoleRun() throws Exception {
		Design design = chooseDesign();
		
		design.getMetrics().writeCSV("./output/metrics_initial.csv");
		
		System.out.println();
		System.out.printf("Initial design has been read. Statistics:\n");
		printBasicInfo(design);
		System.out.println();
		System.out.println();
		
		Design finalDesign = runAlgorithm(design);

		System.out.printf("Final design has been obtained. Statistics:\n");
		printBasicInfo(finalDesign);
		finalDesign.getMetrics().writeCSV("./output/metrics_final.csv");
		
		System.out.println("Would you like to view required actions to reach final design? [Y]es, [N]o: ");
		String resp = readLine();
		if (resp.startsWith("y") || resp.startsWith("Y")) {
			System.out.printf("%d modifications has been made\n", finalDesign.getModifications().size());
			for (String mod : finalDesign.getModifications())
				System.out.println(mod);
		}
		
		System.out.println();
		System.out.println("Press enter to exit");
		readLine();
		
		System.exit(0);
	}
	
    private static Design chooseDesign() {
        System.out.println("Found benchmark software:");
        
        ArrayList<File> files = new ArrayList<File>();
        File bmdir = new File("./data/benchmarks");
        
        for (File f : bmdir.listFiles()) {
                if (!f.isDirectory() && f.getName().endsWith(".zip"))
                        files.add(f);
        }
        
        for (int i = 0; i < files.size(); i++)
                System.out.printf("[%d] %s\n", i, files.get(i).getName());
        
        System.out.print("Please choose the initial design to run refactorings on: ");
        
        String line = readLine();
        int id = -1;
        
        do {
                try { id = Integer.parseInt(line); } catch (NumberFormatException e) {
                        System.out.println("Please enter the number of benchmark software.");
                }
                if (id >= files.size()) {
                        id = -1;
                        System.out.println("Please enter the number of benchmark software.");
                }
        } while (id < 0);
        
        return new ZIPDesignReader(files.get(id).getAbsolutePath()).read();
}
	
	private static Design runAlgorithm(Design design) {
		Algorithm alg = null;
		
		System.out.println("Available algorithms:");
		System.out.println("[0] Hill Climbing");
		System.out.println("[1] Simulated Annealing");
		System.out.println("[2] Beam Search");
		
		int selection = readUInt("Please enter the id of algorithm to run: ", 2);
		
		switch(selection) {
		case 0:
			int resCount = readUInt("Please enter the restart count: ", 10000);
			int resDepth = readUInt("Please enter the restart randomization depth: ", 10000);
			alg = new HillClimbing(resCount, resDepth);
			break;
		case 1:
			int maxIterations = readUInt("Please enter the maximum iterations: ", 100000);
			alg = new ModSimAnn(maxIterations);
			break;
		case 2:
			int beamLength = readUInt("Please enter the beam length: ", 10000);
			int ranDepth = readUInt("Please enter the initial randomization depth: ", 1000);
			alg = new BeamSearch(beamLength, ranDepth);
			break;
		}
		
		DesignWrapper iwrapper = DesignWrapper.wrap(design);
		
		long stime = System.currentTimeMillis();
		DesignWrapper fwrapper = (DesignWrapper)alg.run(iwrapper);
		long ftime = System.currentTimeMillis();
		
		System.out.printf("Algorithm completed in %.2f seconds.\n", (ftime - stime) / 1000.0);
		
		return fwrapper.getDesign();
	}
	
	private static void printBasicInfo(Design design) {
		System.out.printf("Number of packages: %d.\n", design.getPackages().size());
		System.out.printf("Number of types: %d.\n", design.getTypes().size());
		System.out.printf("Metric score: %f.\n", design.getMetrics().getWeightedSum());
		System.out.printf("Possible actions: %d.\n", design.getPossibleActions().size());
	}
	
	private static String readLine() {
		try {
			return new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private static int readUInt(String ask, int max) {
		int i = -1;
		do {
			System.out.println(ask);
			try { i = Integer.parseInt(readLine()); } catch (NumberFormatException nfe) { }
		} while(i < 0 || i > max);
		return i;
	}
}
