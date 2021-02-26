<?php
             date_default_timezone_set('Asia/Kolkata');  
			 require 'db.php';
			 if(isset($_REQUEST["name"]) && isset($_REQUEST["password"]) && isset($_REQUEST["mobile"]) && isset($_REQUEST["hospitalname"])
				 && isset($_REQUEST["address"]) && isset($_REQUEST["specialist"]) && isset($_REQUEST["others"]) && isset($_REQUEST["area"]) 
&& isset($_REQUEST["subarea"]) ){    
			$result=mysqli_query($db,"select * from admin where mobile_no='".$_REQUEST["mobile"]."' " );
			    if( mysqli_num_rows($result) > 0  ){
				  $response['success'] = "201";
			      $response['message'] = "Mobile Number Already Exist";
				  die(print_r(json_encode($response),true));
				 }
                else{
				   $query='insert into  admin (name,password,hospital_name,address,mobile_no,speciality,others,area,subarea) 	
				  VALUES ("'.$_REQUEST["name"].'","'.$_REQUEST["password"].'","'.$_REQUEST["hospitalname"].'","'.$_REQUEST["address"].'","'.$_REQUEST["mobile"].'"
				  ,"'.$_REQUEST["specialist"].'","'.$_REQUEST["others"].'","'.$_REQUEST["area"].'","'.$_REQUEST["subarea"].'") '; //Add New User 
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