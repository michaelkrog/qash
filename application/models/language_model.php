<?php
class Language extends CI_Model {

    //$this->load->model('service/Language');

    function Language() {
        // Call the Model constructor
        parent::__construct();
	
	$this->load->model('service/Language_service');
    }


    function get_language($id = 0) {
	$sql = "";

	if ($id > 0)
	    $sql = " WHERE id_language = " . db_int_fix($id);

	$query = $this->db->query("SELECT * FROM language");
	return $query;
    }


    function get_phrases($language, $type) {
	$query = $this->db->query("SELECT language_tag,language_phrase, (SELECT language FROM language WHERE id_language = language_phrase.id_website_language) AS language FROM language_phrase WHERE id_website_language=" . $language . " AND id_language_phrase_type=" . $type);
	return $query;
    }


    // Build language files for use in modules
    function build_files_modules() {
	// Load helpers
	$this->load->helper('file');

	// Get languages, and write files for every language
	$language = $this->get_language()->result_array();
	for($i=0; $i<=count($language)-1; $i++) {

	    // Build file content
	    $language_phrase = $this->get_phrases($language[$i]["id_language"],10)->result_array();
	    
	    // Add PHP tag to content
	    $file_content = "<?PHP\r\n\r\n";

	    // Add language phrases to content
	    for($ii=0; $ii<=count($language_phrase)-1; $ii++) {
		$file_content .= "\$lang[\"" . str_replace("lang_", "", $language_phrase[$ii]["language_tag"]) . "\"] = \"" . $language_phrase[$ii]["language_phrase"] . "\";\r\n";
	    }

	    // Write file to language folder
	    $path = "./system/application/language/" . strtolower($language[$i]["language"]) . "/module_lang.php";
	    echo("<br/>" . $path . ": ");

	    if (!write_file($path, $file_content,"c")) {
		 echo 'Unable to write the file';
	    }
	    else
	    {
		 echo 'File written!';
	    }


	}


    }

}
?>