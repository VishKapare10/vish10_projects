<?php

			 require 'db.php';
			 if(isset($_REQUEST["name"]) && isset($_REQUEST["password"]) ){    
				 $ck_query='select * from admin where name="'.$_REQUEST['name'].'" AND password="'.$_REQUEST['password'].'"';  
		         $result=mysqli_query($db,$ck_query);
	             if(mysqli_num_rows($result)>0){
				      
				   $data=mysqli_fetch_array($result);
				   $response['id'] = $data['id'];
				   $response['name']=$data['name'];
				   $response['mobile']=$data['mobile_no'];
				   $response['hospitalname']=$data['hospital_name'];
				   $response['success'] = "200";
			       $response['message'] = "Sucessfully Login";
				 }else{
				   $response['success'] = "201";
			       $response['message'] = "Username/Password Invalid";
			  }
			  die(print_r(json_encode($response),true));
			 }
			  else{
	               $response['success'] = "201";
			       $response['message'] = "Enter Username And Password";
				   die(print_r(json_encode($response),true));
				  
			  }
  ?>