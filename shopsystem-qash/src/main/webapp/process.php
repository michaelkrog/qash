<?php
function valid_email($emailaddress)
{
	//new regex, didn't give me any errors...might be a bit more exact
  if (ereg('^[_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))$', $emailaddress) != false)
    return true;
  else 
    return false;
}

function sendEmail($email){
	return 	mail ($email['to'], $email['subject'], $email['message'], $email['headers']);
}
	// get posted data into local variables
	$fullname = trim(stripslashes($_POST['fullname']));
	$emailaddress = trim(stripslashes($_POST['emailaddress']));
	$message = trim(stripslashes($_POST['message']));
	
	//setup email
	$email['to'] = 'test@vuu.com.au';
	$email['subject'] = "Request a Quote";
	$email['headers'] = "From: {$fullname} <{$emailaddress}>\r\n";
	// prepare email body text
	$email['message'] = "Full Name: {$fullname}\n\n";
	$email['message'] .= "Email Address: {$emailaddress}\n\n\n\n";
	$email['message'] .= "Message: {$message}";
	
	//sendEmail($email);

	// validation
	if (valid_email($emailaddress) && $fullname != "" && $message != "") { 
	//if return is true...
		sendEmail($email);
		echo 0; //Success
	}else { //otherwise
		echo 1; //Error
	}

?>