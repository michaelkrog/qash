package dk.apaq.shopsystem.ui.common;

import com.vaadin.data.Container;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ui.VFilterSelect;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Select;
import dk.apaq.filter.FilterGenerator;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
@SuppressWarnings("serial")
@ClientWidget(VFilterSelect.class)
public class SearchField extends Select {

    private String inputPrompt = null;
    private String filterstring = null;
    private FilterGenerator filterGenerator;
    private boolean ignoreNextItemSetChange;

    public SearchField() {
        setMultiSelect(false);
        setNewItemsAllowed(false);
    }

    public SearchField(String caption, Collection<?> options) {
        super(caption, options);
        setMultiSelect(false);
        setNewItemsAllowed(false);
    }

    public SearchField(String caption, Container dataSource) {
        super(caption, dataSource);
        setMultiSelect(false);
        setNewItemsAllowed(false);
    }

    public SearchField(String caption) {
        super(caption);
        setMultiSelect(false);
        setNewItemsAllowed(false);
    }

    @Override
    public void setMultiSelect(boolean multiSelect) {
        if (multiSelect && !isMultiSelect()) {
            throw new UnsupportedOperationException("Multiselect not supported");
        }
        super.setMultiSelect(multiSelect);
    }

    public void setFilterGenerator(FilterGenerator filterGenerator) {
        this.filterGenerator = filterGenerator;
    }

    /**
     * Gets the current input prompt.
     *
     * @see #setInputPrompt(String)
     * @return the current input prompt, or null if not enabled
     */
    public String getInputPrompt() {
        return inputPrompt;
    }

    /**
     * Sets the input prompt - a textual prompt that is displayed when the
     * select would otherwise be empty, to prompt the user for input.
     *
     * @param inputPrompt
     *            the desired input prompt, or null to disable
     */
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
        requestRepaint();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (inputPrompt != null) {
            target.addAttribute("prompt", inputPrompt);
        }
        super.paintContent(target);
    }

    @Override
    protected List<?> getFilteredOptions() {
        //To hack the select obix we need to do this first.
        List options = super.getFilteredOptions();

        if(getContainerDataSource() instanceof FilterableContainer) {
            ignoreNextItemSetChange = true;
            FilterableContainer filterable = (FilterableContainer)getContainerDataSource();
            if (filterGenerator!=null && filterstring!=null && !"".equals(filterstring)) {
                dk.apaq.filter.Filter filter = filterGenerator.generateFilter(filterstring);
                filterable.setFilter(filter);
            } else {
                filterable.setFilter(null);
            }

            options.clear();
            options.addAll(getContainerDataSource().getItemIds());
        }

        return options;
    }

    @Override
    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        if(!ignoreNextItemSetChange) {
            super.containerItemSetChange(event);
        }
        ignoreNextItemSetChange = true;
    }


    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        //snif the filter
        String newFilter;
        if ((newFilter = (String) variables.get("filter")) != null) {
            // this is a filter request
            filterstring = newFilter;
            if (filterstring != null && !"".equals(filterstring)) {
                filterstring = filterstring.toLowerCase();
            }
        }
        super.changeVariables(source, variables);
    }


}