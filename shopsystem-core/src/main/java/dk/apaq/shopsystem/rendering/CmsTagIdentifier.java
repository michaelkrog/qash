package dk.apaq.shopsystem.rendering;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.WicketParseException;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.util.string.Strings;

/**
 * This is a markup inline filter. It identifies xml tags which have a special meaning for the Cms system.
 * There are one type of tags which have a special meaning for Cms.
 * <p>
 * <ul>
 * <li>All tags with Wicket namespace, e.g. &lt;cms:component&gt;</li>
 * </ul>
 * 
 * @author Juergen Donnerstag
 */
public final class CmsTagIdentifier extends AbstractMarkupFilter
{
	/** List of well known wicket tag names */
	private static List<String> wellKnownTagNames;

	/** The current markup needed to get the markups namespace */
	private final MarkupResourceStream markup;

	/**
	 * Construct.
	 * 
	 * @param markup
	 *            The markup as known by now
	 */
	public CmsTagIdentifier(final MarkupResourceStream markup)
	{
		this.markup = markup;
	}

	/**
	 * Get the next tag from the next MarkupFilter in the chain and search for Wicket specific tags.
	 * <p>
	 * Note: The xml parser - the next MarkupFilter in the chain - returns XmlTags which are a
	 * subclass of MarkupElement. The implementation of this filter will return either ComponentTags
	 * or ComponentWicketTags. Both are subclasses of MarkupElement as well and both maintain a
	 * reference to the XmlTag. But no XmlTag is returned.
	 * 
	 * @see org.apache.wicket.markup.parser.IMarkupFilter#nextElement()
	 * @return The next tag from markup to be processed. If null, no more tags are available
	 */
	@Override
	protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException
	{
		final String namespace = "cms";

		// Identify tags with Wicket namespace
		if (namespace.equalsIgnoreCase(tag.getNamespace()))
		{
			// It is <wicket:...>
			tag = new CmsTag(tag.getXmlTag());

                        // Make it a Cms component. Otherwise it would be RawMarkup
                        tag.setId("_cms_" + tag.getName());
                        tag.setAutoComponentTag(true);
                        tag.setModified(true);
                    
		}

		
		return tag;
	}

	
}
