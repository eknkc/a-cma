<?php
if ($_POST['do'] && $_POST['do'] == 'Save') {
	$metrics = $_POST['metrics'];
	if (is_array($metrics)) {
		$acma->setMetricsEnabled($metrics);
	}
	
	$weights = array();
	foreach ($_POST as $key => $value) {
		if (substr($key, 0, 7) == 'weight_') {
			$metric = substr($key, 7);
			$weight = $value;
			
			$weights[$metric] = $weight;
		}
	}
	$acma->setMetricsWeight($weights);
}

if ($_POST['do'] && $_POST['do'] == 'Previous') {
	header('Location: index.php?page=design');
}

if ($_POST['do'] && $_POST['do'] == 'Next') {
	header('Location: index.php?page=actions');
}
	
$data = $acma->getMetrics();

$descriptions = array(	'avrgFieldVisibility' => 'The average number of the fields visibility of a class',
						'NOC' => 'The number of children of a class',
						'numInterf' => 'The number of interfaces in a package',
						'abstractness' => 'The ratio of abstract classes in a package',
						'packagenesting' => 'Nesting level of a package',
						'getters' => 'The number of getter methods in a class',
						'NumAssEl_ssc' => 'The number of associated elements in the same scope (namespace) as the class',
						'numOps' => 'The number of methods in a class',
						'NumAssEl_nsb' => 'The number of associated elements not in the same scope branch as the class',
						'iC_Attr' => 'The number of attributes in the class having another class or interface as their type',
						'staticness' => 'The average number of the static methods of a class',
						'numCls' => 'The number of classes in a package',
						'numFields' => 'The number of the fields in a class',
						'NumDesc' => 'The number of descendents of a class',
						'numOpsCls' => 'The number of the methods of a class in a package',
						'iC_Par' => 'The number of parameters in the class having another class or interface as their type',
						'eC_Par' => 'The number of times the class is externally used as parameter type',
						'Dep_In' => 'The number of elements that depend on this class',
						'avrgMethodVisibility' => 'The average number of the methods visibility of a class',
						'Dep_Out' => 'The number of elements on which this class depends',
						'setters' => 'The number of setter methods in a class',
						'iFImpl' => 'The number of interfaces a class implements',
						'eC_Attr' => 'The number of times the class is externally used as attribute type',
						'nesting' => 'The nesting level of a class',
						'numConstants' => 'The number of constant fields in a class',
						'NumAnc' => 'The number of ancestors of a class');
?>
<h2>Metrics</h2>
<div id="content">
	<h4>Information</h4>
	<p>
		A-CMA lets you choose needed metrics from the predefined available metric table which will be used in refactoring phase. Please 
		put a check on the desired metric name in the metric table, then press save to keep your settings or press next to continue.
	</p>
	<h4>Metric Table</h4>
	<form action="index.php?page=metrics" method="POST">
		<table class="datatable">
			<tr>
				<th width="5%"><img src="inc/images/info.png" /></th>
				<th width="10%">Enabled</th>
				<th>Name</th>
				<th>Weight</th>
				<th>Value</th>
				<th>Category</th>
				<th>Domain</th>
			</tr>
			<tr>
				<?php
				for($i = 0; $i < count($data); $i++){
					$metric = $data[$i];
					
					$class = ($i % 2) == 0 ? 'alt1' : 'alt2';
					
					echo '<tr class="'. $class . '">';
						echo '<td class="align-center"><img src="inc/images/info.png" dialogtitle="' . $metric['name'] . '" title="' . $descriptions[$metric['name']] . '" /></td>';
						
						if ($metric['enabled'])
							echo '<td class="align-center"><input type="checkbox" name="metrics[]" value="' .$metric['name'] . '" checked /></td>';
						else
							echo '<td class="align-center"><input type="checkbox" name="metrics[]" value="' .$metric['name'] . '"/></td>';
							
						echo '<td>'.$metric['name']."</td>";
						
						echo '<td class="align-center">';
						echo '<select name="weight_' .$metric['name'] . '">';
						for ($w = 0; $w < 10; $w+= 0.25) {
							$sel = $metric['weight'] == $w ? ' selected' : '';
							echo '<option value="' . $w . '"' . $sel . '>' . number_format($w, 2) . '</option>';
						}
						echo '</select>';
						echo '</td>';
						
						echo '<td class="align-center">'.number_format($metric['value'], 3)."</td>";
						
						if($metric['minimized'])
							echo '<td class="align-center">Minimized</td>';
						else
							echo '<td class="align-center">Unknown</td>';
						
						if($metric['package'])
							echo '<td class="align-center">Package</td>';
						else
							echo '<td class="align-center">Type</td>';

					echo "</tr>";
				}
				?>
			</tr>
		</table>
		<hr />
		<table width="100%">
			<tr>
				<td width="33%"><input type="submit" name="do" value="Previous" /></td>
				<td class="align-center"><input type="submit" name="do" value="Save" /></td>
				<td width="33%" class="align-right"><input type="submit" name="do" value="Next" /></td>
			</tr>
		</table>
	</form>
</div>
