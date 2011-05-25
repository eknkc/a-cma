<?php
$error = false;

if ($_FILES['design'] && $_POST['email']) {
	$email = $_POST['email'];
	if(eregi("^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})$", $email)) { 
		//Create a temporary file to store the uploaded file
		$tempName = "./temp/" . uniqid() . ".temp";
		
		// Check if everything works fine
		if (move_uploaded_file($_FILES['design']['tmp_name'], $tempName)) {
			try {
				$acma->putDesign($tempName); // Send the file to web service
				$acma->setEmail($email);
			} catch (Exception $e) { $error = 1; }
			unlink($tempName); // Delete the temporary file
			if (!$error) {
				header('location: index.php?action=design'); // Reload page
				exit();
			}
		} else { // There is an error
			$error = 1;
		}
	} else {
		$error = 2;
	}
}
?>
<h2>Design</h2>
<div id="content">
	<h4>Information</h4>
	<p>
		A-CMA lets you apply automated refactoring on any Java based software. You don't need to upload your source codes, just the compiled binaries. If you are interested,
		please use the form below and submit a zip file containing all the compiled <strong>.class</strong> files of your project. 
		After that, you will be able to choose the refactoring methodology and the settings and start a refactoring process. The process will be handled by our servers and you will be notified about the results as soon as they are available.
	</p>
	<?php if($acmaStatus['hasdesign']) { // User has an uploaded design, show info ?>
	<h4>Current Design</h4>
	<center>
		<table class="width-3quarters datatable">		
			<tr>
				<th class="align-right width-half">Context ID:</th>
				<td><?php echo $acma->getContext(); ?></td>			
			</tr>
			<tr>
				<th class="align-right width-half">Email:</th>
				<td><?php echo $acmaStatus['email']; ?></td>			
			</tr>			
			<tr>
				<th class="align-right width-half">Number of Types:</th>
				<td><?php echo $acmaStatus['dinfo']['types']; ?></td>			
			</tr>
			<tr>
				<th class="align-right width-half">Number of Packages:</th>
				<td><?php echo $acmaStatus['dinfo']['packages']; ?></td>			
			</tr>			
		</table>
	</center>
	<hr />
	<table width="100%">
		<tr>
			<td width="33%"></td>
			<td class="align-center"></td>
			<td width="33%" class="align-right"><button onclick="document.location.href='index.php?page=metrics'">Next</button></td>
		</tr>
	</table>
	<?php } ?>
	<h4>New Design Submission</h4>
	<form action="index.php?page=design" method="POST" enctype="multipart/form-data">
		<?php if ($error == 1) { ?>
		<p class="ui-state-error">
			<strong>Error: </strong> There has been a problem during design uploading. Please make sure that you are uploading a zip file containing <strong>.class</strong> files.
		</p>	
		<?php } ?>
		<?php if ($error == 2) { ?>
		<p class="ui-state-error">
			<strong>Error: </strong> Please enter a valid E-Mail address.
		</p>	
		<?php } ?>		
		<br />
		<center>
			<hr />E-Mail Address: <input type="text" name="email" />
			<hr />
			Design File: <input type="file" name="design" />
			<hr />
			<input type="submit" value="Submit" />
			<hr />
		</center>
		<br />
	</form>
</div>