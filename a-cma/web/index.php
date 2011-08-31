<?php
ob_start();
session_start();

// Do not report errors.
error_reporting(E_ALL ^ E_NOTICE ^ E_DEPRECATED ^ E_WARNING);

// Get the requested action "?action=xxx". Default is 'design'
$page = $_GET['page'];
if (!$page) {
	$page = 'design';
}

// User wants to start over
if ($_GET['do'] == 'Start Over') {
	session_destroy();
	header('location: index.php');
	exit();
}

$cid = $_SESSION['contextId'];
$rid = $_GET['rid'];

if ($_GET['cid']) {
	$cid = $_GET['cid'];
}

// Create xml rpc service
require_once('acma.php');
try {
	$acma = new Acma("localhost:8081", $cid);
	$acmaStatus = $acma->getStatus(); // Store status for future use
	$_SESSION['contextId'] = $acma->getContext();
	
	if ($acmaStatus['state'] == 'READY')
		$page = 'refactoring';
		
	if ($rid)
		$page = 'results';
		
	if (!$acmaStatus['hasdesign'])
		$page = 'design';
		
} catch (Exception $e) {
	$page = 'error';
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>A-CMA Web Frontend</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
		<link rel="stylesheet" type="text/css" href="inc/redmond/jquery-ui-1.8.12.custom.css" />
		<link rel="stylesheet" type="text/css" href="inc/acma.css" />
		<script src="inc/jquery-1.6.min.js" type="text/javascript"></script>
		<script src="inc/jquery-ui-1.8.12.custom.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script src="inc/acma.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="container">
			<h1>Alpha CMA</h1>
			<ul id="navigation"> 
				<li>Web Frontend</li> 
			</ul>			
			<?php include($page.".page.php"); ?>
			<div id="footbuttons" class="align-right">
				<form action="index.php" method="GET">
					<input type="submit" name="do" value="Start Over" />
				</form>
			</div>
		</div>
	</body>
</html>
<?php
ob_end_flush();
?>