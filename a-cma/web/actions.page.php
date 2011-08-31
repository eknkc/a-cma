<?php
if ($_POST['do'] && $_POST['do'] == 'Save') {
	$actions = $_POST['actions'];
	if (is_array($actions)) {
		$acma->setActionsEnabled($actions);
	}
	
	$weights = array();
	foreach ($_POST as $key => $value) {
		if (substr($key, 0, 7) == 'weight_') {
			$action = substr($key, 7);
			$weight = $value;
			
			$weights[$action] = $weight;
		}
	}
	$acma->setActionsWeight($weights);
}

if ($_POST['do'] && $_POST['do'] == 'Previous') {
	header('Location: index.php?page=metrics');
}

if ($_POST['do'] && $_POST['do'] == 'Next') {
	header('Location: index.php?page=refactoring');
}
	
$actionsData = $acma->getActions();

$descriptions = array(	'Introduce Factory' => 'Change the security of the constructors of a class to private and add a static method called create that has the same parameters of the constructor',
						'Move Down Field' => 'Move an instance field from a class to one of its child classes',
						'Move Up Method' => 'Move an instance method from a class to its immediate super class',
						'Decrease Field Security' => 'Change the security of a field from protected to public, package to protected or private to package',
						'Move Up Field' => 'Move an instance field from a class to its immediate super class',
						'Make Class Abstract' => 'Make a class abstract, if any instances of the class is not created directly using new key word',
						'Remove Field' => 'Remove a field of a class, only if it has no dependencies',
						'Move Method' => 'Move an instance method from its defining class to one of its parameter types, declaring the previous parent as a parameter',
						'Freeze Method' => 'Make a method static within the same class',
						'Remove Method' => 'Remove a method of a class, if this method has not been called from any other classes or methods',
						'Move Down Method' => 'Move an instance method from a class to one of its child classes',
						'Instantiate Method' => 'Move a static method to one of its parameter types as instance method',
						'Remove Interface' => 'Remove an interface from hierarchy, only if it has no dependencies',
						'Decrease Method Security' => 'Change the security of a method from protected to public, package to protected or private to package',
						'Inline Method' => 'Move an instance method of a class to its caller method, if this method has only one caller method',
						'Make Class Non Final' => 'Make a final class to non-final',
						'Remove Class' => 'Remove a class from hierarchy, only if there are no dependencies targeting this class',
						'Increase Method Security' => 'Change the security of a method from public to protected, protected to package or package to private',
						'Increase Field Security' => 'Change the security of a field from public to protected, protected to package or package to private',
						'Make Class Final' => 'Make a class final');
?>
<h2>Actions</h2>
<div id="content">
	<h4>Information</h4>
	<p>
		A-CMA lets you choose needed actions from the predefined available action table which will be used in refactoring phase. Please 
		put a check on the desired action name in the action table, then press save to keep your settings or press next to continue.
	</p>
	<h4>Action Table</h4>
	<form action="index.php?page=actions" method="POST">
		<table class="datatable">
			<tr>
				<th width="5%"><img src="inc/images/info.png" /></th>
				<th width="10%">Enabled</th>
				<th>Name</th>
				<th>Weight</th>
				<th width="15%">Applicible</th>
			</tr>
			<tr>
				<?php	
				for($i = 0; $i < count($actionsData); $i++){
					$action = $actionsData[$i];
					$class = ($i % 2) == 0 ? 'alt1' : 'alt2';
					
					echo '<tr class="'. $class . '">';
						echo '<td class="align-center"><img src="inc/images/info.png" dialogtitle="' . $action['splitname'] . '" title="' . $descriptions[$action['splitname']] . '" /></td>';
					
						if ($action['enabled'])
							echo '<td class="align-center"><input type="checkbox" name="actions[]" value="' .$action['name'] . '" checked /></td>';
						else
							echo '<td class="align-center"><input type="checkbox" name="actions[]" value="' .$action['name'] . '"/></td>';
							
						echo "<td>".$action['splitname']."</td>";
						
						echo '<td class="align-center">';
						echo '<select name="weight_' . $action['name'] . '">';
						for ($w = 0; $w < 10; $w+= 0.25) {
							$sel = $action['weight'] == $w ? ' selected' : '';
							echo '<option value="' . $w . '"' . $sel . '>' . number_format($w, 2) . '</option>';
						}
						echo '</select>';
						echo '</td>';
						
						echo '<td class="align-center">'.$action['applicible'].'</td>';
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