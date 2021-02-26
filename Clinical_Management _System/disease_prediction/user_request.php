<?php  
			 require 'db.php';
			 if(isset($_REQUEST["username"]) && isset($_REQUEST["usermobile"]) && isset($_REQUEST["adminmobile"]) && isset($_REQUEST["date"])&& isset($_REQUEST["time"])){ 
				   $query='insert into  `user_request`(`username`, `user_mobile`, `admin_mobile`, `date`, `time`,symptoms) 
					VALUES ("'.$_REQUEST["username"].'","'.$_REQUEST["usermobile"].'","'.$_REQUEST["adminmobile"].'",
					"'.$_REQUEST["date"].'","'.$_REQUEST["time"].'","'.$_REQUEST["symptoms"].'") ';
				   $result=mysqli_query($db,$query);
				   
				   if($result==1){
					$response['message'] = "Successfully Inserted Recored";   
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