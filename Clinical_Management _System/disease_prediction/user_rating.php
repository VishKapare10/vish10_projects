<?php

			 require 'db.php';
			 if(isset($_REQUEST["rating"]) && isset($_REQUEST["user_mobile"]) && ($_REQUEST["admin_mobile"]) ){   
			
			$res=mysqli_query($db,"UPDATE `user_request` SET `rate`='rated' WHERE `user_mobile`='".$_REQUEST["user_mobile"]."' 
									and `admin_mobile`='".$_REQUEST["admin_mobile"]."' and request_status like 'Accepted' "  );
			    if($res==1){
				   $result=mysqli_query($db,"UPDATE `admin` SET `rating`='".$_REQUEST["rating"]."' WHERE `mobile_no`='".$_REQUEST["admin_mobile"]."'");
				   if($result==1){
					$response['message'] = "Successful";   
				     $response['success'] = "200";
				   }else{
					    $response['message'] = "Eroor Occured ";
				        $response['success'] = "201";
				   }
				   die(print_r(json_encode($response),true));
				 }
                else{
					$response['success'] = "201";
			      $response['message'] = "Fail while updating user table";
				  die(print_r(json_encode($response),true));
				}
				 
			  } else{
	               $response['success'] = "201";
			       $response['message'] = "Please Enter All Details";
				   die(print_r(json_encode($response),true));
				  
			  }
  ?>