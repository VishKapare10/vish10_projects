<?php  
			 require 'db.php';
			 if(isset($_REQUEST["name"]) ){
				
				$query=mysqli_query($db,"select * from user_request where  username='".$_REQUEST["name"]."' and admin_mobile='".$_REQUEST["mobile"]."' 
				order by id desc");
			    
				if( mysqli_num_rows($query) > 0  ){
				
				$result = array();
 
				while($row = mysqli_fetch_array($query)){
				array_push($result,
				array('name'=>$row[1],
				'mobile'=>$row[2],
				'date'=>$row[5],
				'time'=>$row[6],
				'id'=>$row[0],
				'symptoms'=>$row[7],
				'request_status'=>$row[4]
				));
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