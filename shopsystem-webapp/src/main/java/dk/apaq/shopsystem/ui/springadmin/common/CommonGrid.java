package dk.apaq.shopsystem.ui.springadmin.common;

import java.util.ArrayList;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 *
 * @author Martin Christensen
 */
public class CommonGrid {
       
    @Autowired
    private MessageSource messageSource;

    private String object = "";
    private String id = ""; // Grid identification, for allowing multiple grids
    private ArrayList header = new ArrayList();
    private ArrayList field = new ArrayList();
    private ArrayList action = new ArrayList();
    //private String link = null;
    private String data = null;


    public void setObject(String value) {
	this.object =  value;
    }

    public void  setId(String value) {
	this.id = value;
    }

    public void  setHeader(String value) {
	this.header.add(value);
    }

    public void  setField(String value) {
	this.field.add(value);
    }

    public void  setAction(String value) {
	this.action.add(value);
    }

    //public void  set_link(String value) {
    //	this.link[] =  value;
    //}

    public void  set_data(String data) {
	this.data =  data;
    }

    private String yesno(Boolean value) {
	
        String newValue = "No";
        if (value == true) {
	    newValue = "yes";
	}
	
	return newValue;  
    }

/*
    public String build()
    {

        StringBuilder output = new StringBuilder();
        
	// Create actions box
	output.append("<div style='position:relative;'><div id='actions'><ul><li onclick=\"actions('" + this.id + "','check');\">Alle</li><li onclick=\"actions('" + this.id + "','uncheck');\">Ingen</li><br />\n");
	for (Integer i=0;i < this.action.size(); i++) {
	    //output.append("<li><a href=\"" + this.action[i][1] + "\">" + this.action[i][0] + "</a></li>\n");
	}
	output.append("</ul></div></div>");


	// Create outer grid
	output.append("<table class='grid'>\n");


	// Create header
	if(!this.header.isEmpty()) {

		output.append("<tr>\n");

		for (Integer i=0;i < this.header.size(); i++) {
		    output.append("<td class='header'>" + this.header.get(i) + "</td>\n");
		}

		output.append("<td class='header actions' style='text-align:right;width:1px;'>\n");

		// Insert action image
		if (!this.action.isEmpty()) {
		    output.append("<div><img onclick='show_actions();' src='/application/assets/graphics/admin/icon_action.png'></div>");
		}
		output.append("</td></tr>\n");
	}


	// Create inner grid
	output.append("<tbody class='tbody box4' id='" + this.id + "'>\n");


	// Insert rows
	Integer highlightCnt = 1;
	for(Iterable object : this.data) {
	    
		e = this.domain_model.read(String object->id);
	    
		Integer cnt_cell = 0;
		Integer row_id = 0;
		//output_links_row =  output_links;

		String highlightCss = "";
		if (highlightCnt == 2) {
			highlightCss = " highlight";
			highlightCnt = 0;
		}

		row_id =  e->get_id();
		output.append("<tr class=' row" + class_highlight + "'>");


		foreach(this.field as field) {

		    getter = "get_".field[0];
		    value =  e->getter();

		    // Customize field output using one of the functions above
		    if (count(String field) == 2) {
			value = this.field[1](String value);
		    }
		    
		    output.append("<td class='object_" + this.object + "_" + row_id + "_" + field[0] + "'>" + value. "</td>");

		    cnt_cell++;
		}

		output = mb_ereg_replace("{id}",e->get_id(),output);

		cnt_cell++;


		// Insert sort function
		sort = "";
		//if (in_array("sort",functions))
		//    sort = "<div style='float:left;margin-left:30px;'><a href=\"javascript:ajax('" + site_url("admin/process/change_sort/module_offer_option_join/decrease/" + row_id + "") + "','','','ActionReload');\"><img src='/assets/graphics/admin/arrow_up.png'></a><br /><br /><a href=\"javascript:ajax('" + site_url("admin/process/change_sort/module_offer_option_join/increase/" + row_id + "") + "','','','ActionReload');\"><img src='/assets/graphics/admin/arrow_down.png'></a></div>");

		// Insert checkbox
		checkbox = "";
		if (this.action)
		    checkbox = "<input type='checkbox' name='checkbox_grid_" + this.id + "' value='" + row_id + "'>\n");

		output.append("<td class='last_col' style='text-align:right;'>sortcheckbox</td>\n");


		// End row
		output.append("</tr>\n");
		highlightCnt;++;

	}

	// If no data exists
	if (this.data.isEmpty()) {
	    output.append("<tr><td colspan='" + i + "'>" + messageSource.getMessage("", null, Locale.forLanguageTag("da")) + "</td></tr>\n");
	}

	// Finish grid creation
	output.append("</tbody>\n</table>\n");

	
	return output.toString();
	
    }


}
*/
    
    
}
