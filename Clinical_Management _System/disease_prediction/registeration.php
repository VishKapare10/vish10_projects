<?php  
			 require 'db.php';
			 if(isset($_REQUEST["name"]) && isset($_REQUEST["password"]) && isset($_REQUEST["mobile"]) && isset($_REQUEST["mail"])){ 
			
			$result=mysqli_query($db,"select * from user where name='".$_REQUEST["name"]."' or mobile_no='".$_REQUEST["mobile"]."' " );
			    if( mysqli_num_rows($result) > 0  ){
				  $response['success'] = "201";
			      $response['message'] = "Name/Mobile Number Already Exist";
				  die(print_r(json_encode($response),true));
				 }
                else{
				   $query='insert into  user (name,password,mobile_no,email_id) 	
				  VALUES ("'.$_REQUEST["name"].'","'.$_REQUEST["password"].'","'.$_REQUEST["mobile"].'","'.$_REQUEST["mail"].'") ';
				   $result=mysqli_query($db,$query);
				   
				   if($result==1){
					$response['message'] = "Successfully Inserted Recored";   
				     $response['success'] = "200";
				   }else{
					    $response['message'] = "Eroor Occured ";
				        $response['success'] = "201";
				   }
				 die(print_r(json_encode($response),true));
				}
			  } else{
	               $response['success'] = "201";
			       $response['message'] = "Please Enter All Details";
				   die(print_r(json_encode($response),true)); 
			  }
  ?>