<?php
/**
 PHP API for Login, Register, Changepassword, Resetpassword Requests and for Email Notifications.
 **/
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // Get tag
    $tag = $_POST['tag'];

    // Include Database handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
            $response["user"]["fname"] = $user["firstname"];
            $response["user"]["lname"] = $user["lastname"];
            $response["user"]["email"] = $user["email"];
	    $response["user"]["uname"] = $user["username"];
            $response["user"]["uid"] = $user["unique_id"];
            $response["user"]["created_at"] = $user["created_at"];
            $points = $db->getPointsByUid($user["unique_id"]);
            $ratingDetails =  $db->getRatingByUid($user["unique_id"]);
	    $response["user"]["points"] = $points;
       $response["user"]["rating"] = $ratingDetails;  
            echo json_encode($response);
	    
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    }
else if($tag=='updatepoints'){
$uid = $_POST['uid'];
$newpoints = $_POST['newpoints'];
$businessname = $_POST['businessname'];
if ($db->getUserByUniqueId($uid)) {

$result = $db->updatepoints($uid, $businessname, $newpoints);
if ($result) {
            $response["success"] = 1;
            $response["result"] =$result;
            echo json_encode($response);
}
else {
$response["error"] = 1;
$response["error_msg"] = "Error retrieving user details";
echo json_encode($response);
}          
        } 
           else {

            $response["error"] = 2;
            $response["error_msg"] = "User does not exist";
             echo json_encode($response);

}
}else if($tag=='updaterating'){
$uid = $_POST['uid'];
$rating = $_POST['rating'];
$feedback = $_POST['feedback'];
if ($db->getUserByUniqueId($uid)) {

$result = $db->updaterating($uid, $rating, $feedback);
if ($result) {
            $response["success"] = 1;
            echo json_encode($response);
}
else {
$response["error"] = 1;
$response["error_msg"] = "Error updating rating details";
echo json_encode($response);
}          
        } 
           else {

            $response["error"] = 2;
            $response["error_msg"] = "User does not exist";
             echo json_encode($response);

}
}else if($tag=="sync_db"){
	$uid = $_POST['uid'];
	$points = $db->getPointsByUid($uid);
        $response["success"] = 1;
	$response["points"] = $points;
        echo json_encode($response);

}
  else if ($tag == 'chgpass'){
  $email = $_POST['email'];

  $newpassword = $_POST['newpas'];
  

  $hash = $db->hashSSHA($newpassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
  $subject = "Change Password Notification";
         $message = "Hello Piggy Rewards Customer,\n\nYour Password has been sucessfully changed.\n\nRegards,\Piggy Rewards Team.";
          $from = "svetvaz@gmail.com";
          $headers = "From: svetvaz@gmail.com";
	if ($db->isUserExisted($email)) {

 $user = $db->forgotPassword($email, $encrypted_password, $salt);
if ($user) {
         $response["success"] = 1;
          mail($email,$subject,$message,$headers);
         echo json_encode($response);
}
else {
$response["error"] = 1;
echo json_encode($response);
}


            // user is already existed - error response
           
           
        } 
           else {

            $response["error"] = 2;
            $response["error_msg"] = "User does not exist";
             echo json_encode($response);

}
}
else if ($tag == 'forpass'){
$forgotpassword = $_POST['forgotpassword'];

$randomcode = $db->random_string();
  

$hash = $db->hashSSHA($randomcode);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
  $subject = "Password Recovery";
         $message = "Hello Piggy Rewards Customer,\n\nYour Password has been sucessfully changed. Your new Password is $randomcode . You are advised to login with your new Password and change it in the Main Menu.\n\nRegards,\Piggy Rewards Team.";
          $from = "svetvaz@gmail.com";
          $headers = "From:" . $from;
	if ($db->isUserExisted($forgotpassword)) {

 $user = $db->forgotPassword($forgotpassword, $encrypted_password, $salt);
if ($user) {
         $response["success"] = 1;
          mail($forgotpassword,$subject,$message,$headers);
         echo json_encode($response);
}
else {
$response["error"] = 1;
echo json_encode($response);
}


            // user is already existed - error response
           
           
        } 
           else {

            $response["error"] = 2;
            $response["error_msg"] = "User does not exist";
             echo json_encode($response);

}

}
else if ($tag == 'register') {
        // Request type is Register new user
        $fname = $_POST['fname'];
		$lname = $_POST['lname'];
        $email = $_POST['email'];
		$uname = $_POST['uname'];
        $password = $_POST['password'];


        
          $subject = "Registration";
         $message = "Hello $fname,\n\nYou have sucessfully registered to the Piggy Rewards Loyalty program. You may begin using all of our services. \n\nRegards,\nAdmin, Loyalty Rewards Team.";
          $from = "svetvaz@gmail.com";
          $headers = "From:" . $from;

        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already exists.Click on 'Forgot Password to retrieve your password'";
            echo json_encode($response);
        } 
           else if(!$db->validEmail($email)){
            $response["error"] = 3;
            $response["error_msg"] = "Invalid Email Id";
            echo json_encode($response);             
}
else {
            // store user
            $user = $db->storeUser($fname, $lname, $email, $uname, $password);
            if ($user) {
                // user stored successfully
            $response["success"] = 1;
            $response["user"]["fname"] = $user["firstname"];
            $response["user"]["lname"] = $user["lastname"];
            $response["user"]["email"] = $user["email"];
	          $response["user"]["uname"] = $user["username"];
            $response["user"]["uid"] = $user["unique_id"];
            $response["user"]["created_at"] = $user["created_at"];
  //             mail($email,$subject,$message,$headers);
            
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registration";
                echo json_encode($response);
            }
        }
    } else {
         $response["error"] = 3;
         $response["error_msg"] = "JSON ERROR";
        echo json_encode($response);
    }
} else {
    echo "Loyalty Rewards API";
}
?>

