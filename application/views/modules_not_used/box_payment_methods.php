<?PHP
$payment_method = $this->Service_layer->get_payment_method_list($website->id);
?>


<div id="module_paymentmethods" class="box">
<?
if($payment_method) {
    foreach($payment_method as $object) {
 	if ($object->image_url) {
 		echo("<img src=\"$object->image_url\">");
 	}
    }
}
?>
</div>