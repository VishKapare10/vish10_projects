<?php  
			 require 'db.php';
			 if(isset($_REQUEST["mobile"]) && isset($_REQUEST["request"]) ){
				
				$query=mysqli_query($db,"select * from user_request where request_status='".$_REQUEST["request"]."' and admin_mobile='".$_REQUEST["mobile"]."'");
			    
				if( mysqli_num_rows($query) > 0  ){
				$result = array();
				while($row = mysqli_fetch_array($query)){
				$mobile_no=$row[2];
				$query2=mysqli_query($db,"select email_id from user where  mobile_no='$mobile_no'");
					if( mysqli_num_rows($query2) > 0  ){
				while($row1 = mysqli_fetch_array($query2)){
				array_push($result,
				array('name'=>$row[1],
				'mobile'=>$row[2],
				'date'=>$row[5],
				'time'=>$row[6],
				'id'=>$row[0],
				'symptoms'=>$row[7],
				'mail'=>$row1[0]
				));
				}
					}				
				}				
				
				$response['success'] = "200";
				$response['message'] = "Result Found";
				$response['result'] = $result;
				die(print_r(json_encode($response),true));
				 }
                else{	
					$response['success'] = "201";
					$response['message'] = "No Result Found";
					die(print_r(json_encode($response),true));
				 }
			 } else{
	               $response['success'] = "201";
			       $response['message'] = "Please Enter All Details";
				   die(print_r(json_encode($response),true)); 
			  }
  ?>