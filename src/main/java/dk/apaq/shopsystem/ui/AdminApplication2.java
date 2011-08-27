package dk.apaq.shopsystem.ui;

import java.util.Iterator;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class AdminApplication2 extends Application {

    VerticalLayout main = new VerticalLayout();

    @Override
    public void init() {
        setTheme("reindeermods");
        Window mainWindow = new Window("Reindeermods Application", main);
        setMainWindow(mainWindow);
        main.setSizeFull();

        CssLayout toolbar = new CssLayout();
        toolbar.setWidth("100%");
        toolbar.addStyleName("toolbar-invert");

        CssLayout right = new CssLayout();
        right.setSizeUndefined();
        right.addStyleName("right");
        toolbar.addComponent(right);

        Label text = new Label("Informational text");
        right.addComponent(text);

        Button b = new Button("Action");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        b.addStyleName("borderless");
        right.addComponent(b);

        b = new Button("Action 2");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        b.addStyleName("borderless");
        right.addComponent(b);

        CssLayout left = new CssLayout();
        left.setSizeUndefined();
        left.addStyleName("left");
        toolbar.addComponent(left);

        Label title = new Label("Application Name");
        title.addStyleName("h1");
        left.addComponent(title);

        b = new NativeButton("Section");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        left.addComponent(b);
        left.addComponent(new NativeButton("Section 2"));

        main.addComponent(toolbar);

        final SplitPanel split = new SplitPanel(
                SplitPanel.ORIENTATION_HORIZONTAL);
        split.addStyleName("small blue white");
        split.setSplitPosition(20);
        main.addComponent(split);
        main.setExpandRatio(split, 1);

        CssLayout menu = new CssLayout();
        menu.addStyleName("menu");
        menu.setWidth("100%");
        split.setFirstComponent(menu);

        final Button.ClickListener change = new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ComponentContainer p = (ComponentContainer) event.getButton()
                        .getParent();
                for (Iterator iterator = p.getComponentIterator(); iterator
                        .hasNext();) {
                    ((AbstractComponent) iterator.next())
                            .removeStyleName("selected");
                }
                event.getButton().addStyleName("selected");
                split.setSecondComponent((Component) event.getButton()
                        .getData());
            }
        };

        Label l = new Label("Reindeer Mods");
        l.addStyleName("section");
        menu.addComponent(l);

        b = new NativeButton("Instructions", change);
        b.addStyleName("selected");
        menu.addComponent(b);

        VerticalLayout instructions = new VerticalLayout();
        instructions.setMargin(true);
        instructions.addComponent(new Label("Instructions"));
        b.setData(buildComponents());
        split.setSecondComponent(buildComponents());

        b = new NativeButton("Sample screen", change);
        menu.addComponent(b);

        VerticalLayout sample = new VerticalLayout();
        sample.addComponent(new Label("sample"));
        b.setData(sample);

        l = new Label(
                "Instructions contain info about the available styles this theme extension provides. Sample screen show an example layout of a quite regular application layout.");
        menu.addComponent(l);

        Window w = new Window("Normal Window");
        w.setHeight("400px");
        w.setWidth("500px");
        w.setPositionY(50);
        w.setPositionX(200);
        mainWindow.addWindow(w);
        w.setContent(buildComponents());

        w = new Window("Black Window");
        w.addStyleName("black");
        w.setHeight("300px");
        w.setWidth("400px");
        w.setPositionY(80);
        w.setPositionX(40);
        // mainWindow.addWindow(w);
        // w.setContent(buildComponents());

        w = new Window("Light Window");
        w.addStyleName("light");
        w.setHeight("200px");
        w.setWidth("300px");
        w.setPositionY(110);
        w.setPositionX(60);
        // mainWindow.addWindow(w);
        // w.setContent(buildComponents());

        w = new Window("Light Window without caption");
        w.addStyleName("light no-caption");
        w.setHeight("200px");
        w.setWidth("300px");
        w.setPositionY(110);
        w.setPositionX(60);
        // mainWindow.addWindow(w);
        // w.setContent(buildComponents());
    }

    ComponentContainer buildComponents() {
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");

        GridLayout l = new GridLayout(4, 1);
        l.setCaption("Basic components");
        l.setSpacing(true);
        l.setMargin(true);
        ts.addComponent(l);

        Button b = new Button("Default");
        b.addStyleName("primary");
        l.addComponent(b);

        b = new Button("Normal");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        l.addComponent(b);

        b = new Button("Normal, with-icon");
        b.addStyleName("with-icon");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        l.addComponent(b);

        b = new Button("Small");
        b.addStyleName("small");
        l.addComponent(b);

        b = new Button("Default, with-icon");
        b.addStyleName("primary with-icon");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        l.addComponent(b);

        b = new Button("Link");
        b.addStyleName("link");
        l.addComponent(b);

        b = new Button("Icon only");
        b.addStyleName("icon-only");
        b.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        l.addComponent(b);

        l = new GridLayout(4, 1);
        l.setCaption("Compound styles");
        l.addStyleName("white");
        l.setSizeFull();
        l.setSpacing(true);
        l.setMargin(true);
        ts.addComponent(l);

        return ts;
    }
}
