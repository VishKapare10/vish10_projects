<?php  
			 require 'db.php';
			 if(isset($_REQUEST["mobile"]) ){
				
				$query=mysqli_query($db,"select * from user_request where  user_mobile='".$_REQUEST["mobile"]."' and 
				request_status like 'Accepted' and rate like 'not rated' group by admin_mobile");
			    
				if( mysqli_num_rows($query) > 0  ){
				
				$result = array();
				while($row = mysqli_fetch_array($query)){
					$mobile_no=$row[3];
					
					$query2=mysqli_query($db,"select * from admin where  mobile_no='$mobile_no'");
					if( mysqli_num_rows($query2) > 0  ){
					while($row1 = mysqli_fetch_array($query2)){
						array_push($result,
						array('name'=>$row1[1],
						'mobile'=>$row1[7],
						'subarea'=>$row1[8],
						'area'=>$row1[9],
						'rating'=>$row1[10],
						'hospital_name'=>$row1[3]
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
  ?>