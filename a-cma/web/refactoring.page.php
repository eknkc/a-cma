<?php
if (!$acmaStatus['hasdesign']) header('Location: index.php');

if ($_POST['algorithm'] && $_POST['algorithm'] == 'Start MSD') {
	$randomRestarts = intval($_POST['msdrandomRestarts']);
	$restartDepth = intval($_POST['msdrestartDepth']);
	
	if ($acma->startRefactoring(array('algorithm' => 'MSD', 'randomRestarts' => $randomRestarts, 'restartDepth' => $restartDepth))) {
		header('Location: index.php?page=refactoring');
	}
}

if ($_POST['algorithm'] && $_POST['algorithm'] == 'Start ABC') {
	$population = intval($_POST['abcpopulation']);
	$iterations = intval($_POST['abciterations']);
	
	if ($acma->startRefactoring(array('algorithm' => 'ABC', 'population' => $population, 'iterations' => $iterations))) {
		header('Location: index.php?page=refactoring');
	}
}

if ($_POST['algorithm'] && $_POST['algorithm'] == 'Start LBS') {
	$population = intval($_POST['lbspopulation']);
	$randomDepth = intval($_POST['lbsrandomDepth']);
	$iterations = intval($_POST['lbsiterations']);
	
	if ($acma->startRefactoring(array('algorithm' => 'LBS', 'population' => $population, 'randomDepth' => $randomDepth, 'iterations' => $iterations))) {
		header('Location: index.php?page=refactoring');
	}
}

if ($_POST['algorithm'] && $_POST['algorithm'] == 'Start SBS') {
	$population = intval($_POST['sbspopulation']);
	$randomDepth = intval($_POST['sbsrandomDepth']);
	$iterations = intval($_POST['sbsiterations']);
	$boltzman = $_POST['sbsdistribution'] == "Boltzman" ? true : false;
	
	if ($acma->startRefactoring(array('algorithm' => 'SBS', 'population' => $population, 'randomDepth' => $randomDepth, 'iterations' => $iterations, 'boltzman' => $boltzman))) {
		header('Location: index.php?page=refactoring');
	}
}
?>
<h2>Refactoring</h2>
<div id="content">
	<h4>Past / Current Runs (Hit F5 ro refresh)</h4>
	<?php if (count($acmaStatus['runs']) == 0) { ?>
	<br /><center>No runs have been registered. Please Start one below.</center><br />
	<?php } else { ?>
		<table class="datatable">
			<tr>
				<th width="20%">Date</th>
				<th>Name</th>
				<th width="10%">Status</th>
			</tr>
			<?php 
			foreach($acmaStatus['runs'] as $run) {
				$cname = '';
				if ($run['state'] == 'RUNNING') $cname = 'run-running';
				if ($run['state'] == 'COMPLETED') $cname = 'run-completed';
			?>
				<tr>
					<td class="<?php echo $cname; ?>"><?php echo $run['date']; ?></td>
					<td class="<?php echo $cname; ?>"><?php echo $run['name']; ?></td>
					<td class="<?php echo $cname; ?> align-center">
					<?php if ($run['state'] == 'COMPLETED') { ?>
						<button onclick="document.location.href='index.php?rid=<?php echo $run['id']; ?>'">Results</button>
					<?php } else { echo $run['state']; } ?>
					</td>
				</tr>
			<?php } ?>
		</table>
	<?php } ?>
	<h4>Start New Refactoring</h4>
	<form action="index.php?page=refactoring" method="POST">
		<div id="algorithms">
			<h3><a href="#">Multiple Steepest Descend</a></h3>
			<div>
				<table width="100%" class="datatable">
					<tr>
						<th class="align-right width-half">Random Restarts:</th>
						<td><select name="msdrandomRestarts"><option>10</option><option>20</option><option selected>30</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Restart Depth:</th>
						<td><select name="msdrestartDepth"><option selected>5</option><option>10</option><option>15</option></td>	
					</tr>
					<tr>
						<td colspan="2" class="align-center"><input type="submit" name="algorithm" value="Start MSD"></td>	
					</tr>				
				</table>
			</div>
			<h3><a href="#">Artificial Bee Colony</a></h3>
			<div>
				<table width="100%" class="datatable">
					<tr>
						<th class="align-right width-half">Population:</th>
						<td><select name="abcpopulation"><option>20</option><option>40</option><option selected>60</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Iterations:</th>
						<td><select name="abciterations"><option selected>100</option><option>2500</option><option>5000</option><option>7500</option><option value="2147483647">Exhaust Search Space</option></td>	
					</tr>				
					<tr>
						<td colspan="2" class="align-center"><input type="submit" name="algorithm" value="Start ABC"></td>	
					</tr>				
				</table>
			</div>
			<h3><a href="#">Local Beam Search</a></h3>
			<div>
				<table width="100%" class="datatable">
					<tr>
						<th class="align-right width-half">Population:</th>
						<td><select name="lbspopulation"><option>20</option><option>40</option><option selected>60</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Randomization Depth:</th>
						<td><select name="lbsrandomDepth"><option selected>5</option><option>10</option><option>15</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Iterations:</th>
						<td><select name="lbsiterations"><option selected>100</option><option>250</option><option>500</option><option>750</option><option>1000</option><option value="2147483647">Exhaust Search Space</option></td>	
					</tr>						
					<tr>
						<td colspan="2" class="align-center"><input type="submit" name="algorithm" value="Start LBS"></td>	
					</tr>				
				</table>
			</div>
			<h3><a href="#">Stochastic Beam Search</a></h3>
			<div>
				<table width="100%" class="datatable">
					<tr>
						<th class="align-right width-half">Population:</th>
						<td><select name="sbspopulation"><option>20</option><option>40</option><option selected>60</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Randomization Depth:</th>
						<td><select name="sbsrandomDepth"><option selected>5</option><option>10</option><option>15</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Iterations:</th>
						<td><select name="sbsiterations"><option selected>100</option><option>250</option><option>500</option><option>750</option><option>1000</option></td>	
					</tr>
					<tr>
						<th class="align-right width-half">Distribution:</th>
						<td><select name="sbsdistribution"><option selected>Gibb's</option><option>Boltzman</option></td>	
					</tr>					
					<tr>
						<td colspan="2" class="align-center"><input type="submit" name="algorithm" value="Start SBS"></td>	
					</tr>				
				</table>
			</div>			
		</div>
	</form>
	<table width="100%">
		<tr>
			<td width="33%"><button onclick="document.location.href='index.php?page=actions'">Previous</button></td>
			<td class="align-center"></td>
			<td width="33%" class="align-right"></td>
		</tr>
	</table>	
</div>