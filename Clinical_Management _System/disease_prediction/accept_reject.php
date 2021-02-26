<?php  
			 require 'db.php';
			 if(isset($_REQUEST["id"]) && isset($_REQUEST["request"]) ){ 
				   $query='UPDATE `user_request` SET `request_status`="'.$_REQUEST["request"].'" WHERE `id`="'.$_REQUEST["id"].'" ';
				   $result=mysqli_query($db,$query);
				   if($result==1){
					$response['message'] = "Successfully Sent Notification";   
				     $response['success'] = "200";
				   }else{
					    $response['message'] = "Eroor Occured ";
				        $response['success'] = "201";
				   }
				 die(print_r(json_encode($response),true));
				
			  } else{
	               $response['success'] = "201";
			       $response['message'] = "Please Enter All Details";
				   die(print_r(json_encode($response),true)); 
			  }
  ?>