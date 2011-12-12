package dk.apaq.shopsystem.annex;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author michaelzachariassenkrog
 */
public class VelocityTool {

    public String html(String input) {
        if(input!=null) {
            input = StringEscapeUtils.escapeHtml(input);
            input = input.replace("\\n", "<br/>");
        }
        return input;
    }


}
