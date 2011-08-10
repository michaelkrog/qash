<?PHP
# Check shoppinnet permission
function check_permission_shoppinnet() {
	if($_COOKIE["shoppinnet"] == "kjdjcje723ui39217u12987r9xn93yrn3974y68tb891xmou3rte21tbnulkjhn")
		return true;
	else
		return false;
}


# Check user permission
function check_permission($allowed) { //commaseparated input
	global $website_md5;
	
	$blocked = true;
	
	if (strrpos($allowed, "user", 0) != false && $_COOKIE["user_md5"] != "") {
		$blocked = false; //User action accepted and user is logged in
	}
	
	if ($_COOKIE["admin_md5"] == $website_md5) {
		$blocked = false; //Admin is logged in
	}
	
	if ($blocked == true) {
		echo("Permission denied! Please log in...");
		die();
	}
}


function yesno($data) {
    $value = "Ja";
    if ($data == 0)
	$value = "Nej";

    return $value;
}


# Secure string in sql
function db_str_fix($data) {    
	$data = mysql_real_escape_string($data);
	return $data;
}


# Secure integers in sql
function db_int_fix($data) {
	if (!is_null($data)) {

		if ($data == "on" || $data == "true") {
			$data = 1;
		}
		
		$data = str_replace(",", ".", $data);
	}
	
	if (!is_numeric($data))
		$data = 0;

	return $data;
}


# Format date for correct vissibility
function date_mysql_out($mysqldate) {
	$phpdate = strtotime( $mysqldate );
	return date("d-m-Y H:i:s", $phpdate);
}


# Format date for correct vissibility
function date_international_out($date) {
	return date("Y-m-d", $date);
}


# Load sql into multidimensional array
function sql_to_array($sql){
	$result = mysql_query($sql);
	$data = array();
	
	
	if ($result != "") {
		$i = 0;

		while($data[$i] = mysql_fetch_assoc($result))
			$i++;
		unset($data[$i]);
		
		mysql_free_result($result);
	}
	
	return $data;
}


# Create uniqe md5
function md5_create() {
	return md5(time() . rand(1, 1000000));
}


# Get user id from md5
function md5_to_id($table,$md5value) {
	$result = mysql_query("SELECT id FROM " . $table . " WHERE md5 = '" . db_str_fix($md5value) . "' ");
	return db_int_fix(mysql_result ($result, 0, "id"));
}


# Delete file
function file_delete($file_type_id,$joined_id) {
	mysql_query("UPDATE file SET deleted = 1 WHERE file_type_id = " . db_int_fix($file_type_id) . " AND joined_id = " . db_int_fix($joined_id) );
}


# Upload file
function file_upload($file_array,$file_type_id,$joined_id) {
	foreach ($_FILES[$file_array]["error"] as $key => $error) {
		 if ($error == UPLOAD_ERR_OK) {
		 	
			// File uploaded correct to temp. Now move and rename file
			 $tmp_name = $_FILES[$file_array]["tmp_name"][$key];
			 $name = $_FILES[$file_array]["name"][$key];
			 $file_size = $_FILES[$file_array]["size"][$key];
			 $file_extension_array = explode(".",$name);
			 $file_extension = $file_extension_array[count($file_extension_array)-1];
			 $file_md5 = md5_create();
			 $name = $file_md5 . "." . $file_extension;
			 move_uploaded_file($tmp_name, "upload/$name");
			 
			 // Save file info to db
			 mysql_query("INSERT INTO file (md5,file_type_id,joined_id,width,height,thumbnail,file_name,file_extension,file_size) VALUES ('" . $file_md5 . "','" . $file_type_id . "','" . $joined_id . "',0,0,0,'" . $file_name . "', '" . $file_extension . "', '" . $file_size . "') ") or die(mysql_error());
		 }
	}
}


# Convert text to keyword string for urls
function link_keyword_fix($data) {
	$data = str_replace(" ","-",$data);
	$data = substr($data,0,50);
	$data = urlencode($data);

	return $data;
}


# Convert language code to language id, and write cookie
function language_set($website_language_code, $website_default_language) {
		$result =  mysql_query("SELECT id_language FROM language WHERE language.active=1 AND language_code='" . db_str_fix($website_language_code) . "' ");
		$num = mysql_numrows($result);
		
		$language_id = $website_default_language;
		if ($num > 0)
			$language_id = mysql_result($result, 0, "id_language");
		
		// Write cookie
		setcookie("idl", $language_id, time()+31536000); // 1 year
		
		return $language_id;
}


# Send email
function email_send($email, $subject, $message, $website_id, $sender_name) {
	global $website_encoding;
	
	$headers = "X-Priority: 3\r\n";
	$headers .= "Content-Transfer-Encoding: " . $website_encoding . "\r\n";
	$headers .= "MIME-Version: 1.0\r\n";
	$headers .= "X-Mailer: ShoppinNet.com\r\n";
    $headers .= "Content-type: text/html; charset=" . $website_encoding . "\r\n";
	$headers .= "From: '" . $sender_name . "' <notification-" . $website_id . "@shoppinnet.com>\r\n"; 
	$headers .= "Cc: " . $email . "\r\n";
	$subject = "=?" . $website_encoding . "?B?" . base64_encode($subject) . "?="; 


    $status = mail("notification-000@shoppinnet.com", $subject, $message, $headers);
	
	if ($status == false) {
		
	}
	
	return $status;
}


# Download website
function get_web_page($url) {
	$options = array(
		CURLOPT_RETURNTRANSFER => true,     // return web page
		CURLOPT_HEADER         => false,    // don't return headers
		CURLOPT_FOLLOWLOCATION => false,     // follow redirects
		CURLOPT_ENCODING       => "",       // handle all encodings
		CURLOPT_USERAGENT      => "ShoppinNet.com", // who am i
		CURLOPT_AUTOREFERER    => false,     // set referer on redirect
		CURLOPT_CONNECTTIMEOUT => 120,      // timeout on connect
		CURLOPT_TIMEOUT        => 120,      // timeout on response
		CURLOPT_MAXREDIRS      => 10,       // stop after 10 redirects
	);
	
	$ch = curl_init( $url );
	curl_setopt_array( $ch, $options );
	$content = curl_exec( $ch );
	$err     = curl_errno( $ch );
	$errmsg  = curl_error( $ch );
	$header  = curl_getinfo( $ch );
	curl_close( $ch );
	
	$header['errno']   = $err;
	$header['errmsg']  = $errmsg;
	$header['content'] = $content;
	
	return $header;
}

?>