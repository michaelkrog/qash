/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.ui;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.qash.app.QashTheme;
import dk.apaq.qash.app.ui.common.Spacer;
import dk.apaq.qash.app.util.PrintType;
import dk.apaq.qash.app.util.PrinterUtil;
import dk.apaq.qash.app.util.SystemSettings;
import dk.apaq.vaadin.addon.printservice.PrintServiceListChangedEvent;
import dk.apaq.vaadin.addon.printservice.PrintServiceListChangedListener;
import dk.apaq.vaadin.addon.printservice.RemotePrintServiceManager;
import java.util.List;
import javax.print.PrintService;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PrinterSettingPanel extends CustomComponent {

    private final Select printTypeSelect = new Select();
    private final CheckBox chkPrintOnComplete = new CheckBox("Auto print completed orders");
    private final VerticalLayout outerLayout = new VerticalLayout();
    private final HorizontalLayout innerLayout = new HorizontalLayout();
    private final HorizontalLayout bottomLayout = new HorizontalLayout();
    private final GridLayout settingsLayout = new GridLayout(2,2);
    private final VerticalLayout settingsWrapper = new VerticalLayout();
    private final Spacer settingsLayoutSpacer = new Spacer();
    private final Spacer bottomLayoutSpacer = new Spacer();
    private final ListSelect printerList = new ListSelect();
    private final Label lblDefaultPrinter = new Label("Default printer: None");
    private final Label lblPrinterName = new Label("Samsung ML-1630");
    private final Label lblPrintType = new Label("Print type");
    private final Resource printerIconResource = new ThemeResource("img/printer_48.png");
    private final Embedded printerIcon = new Embedded(null, printerIconResource);
    private RemotePrintServiceManager printServiceManager;
    private PrinterUpdateHandler updateHandler = new PrinterUpdateHandler();
    private PrintOnCompleteChangeHandler onCompleteChangeHandler = new PrintOnCompleteChangeHandler();
    private PrintService selectedPrintService = null;
    private SystemSettings settings = null;
    private BeanContainer<String, PrintService> printServiceContainer = new BeanContainer<String, PrintService>(PrintService.class);

    private class PrinterUpdateHandler implements PrintServiceListChangedListener {

        public void onPrintServiceListChanged(PrintServiceListChangedEvent event) {
            updateList();
        }

    }

    private class PrintOnCompleteChangeHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            settings.set(PrinterUtil.PRINT_ON_COMPLETE_SETTING, 
                        Boolean.toString(chkPrintOnComplete.booleanValue()));
        }

    }

    private class PrintListSelectionHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            Object id = event.getProperty().getValue();
            if(id==null) {
                setSelectedPrintService(null);
            } else {
                BeanItem<PrintService> item = printServiceContainer.getItem(id);
                PrintService ps = item.getBean();
                setSelectedPrintService(ps);
            }
        }

    }

    private class PrintTypeSelectionHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            if(selectedPrintService!=null) {
                String key = PrinterUtil.generatePrintTypeSettingKey(selectedPrintService);
                settings.set(key, event.getProperty().getValue().toString());
            }
        }

    }

    public PrinterSettingPanel() {

        setStyleName("v-printersettingpanel");
        settingsLayout.setStyleName("v-printersettingpanel-settings");
        settingsWrapper.setStyleName("v-printersettingpanel-settings-wrapper");
        lblPrinterName.setStyleName(QashTheme.LABEL_BOLD);
        
        innerLayout.addComponent(printerList);
        innerLayout.addComponent(settingsWrapper);
        innerLayout.setExpandRatio(settingsWrapper, 1.0F);

        settingsWrapper.addComponent(settingsLayout);
        settingsWrapper.addComponent(settingsLayoutSpacer);
        settingsWrapper.setExpandRatio(settingsLayoutSpacer, 1.0F);

        settingsLayout.addComponent(printerIcon, 0, 0);
        settingsLayout.addComponent(lblPrinterName, 1, 0);
        settingsLayout.addComponent(lblPrintType, 0, 1);
        settingsLayout.addComponent(printTypeSelect, 1, 1);
        settingsLayout.setComponentAlignment(printerIcon, Alignment.TOP_RIGHT);
        settingsLayout.setComponentAlignment(lblPrinterName, Alignment.TOP_LEFT);
        settingsLayout.setColumnExpandRatio(1, 1.0F);

        settingsLayout.setComponentAlignment(lblPrintType, Alignment.MIDDLE_RIGHT);

        outerLayout.addComponent(innerLayout);
        outerLayout.addComponent(bottomLayout);
        outerLayout.setExpandRatio(innerLayout, 1.0F);

        bottomLayout.addComponent(chkPrintOnComplete);
        bottomLayout.addComponent(bottomLayoutSpacer);
        bottomLayout.addComponent(lblDefaultPrinter);
        bottomLayout.setExpandRatio(bottomLayoutSpacer, 1.0F);

        outerLayout.setMargin(true);
        outerLayout.setSpacing(true);
        innerLayout.setSpacing(true);
        settingsLayout.setMargin(true);
        settingsLayout.setSpacing(true);

        printerIcon.setWidth(48, UNITS_PIXELS);
        printerIcon.setHeight(48, UNITS_PIXELS);
        lblDefaultPrinter.setSizeUndefined();
        lblDefaultPrinter.setStyleName(Reindeer.LABEL_SMALL);

        printTypeSelect.addItem(PrintType.Receipt);
        printTypeSelect.addItem(PrintType.Invoice);
        printTypeSelect.setNullSelectionAllowed(false);
        printTypeSelect.setValue(PrintType.Receipt);
        printTypeSelect.setImmediate(true);

        printerList.setNewItemsAllowed(false);
        printerList.setNullSelectionAllowed(false);
        printerList.setItemCaptionPropertyId("name");
        printerList.setImmediate(true);
        printerList.setContainerDataSource(printServiceContainer);

        printServiceContainer.setBeanIdProperty("name");

        chkPrintOnComplete.addListener(onCompleteChangeHandler);

        setCompositionRoot(outerLayout);

        lblPrintType.setSizeUndefined();
        printerList.setWidth(200, UNITS_PIXELS);
        printerList.setHeight(100, UNITS_PERCENTAGE);
        settingsWrapper.setSizeFull();
        settingsLayout.setWidth(100, UNITS_PERCENTAGE);
        innerLayout.setSizeFull();
        bottomLayout.setWidth(100, UNITS_PERCENTAGE);
        bottomLayout.setHeight(20, UNITS_PIXELS);
        outerLayout.setSizeFull();
        setSizeFull();;

        printerList.addListener(new PrintListSelectionHandler());
        printTypeSelect.addListener(new PrintTypeSelectionHandler());
        
    }

    private void updateList() {
        List<? extends PrintService> list = printServiceManager.getPrintServices(null, null);
        printServiceContainer.removeAllItems();
        for(PrintService service : list) {
            printServiceContainer.addBean(service);
        }

        if(printServiceContainer.size()>0) {
            setSelectedPrintService(list.get(0));
        }

        PrintService defPs = printServiceManager.getDefaultPrintService();
        if(defPs==null) {
            lblDefaultPrinter.setValue("Default printer: None");
        } else {
            lblDefaultPrinter.setValue("Default printer: " + defPs.getName());
        }


        boolean printOnComplete = "true".equalsIgnoreCase(settings.get(PrinterUtil.PRINT_ON_COMPLETE_SETTING));
        chkPrintOnComplete.setValue(printOnComplete);


    }

    private void setSelectedPrintService(PrintService selectedPrintService) {
        this.selectedPrintService = selectedPrintService;
        printerList.setValue(selectedPrintService.getName());

        if(selectedPrintService==null) {
            lblPrinterName.setValue("");
            printTypeSelect.setEnabled(false);
        } else {
            lblPrinterName.setValue(selectedPrintService.getName());
            printTypeSelect.setEnabled(true);

            if(settings!=null) {
                String strPrintType = settings.get(PrinterUtil.generatePrintTypeSettingKey(selectedPrintService));
                PrintType printType;
                if(strPrintType==null) {
                    printType = PrintType.Receipt;
                } else {
                    printType = PrintType.valueOf(strPrintType);
                }
                printTypeSelect.setValue(printType);
            }
        }
    }



    @Override
    public void attach() {
        if(settings == null) {
            settings = SystemSettings.getInstance(getApplication());
        }

        if(printServiceManager==null) {
            printServiceManager = RemotePrintServiceManager.getInstance(getApplication());
            //printServiceManager = RemotePrintServiceHelper.getPrintServiceManager(getApplication());
            printServiceManager.addListener(updateHandler);
            updateList();
        }
    }
    
}
