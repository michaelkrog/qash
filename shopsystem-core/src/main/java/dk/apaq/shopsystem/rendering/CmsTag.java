package dk.apaq.shopsystem.rendering;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.XmlTag;

/**
 * CmsTag extends ComponentTag and will be created by a MarkupParser whenever it parses a tag in
 * the cms namespace. By default, this namespace is "cms", so cms tags are then of the form
 * &lt;cms:*&gt;
 * <p>
 * Note 1: you need to add an XHTML doctype to your markup and use &lt;html xmlns:cms&gt; to
 * create a XHTML conform namespace for such tags.
 * <p>
 * Note 2: The namespace name is configurable. E.g. &lt;html xmlns:ccn="http://cms"&gt;
 * 
 * @author Michael Krog
 */
public class CmsTag extends ComponentTag
{
	/**
	 * Constructor
	 * 
	 * @param tag
	 *            The XML tag which this wicket tag is based upon.
	 */
	public CmsTag(final XmlTag tag)
	{
		super(tag);
	}


	/**
	 * @return True, if tag name equals 'wicket:container'
	 */
	public final boolean isComponentTag()
	{
		return "component".equalsIgnoreCase(getName());
	}

	/**
	 * @return True, if tag name equals 'wicket:link'
	 */
	public final boolean isParameterTag()
	{
		return "parameter".equalsIgnoreCase(getName());
	}

	

	/**
	 * Gets this tag if it is already mutable, or a mutable copy of this tag if it is immutable.
	 * 
	 * @return This tag if it is already mutable, or a mutable copy of this tag if it is immutable.
	 */
	@Override
	public ComponentTag mutable()
	{
		if (xmlTag.isMutable())
		{
			return this;
		}
		else
		{
			final WicketTag tag = new WicketTag(xmlTag.mutable());
			tag.setId(getId());
			tag.setAutoComponentTag(isAutoComponentTag());
			return tag;
		}
	}
}

