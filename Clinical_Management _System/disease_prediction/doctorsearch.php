<?php  
			 require 'db.php';
			 if(isset($_REQUEST["subarea"]) && isset($_REQUEST["symptoms"]) ){
				
				$str  = $_REQUEST["symptoms"];
				$pieces = explode("%", $str);
				
				$query=mysqli_query($db,"select * from admin where subarea='".$_REQUEST["subarea"]."' and others like '".$_REQUEST["symptoms"]."' 
				order by CHAR_LENGTH(others) desc " );
			    
				if( mysqli_num_rows($query) > 0  ){
				
				$result = array();
 
				while($row = mysqli_fetch_array($query)){
				array_push($result,
				array('name'=>$row[1],
				'hospitalname'=>$row[3],
				'address'=>$row[4],
				'speciality'=>$row[5],
				'others'=>$row[6],
				'mobileno'=>$row[7],
				'area'=>$row[8],
				'subarea'=>$row[9],
				'rating'=>$row[10]
				));
				}
				$response['success'] = "200";
				$response['message'] = "Query1 Accepted Request";
				$response['result'] = $result;
				die(print_r(json_encode($response),true));
				 }
                else{	
						$query2=mysqli_query($db,"select * from admin where subarea='".$_REQUEST["subarea"]."' and others like '$pieces[1]%'
												order by CHAR_LENGTH(others) desc " );
						
						
						if( mysqli_num_rows($query2) > 0  ){
							$result = array();
 
								while($row = mysqli_fetch_array($query2)){
								array_push($result,
								array('name'=>$row[1],
								'hospitalname'=>$row[3],
								'address'=>$row[4],
								'speciality'=>$row[5],
								'others'=>$row[6],
								'mobileno'=>$row[7],
								'area'=>$row[8],
								'subarea'=>$row[9],
								'rating'=>$row[10]
								));
							}
				$response['success'] = "200";
				$response['message'] = "Query2 Accepted Request";
				$response['result'] = $result;					
				die(print_r(json_encode($response),true));
				 }else{
					 $query3=mysqli_query($db,"select * from admin where subarea='".$_REQUEST["subarea"]."'  " );
						
						if( mysqli_num_rows($query3) > 0  ){
							$result = array();
 
								while($row = mysqli_fetch_array($query3)){
								array_push($result,
								array('name'=>$row[1],
								'hospitalname'=>$row[3],
								'address'=>$row[4],
								'speciality'=>$row[5],
								'others'=>$row[6],
								'mobileno'=>$row[7],
								'area'=>$row[8],
								'subarea'=>$row[9],
								'rating'=>$row[10]
								));
							}
				$response['success'] = "200";
				$response['message'] = "Query3 Accepted Request";
				$response['result'] = $result;					
				die(print_r(json_encode($response),true));
				 }else{
					 $response['success'] = "201";
					$response['message'] = "No Result Found";
					die(print_r(json_encode($response),true));
				 }
			 } }}else{
	               $response['success'] = "201";
			       $response['message'] = "Please Enter All Details";
				   die(print_r(json_encode($response),true)); 
			  }
  ?>