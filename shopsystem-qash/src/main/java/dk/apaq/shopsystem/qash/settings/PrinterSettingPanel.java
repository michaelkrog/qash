package dk.apaq.shopsystem.qash.settings;

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
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterEvent;
import dk.apaq.printing.core.PrinterListChangeListener;
import dk.apaq.printing.core.PrinterManager;
import dk.apaq.shopsystem.annex.AnnexType;
import dk.apaq.shopsystem.qash.PrintFacade;
import dk.apaq.shopsystem.qash.ShopSystemTheme;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.qash.common.SystemSettings;
import dk.apaq.vaadin.addon.printservice.PrintServiceListChangedEvent;
import dk.apaq.vaadin.addon.printservice.PrintServiceListChangedListener;
import java.util.List;

/**
 *
 * @author krog
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
    private PrinterUpdateHandler updateHandler = null;
    private PrintOnCompleteChangeHandler onCompleteChangeHandler = new PrintOnCompleteChangeHandler();
    private Printer selectedPrinter = null;
    private SystemSettings settings = null;
    private BeanContainer<String, Printer> printerContainer = new BeanContainer<String, Printer>(Printer.class);

    private class PrinterUpdateHandler implements PrinterListChangeListener {

        @Override
        public void onPrinterListChange(PrinterEvent event) {
            updateList();
        }

    }

    private class PrintOnCompleteChangeHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            settings.set(PrintFacade.PRINT_ON_COMPLETE_SETTING, 
                        Boolean.toString(chkPrintOnComplete.booleanValue()));
        }

    }

    private class PrintListSelectionHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            Object id = event.getProperty().getValue();
            if(id==null) {
                setSelectedPrintService(null);
            } else {
                BeanItem<Printer> item = printerContainer.getItem(id);
                Printer printer = item.getBean();
                setSelectedPrintService(printer);
            }
        }

    }

    private class PrintTypeSelectionHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            if(selectedPrinter!=null) {
                String key = PrintFacade.generatePrintTypeSettingKey(selectedPrinter);
                settings.set(key, event.getProperty().getValue().toString());
            }
        }

    }

    public PrinterSettingPanel() {

        setStyleName("v-printersettingpanel");
        settingsLayout.setStyleName("v-printersettingpanel-settings");
        settingsWrapper.setStyleName("v-printersettingpanel-settings-wrapper");
        lblPrinterName.setStyleName(ShopSystemTheme.LABEL_BOLD);
        
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

        printTypeSelect.addItem(AnnexType.Receipt);
        printTypeSelect.addItem(AnnexType.Invoice);
        printTypeSelect.setNullSelectionAllowed(false);
        printTypeSelect.setValue(AnnexType.Receipt);
        printTypeSelect.setImmediate(true);

        printerList.setNewItemsAllowed(false);
        printerList.setNullSelectionAllowed(false);
        printerList.setItemCaptionPropertyId("name");
        printerList.setImmediate(true);
        printerList.setContainerDataSource(printerContainer);

        printerContainer.setBeanIdProperty("name");

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
        PrinterManager pm = PrintFacade.getManager(getApplication());
        List<Printer> list = pm.getPrinters();
        printerContainer.removeAllItems();
        for(Printer printer : list) {
            printerContainer.addBean(printer);
        }

        if(printerContainer.size()>0) {
            setSelectedPrintService(list.get(0));
        }

        Printer defPrinter = pm.getDefaultPrinter();
        if(defPrinter==null) {
            lblDefaultPrinter.setValue("Default printer: None");
        } else {
            lblDefaultPrinter.setValue("Default printer: " + defPrinter.getName());
        }


        boolean printOnComplete = "true".equalsIgnoreCase(settings.get(PrintFacade.PRINT_ON_COMPLETE_SETTING));
        chkPrintOnComplete.setValue(printOnComplete);


    }

    private void setSelectedPrintService(Printer selectedPrinter) {
        this.selectedPrinter = selectedPrinter;
        printerList.setValue(selectedPrinter.getName());

        if(selectedPrinter==null) {
            lblPrinterName.setValue("");
            printTypeSelect.setEnabled(false);
        } else {
            lblPrinterName.setValue(selectedPrinter.getName());
            printTypeSelect.setEnabled(true);

            if(settings!=null) {
                String strPrintType = settings.get(PrintFacade.generatePrintTypeSettingKey(selectedPrinter));
                AnnexType printType;
                if(strPrintType==null) {
                    printType = AnnexType.Receipt;
                } else {
                    printType = AnnexType.valueOf(strPrintType);
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
        
        if(updateHandler==null) {
            updateHandler = new PrinterUpdateHandler();
            PrintFacade.getManager(getApplication()).addListener(updateHandler);
            updateList();
        }
    }
    
}
