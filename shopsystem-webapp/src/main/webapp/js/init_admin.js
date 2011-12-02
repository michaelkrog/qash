// Load language phrases for javascript 
$.getJSON("/index.php/admin/init/jslanguage", function(data) {
   window.language = data;
 });

$(function(){
    $.ajaxSetup({cache: false});


    // Navigation
    $("#navigation").tabs("#navigation div div", {tabs: 'h2', effect: 'slide', initialIndex: null});

    // Tooltip test
    //$(".current").tooltip({opacity: 0.7});

    // Load overview
    //ajax("/admin/overview.asp","","section_mid","");

});
