<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');


if ( ! function_exists('link_main'))
{
	function link_main($posturl)
	{
	    return "javascript:ajax('$posturl','','section_mid','');";
	}

}


if ( ! function_exists('link_action_dialog'))
{
	function link_action_dialog($posturl)
	{
	    return "javascript:actions('','$posturl','dialog','');";
	}
}


if ( ! function_exists('link_action_delete'))
{
	function link_action_delete($posturl)
	{
	    return "javascript:actions('','$posturl','dialog','confirm,hide_elements');";
	}
}

/**
 * link_popup
 *
 * Returns javascript link for loading content into popup
 *
 * @access	public
 * @param	string
 * @param	array
 * @return	string
 */
if ( ! function_exists('link_dialog'))
{
	function link_dialog($posturl)
	{
	    return "javascript:show_dialog(true,'box_popup','');ajax('" . $posturl . "','','box_popup','');";
	}
}


/**
 * link_popup_close
 *
 * Returns javascript link for closing popup
 *
 * @access	public
 * @return	string
 */
if ( ! function_exists('link_dialog_close'))
{
	function link_dialog_close()
	{
	    return "javascript:show_messagebox(false,'box_popup','');";
	}
}



