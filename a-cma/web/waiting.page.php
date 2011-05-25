<h2>In Progress</h2>
<div id="content">
<br /><br /><br />
<center>Your refactoring process is in progress. We will send an e-mail to <strong><?php echo $acmaStatus['email']; ?></strong> as soon as it is finished.<br />
		You can also keep this window open to be able to get notified about results when the refactoring is completed<br /> Thank you.</center>
<br /><br /><br />
</div>
<script type="text/javascript">
$(document).ready(function() {
	window.setTimeout(function() {
		document.location.href = 'index.php?page=results&ts=' + (new Date()).getTime();
	}, 2500);
});
</script>