<div class="box">
    
	<?if ($user) {?>
		<a href="/?page=login&mode=logout"><b><?=lang("module_13_log_off")?></b></a> <?=$user->name?>
	<?} else {?>
		<a href="/?page=login"><b><?=lang("module_13_log_on")?></b></a> <?=lang("module_13_guest")?>
	<?}?>
</div>
