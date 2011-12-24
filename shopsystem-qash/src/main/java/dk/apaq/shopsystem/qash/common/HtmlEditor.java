package dk.apaq.shopsystem.qash.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.entity.WebPage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krog
 */
public class HtmlEditor extends CustomComponent {

    private CustomLayout customLayout;
    private String html = "<html><body><div location=\"ci1\">Label indhold</div><div location=\"ci2\"><b>Fed tekst</b></div><div location=\"ci3\">Label indhold</div></body></html>";

    public HtmlEditor() {
        List<ComponentInformation> cilist = new ArrayList<ComponentInformation>();

        ComponentInformation ci = new ComponentInformation();
        ci.setModuleName("label");
        ci.setId("ci1");
        cilist.add(ci);

        ci = new ComponentInformation();
        ci.setModuleName("image");
        ci.setId("ci2");
        cilist.add(ci);

        ci = new ComponentInformation();
        ci.setModuleName("ProductList");
        ci.setId("ci3");
        ComponentParameter param = new ComponentParameter();
        param.setString("test");
        ci.getParameterMap().put("value", param);
        cilist.add(ci);


        try {
            customLayout = new CustomLayout(new ByteArrayInputStream(html.getBytes()));
            setCompositionRoot(customLayout);

            for (ComponentInformation currentci : cilist) {
                if ("Label".equals(currentci.getModuleName())) {
                    customLayout.addComponent(new TextField(), currentci.getId());
                } else if ("Html".equals(currentci.getModuleName())) {
                    customLayout.addComponent(new RichTextArea(), currentci.getId());
                } else {
                    Panel panel = new Panel(currentci.getModuleName());

                    for (Entry<String, ComponentParameter> e : currentci.getParameterMap().entrySet()) {
                        String name = e.getKey();
                        ComponentParameter cp = e.getValue();
                        TextField tf = new TextField(name);
                        tf.setValue(cp.getString());
                        tf.setData(name);
                        panel.addComponent(tf);
                    }
                    customLayout.addComponent(panel, currentci.getId());
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(HtmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
