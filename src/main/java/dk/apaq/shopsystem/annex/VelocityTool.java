package dk.apaq.shopsystem.annex;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author michaelzachariassenkrog
 */
public class VelocityTool {

    public String html(String input) {
        String string = StringEscapeUtils.escapeHtml(input);
        if(string!=null) {
            string = string.replace("\\n", "<br/>");
        }
        return string;
    }


}
