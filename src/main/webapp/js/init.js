// Load language phrases for javascript 
$.getJSON("/index.php/admin/init/jslanguage", function(data) {
   window.language = data;
 });


// Do some stuff on page load
$(function(){

    // Settings
    $.ajaxSetup({cache: false});


    // Find ajaxmodule divs, and load contents
    var ids = $("div[id^=ajaxmodule]").map(function() {return this.id;}).get();
    for (i=0;i<ids.length;i++) {
	ajax("/index.php/view/module/" + ids[i].replace("ajaxmodule_",""),"",ids[i],"");
    }

});
