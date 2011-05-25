<?php
$result = $acma->getResult($rid);
?>
<h2>Run Result</h2>
<div id="content">
	<h4>Summary</h4>
	<table class="width-full datatable">		
		<tr>
			<th class="align-right width-quarter">Run Info:</th>
			<td><?php echo $result['runinfo']; ?></td>			
		</tr>
		<tr>
			<th class="align-right width-quarter">Time Taken:</th>
			<td><?php echo $result['timetakenms'] / 1000.0; ?> seconds</td>			
		</tr>
		<tr>
			<th class="align-right width-quarter">Expanded Designs:</th>
			<td><?php echo $result['nodeexpansion']; ?> distinct designs throughtout the search</td>			
		</tr>
		<tr>
			<th class="align-right width-quarter">Initial Metric Score:</th>
			<td><?php echo number_format($result['initial']['score'], 4); ?></td>			
		</tr>	
		<tr>
			<th class="align-right width-quarter">Final Metric Score:</th>
			<td><?php echo number_format($result['final']['score'], 4); ?></td>			
		</tr>	
		<tr>
			<th class="align-right width-quarter">Quality Gain:</th>
			<?php
				$gain = $result['initial']['score'] - $result['final']['score'];
				$percentage = $gain / $result['initial']['score'];
			?>
			<td><?php echo number_format($gain, 4); ?> <strong>(<?php echo number_format($percentage * 100, 2); ?>%)</strong></td>			
		</tr>			
	</table>
	<table width="100%">
		<tr>
			<td width="33%"></td>
			<td class="align-center"><button onclick="document.location.href='index.php?page=refactoring'">Go Back To Refactoring Page</button></td>
			<td width="33%" class="align-right"></td>
		</tr>
	</table>	
	<h4>Refactoring Actions</h4>
	<table class="width-full datatable">		
		<tr>
			<th width="5%">#</th>
			<th width="15%">Action</th>
			<th width="65%">Description</th>
			<th width="15%">Metric Score</th>			
		</tr>
		<?php 
			$i = 0; 
			foreach ($result['final']['appliedactions'] as $action) { 
				$i++; 
		?>
				<tr>
					<td class="align-center"><?php echo $i; ?></td>
					<td class="align-right"><?php echo $action[1]; ?></td>
					<td><?php echo wordwrap($action[2], 100, "<br/>", 1); ?></td>
					<td class="align-center"><?php echo $action[0]; ?></td>			
				</tr>
	
		<?php } ?>
	</table>
	<h4>Run Process</h4>
	<center>Below, you can see the run progress of your refactoring action over time. Lower scores represent better designs.</center>
	<div id="process_div" style="border: 1px solid black;">
	</div>
	<br />
	<h4>Metric Optimization</h4>
	<center>Below, you can see the change in metrics after the run. Red pieces shows the optimized part of a specific metric.</center>
	<div id="metric_div" style="border: 1px solid black;">
	</div>
</div>
<script type="text/javascript">
	google.load("visualization", "1", {packages:["corechart"]});
	google.setOnLoadCallback(drawCharts);
	function drawCharts() {
		drawProcess();
		drawMetric();
	}

	function drawProcess() {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Score');
		data.addColumn('number', 'Score');
		
		<?php foreach ($result['final']['appliedactions'] as $action) { ?>
			data.addRow(["", <?php echo $action[0] ?>]);
		<?php } ?>

		var chart = new google.visualization.LineChart(document.getElementById('process_div'));
		chart.draw(data, {height: 240, legend: 'none', chartArea: { left: 50, width: 720 }, hAxis: {title: "Time"}, vAxis: {title: "Metric Score"}});
	}
	
	function drawMetric() {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Score');
		data.addColumn('number', 'Base');
		data.addColumn('number', 'Difference');
		
		<?php foreach ($result['final']['metrics'] as $key => $metric) { 
			$base = min($metric, $result['initial']['metrics'][$key]);
			$diff = abs($metric - $result['initial']['metrics'][$key]);
		?>
			data.addRow(["<?php echo $key; ?>", <?php echo $base; ?>, <?php echo $diff; ?>]);
		<?php } ?>

		var chart = new google.visualization.ColumnChart(document.getElementById('metric_div'));
		chart.draw(data, {height: 350, legend: 'none', isStacked: true, chartArea: { left: 50, width: 720, top: 20 }, vAxis: {title: "Metric Score"}, hAxis: {slantedTextAngle: 75, textStyle: {fontSize: 10}}});
	}	
</script>