<?php
require_once('xmlrpc.php');

class Acma {
	private $baseUrl;
	private $context;
	
	function __construct($baseUrl, $ctxt = null) {
		$this->baseUrl = $baseUrl;
		$this->context = $ctxt;
		
		if ($this->context) {
			$status = $this->getStatus();
			if ($status && $status['state'] != "NOT_EXISTS") {
				return;
			}
		}
		
		$this->context = $this->request("acma.createContext");
	}
	
	public function getContext() {
		return $this->context;
	}
	
	public function getStatus() {
		return $this->request("acma.getStatus", array(XMLRPC_prepare($this->context)));
	}
	
	public function setEmail($email) {
		return $this->request("acma.setEmail", array(XMLRPC_prepare($this->context), XMLRPC_prepare($email)));
	}
	
	public function startRefactoring($data) {
		return $this->request("acma.startRefactoring", array(XMLRPC_prepare($this->context), XMLRPC_prepare($data, 'struct')));
	}
	
	public function getResult($run) {
		return $this->request("acma.getResult", array(XMLRPC_prepare($this->context), XMLRPC_prepare($run)));
	}
	
	public function getActions() {
		return $this->request("acma.getActions", array(XMLRPC_prepare($this->context)));
	}
	
	public function getMetrics() {
		return $this->request("acma.getMetrics", array(XMLRPC_prepare($this->context)));
	}
	
	public function getConfig() {
		return $this->request("acma.getConfig", array(XMLRPC_prepare($this->context)));
	}
	
	public function setActionEnabled($name, $enabled = true) {
		return $this->request("acma.setActionEnabled", array(XMLRPC_prepare($this->context), XMLRPC_prepare($name), XMLRPC_prepare((bool)$enabled)));
	}
	
	public function setActionsEnabled($actions) {
		return $this->request("acma.setActionsEnabled", array(XMLRPC_prepare($this->context), XMLRPC_prepare($actions, 'array')));
	}
	
	public function setActionWeight($name, $weight = 1.0) {
		return $this->request("acma.setActionWeight", array(XMLRPC_prepare($this->context), XMLRPC_prepare($name), XMLRPC_prepare((double)$weight)));
	}
	
	public function setActionsWeight($data) {
		return $this->request("acma.setActionsWeight", array(XMLRPC_prepare($this->context), XMLRPC_prepare($data, 'struct')));
	}
	
	public function setMetricEnabled($name, $enabled = true) {
		return $this->request("acma.setMetricEnabled", array(XMLRPC_prepare($this->context), XMLRPC_prepare($name), XMLRPC_prepare((bool)$enabled)));
	}
	
	public function setMetricsEnabled($metrics) {
		return $this->request("acma.setMetricsEnabled", array(XMLRPC_prepare($this->context), XMLRPC_prepare($metrics, 'array')));
	}
	
	public function setMetricWeight($name, $weight = 1.0) {
		return $this->request("acma.setMetricWeight", array(XMLRPC_prepare($this->context), XMLRPC_prepare($name), XMLRPC_prepare((double)$weight)));
	}
	
	public function setMetricsWeight($data) {
		return $this->request("acma.setMetricsWeight", array(XMLRPC_prepare($this->context), XMLRPC_prepare($data, 'struct')));
	}
	
	public function putDesign($file) {
		if (!file_exists($file)) {
			throw new Exception("File does not exist!");
		}
	
		$handle = fopen($file, "rb");
		$contents = fread($handle, filesize($file));
		fclose($handle);
		
		return $this->request("acma.putDesign", array(XMLRPC_prepare($this->context), XMLRPC_prepare(base64_encode($contents), "base64")));
	}
	
	private function request($proc, $params = array()) {
		list($success, $response) = XMLRPC_request($this->baseUrl, '/xmlrpc', $proc, $params);
		if ($success)
			return $response;
		else {
			if ($response["faultString"] && isset($response["faultCode"])) throw new Exception($response["faultString"], $response["faultCode"]);
			else throw new Exception("Could not connect to server.", 0);
		}
	}
}
?>