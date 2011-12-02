<div style="position:relative;">
    <div id="actions">
        <ul>
            <li onclick="actions('${id}','check');">Alle</li>
            <li onclick="actions('${id}','uncheck');">Ingen</li>
            <br />
            <!-- IF actions 
            <li><a href=\"" + this.action[i][1] + "\">" + this.action[i][0] + "</a></li>
            -->
        </ul>
    </div>
</div>
            
<table class='grid'>
    <!-- if header-->
    <tr>
        <!-- for each -->
        <td class='header'>Some header</td>
        <!-- end for each -->
        
        <td class="header actions" style="text-align:right;width:1px;">
            <div><img onclick="show_actions();" src="/application/assets/graphics/admin/icon_action.png"></div>
        </td>
    </tr>
    
    <!-- Inner Grid -->
    <tbody class='tbody box4' id="${row_id}">
        
        <!-- No data -->
        <tr><td><spring:message code="noData" text="noData" /></td></tr>
    
    </tbody>
    
</table>
                