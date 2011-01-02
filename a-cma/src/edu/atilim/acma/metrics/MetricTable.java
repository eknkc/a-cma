package edu.atilim.acma.metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricTable {
	private double[] values;
	private HashMap<String, Integer> rowNames;
	private HashMap<String, Integer> colNames;
	
	private int index(String row, String col) {
		int i = rowNames.get(row);
		int j = colNames.get(col);
		
		return (i * colNames.size()) + j;
	}
	
	public Map<String, Integer> getCols() {
		return Collections.unmodifiableMap(colNames);
	}
	
	public Map<String, Integer> getRows() {
		return Collections.unmodifiableMap(rowNames);
	}
	
	public double getAverage(String metric) {
		int j = colNames.get(metric);
		
		double sum = 0;
		double cnt = 0;
		
		for (int i = 0; i < rowNames.size(); i++) {
			int index = (i * colNames.size()) + j;
				
			if (Double.isNaN(values[index]))
				continue;
			
			cnt++;
			sum += values[index];
		}
		
		if (cnt == 0) return 0;		
		return sum / cnt;
	}
	
	public double getWeightedSum() {
		double sum = 0;
		for (String c : colNames.keySet()) {
			double avg = getAverage(c);
			double wgh = MetricCalculator.getWeight(c);
			
			sum += avg * wgh;
		}
		return sum;
	}
	
	public double get(String element, String metric) {
		return values[index(element, metric)];
	}
	
	public double get(int row, int col) {
		return values[row * colNames.size() + col];
	}
	
	public void set(String element, String metric, double value) {
		values[index(element, metric)] = value;
	}
	
	public void add(String element, String metric, double value) {
		double cur = get(element, metric);
		if (cur == Double.NaN)
			cur = 0;
		set(element, metric, cur + value);
	}
	
	public MetricRow row(String key) {
		return new MetricRow(key);
	}
	
	public MetricRow row(Object key) {
		return new MetricRow(key.toString());
	}
	
	public MetricTable(List<String> rowNames, List<String> colNames) {
		this.rowNames = new HashMap<String, Integer>();
		this.colNames = new HashMap<String, Integer>();
		
		for (int i = 0; i < rowNames.size(); i++) {
			this.rowNames.put(rowNames.get(i), i);
		}
		
		for (int i = 0; i < colNames.size(); i++) {
			this.colNames.put(colNames.get(i), i);
		}
		
		this.values = new double[rowNames.size() * colNames.size()];
		for (int i = 0; i < this.values.length; i++)
			this.values[i] = Double.NaN;
	}
	
	public void writeCSV(String fileName) throws IOException {
		NumberFormat nf = NumberFormat.getInstance();
		List<String> cols = new ArrayList<String>(colNames.keySet());
		List<String> rows = new ArrayList<String>(rowNames.keySet());
		
		Collections.sort(cols);
		Collections.sort(rows);
		
		StringBuilder sb = new StringBuilder(";");
		
		for (String c : cols)
			sb.append(c).append(';');
		
		sb.setLength(sb.length() - 1);
		sb.append("\r\n");
		
		for (String r : rows) {
			sb.append(r).append(';');
			
			for (String c : cols) {
				sb.append(nf.format(get(r, c))).append(';');
			}
			
			sb.setLength(sb.length() - 1);
			sb.append("\r\n");
		}
		
		sb.append("AVERAGES;");
		for (String c : cols)
			sb.append(nf.format(getAverage(c))).append(';');
		
		sb.setLength(sb.length() - 1);
		sb.append("\r\n");
		
		sb.append("WEIGHTS;");
		for (String c : cols)
			sb.append(nf.format(MetricCalculator.getWeight(c))).append(';');
		
		sb.setLength(sb.length() - 1);
		sb.append("\r\n");
		
		sb.append("WEIGHTED SUM;").append(nf.format(getWeightedSum()));
		
		FileWriter fw = new FileWriter(fileName);
		fw.write(sb.toString());
		fw.flush();
		fw.close();
	}
	
	public class MetricRow {
		private String element;
		
		MetricRow(String element) {
			this.element = element;
		}
		
		public double get(String metric) {
			return MetricTable.this.get(element, metric);
		}
		
		public void set(String metric, double value) {
			MetricTable.this.set(element, metric, value);
		}
		
		public void add(String metric, double value) {
			MetricTable.this.add(element, metric, value);
		}
		
		public void increase(String metric) {
			add(metric, 1.0);
		}
		
		public MetricCell cell(String metric) {
			return new MetricCell(metric);
		}
		
		public class MetricCell {
			private String metric;

			MetricCell(String metric) {
				this.metric = metric;
			}
			
			public double get() {
				return MetricRow.this.get(metric);
			}
			
			public void set(double value) {
				MetricRow.this.set(metric, value);
			}
			
			public void add(double value) {
				MetricRow.this.add(metric, value);
			}
		}
	}
}
