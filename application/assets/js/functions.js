// Load language



// Convert RGB css values to HEX
var HexLookup = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"];

function RGB2Hex(rgb) {
    var Hex = "";
    while (rgb > 0) {
        Hex = HexLookup[rgb % 16] + Hex;
        rgb -= (rgb % 16);
        rgb /= 16;
    }
    return Hex;
}


function RGBcss2Hex(str) {
    var hex = str;
    
    if(str) {
	// Is this an RGB string?
	if (str.indexOf("rgb(", 0) == 0) {
	    var vals = str.match(/\d+/g);
	    hex = "";
	    for (var i = 0; i < vals.length; i++) {
	        hex += RGB2Hex(vals[i]);
	    }
		
		hex = "#" + hex;
	}
    }
    return hex;
    
}


$.fn.serializeObject = function()
{
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
};


function show_message(text,mode) {

	if (mode == "working") {
	    text = "<img src='/application/assets/graphics/global/reading.gif'>";
	}

	if (mode != "hide") {
	    $("#message").html(text);
	}

	if(mode == "show" || mode == "working" || mode == "alert") {
		$("#message").fadeTo(200, 1);
		//$("#page_container").fadeTo(200, 0.25);
	}

	if(mode == "alert") {
	    $("#message").delay(3000).fadeTo(200, 0.25).delay(300).slideUp(200);
	    //setTimeout(show_message(text,"hide"),3000);
	}

	if(mode == "hide") {
		$("#message").delay(1000).fadeTo(200, 0.25).delay(300).slideUp(200);
		//$("#page_container").fadeTo(200, 1);
	}
}


function show_dialog() {

    $("#dialog").overlay({
	mask: {
		color: '#ebecff',
		loadSpeed: 200,
		opacity: 0.6
	},
	closeOnClick: false
    });

$("#dialog").data("overlay").load();
}

function hide_dialog() {
    $("#dialog").data("overlay").close();
}


function ajax(posturl,postdata,result_element,VisualActions) {

	var action_alert = false;
	var action_reload = false;
	var action_confirm = false;
	var action_hide_dialog = false;
	var action_hide_elements = false;
	var action_update = false;


	if(VisualActions) {
	    var array_actions = VisualActions.split(","); // Provide a string of actions to process if succeded
	
	    for (var i = array_actions.length-1; i >= 0; --i ){
		if (!array_actions[i] == "") {
		    eval("action_" + array_actions[i] + " = true;");
		}
	    }
	}

        var confirmed = true; //default
	if(action_confirm == true) {
		if(!confirm("Er du sikker?")) {
		    confirmed = false;
		}
	}

	 if (action_alert == true) {
	    show_message("","working");
	}
				  
	 //if(result_element == "") {
	 //    result_element = "section_mid"
	 //}

        if(result_element == 'section_mid') {
		// Save mainpage location if a reload is requested later
		window.page_posturl = posturl;
		window.page_postdata = postdata;
	}

        if(confirmed == true) {

	    if(result_element == 'dialog') {
		show_dialog();
	    }

            if (!result_element == "") {
                $("#" + result_element).html("<img src='/application/assets/graphics/global/reading.gif' style='width:15px;'>");
	    }
	    else {
		show_message("","working");
	    }

	    // Close action box in case it is open
	    show_actions(0);

            $.ajax({
                    type: "POST",
                    url: posturl,
                    data: postdata,
                    cache: false,
                    success: function(msg) {

			    if(msg.indexOf("A PHP Error was encountered") > -1) {
                                    show_message("Error: " + window.language["server_failed"] + "<br /><br />" + msg,"show");
                            }
                            else {
				    if (!result_element == "") {
                                        msg = msg.replace("pageload:ok","")
					$("#" + result_element).html(msg);
					eval($("#" + result_element + " script").html());
				    }
				    else {
					show_message("","hide");
				    }

                                    if (action_alert == true) {
					show_message("","hide");
				    }

				    if (action_reload == true) {
                                        ajax(window.page_posturl,window.page_postdata,"section_mid","")
				    }

				    if (action_hide_elements == true) {
					$("input[name='" + window.checkbox_grid + "']").each(function() {
					    if ($(this).attr('checked')?1:0) {
						// Hide elements with id of the checkbox value
						$("." + $(this).val()).hide();
					    }
					});
				    }

				    if (action_hide_dialog == true) {
					hide_dialog();
				    }

				    if (action_update == true) {
					// Update inner html of elements throughout page, with posted form data
					postdata_array = postdata.split("&");
					for (var i=2;i<postdata_array.length;i++) {
					    element_array = postdata_array[i].split("=");
					    $(".object_" + $("#form input[name=object]").val() + "_" + $("#form input[name=id]").val() + "_" + element_array[0]).html( $("#form input[name=" + element_array[0] + "]").val() );
					}
				    }


				    //if (result_element == "") {
					//show_message("","hide");
				    //}
                            }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
			show_message("Error: " + errorThrown + " / " + textStatus + " / " + XMLHttpRequest,"alert");
			//window.language["server_failed"] + "<br /><br />
		}
            });
       }
}


function show_actions(status) {
    if ((window.action_status != 1.00 && status != 0) || status == 1)
	window.action_status = 1.00;
    else
	window.action_status = 0;

    $('#actions').fadeTo(200,window.action_status);
}


function actions(id, value, result_element, actions) {

	// Check og uncheck all checkboxes
	if (value == "check" || value == "uncheck") {
		var check = true;
		if (value == "uncheck")
			check = false;

		$("input[name='checkbox_grid_" + id + "']").attr('checked', check);
	}

	// Get checkbox values into string, and process action
	else if (!value == "") {
		
		window.checkbox_grid = "checkbox_grid_" + id;
		var checkbox_list = "";

		$("input[name='checkbox_grid_" + id + "']").each(function() {
		    if ($(this).attr('checked')?1:0) {
			checkbox_list += "," + $(this).val();
		    }
		});
		checkbox_list = checkbox_list.substring(1);
		
		ajax(value + checkbox_list,"",result_element,actions);
	}

	//
	setTimeout("$('#action_grid_" + id + "').val('');",1000);
}

