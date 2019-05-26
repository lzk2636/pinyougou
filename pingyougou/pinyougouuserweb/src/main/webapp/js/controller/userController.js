 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	$scope.entity={};
$scope.resign=function () {
	if ($scope.entity.password!=$scope.password){
		alert('密码不一致')
		$scope.entity.password='';
		$scope.password=''
		return;
	}
	userService.add($scope.entity,$scope.smsCode).success(function (response) {
		console.log(response)
		if (response.success==true){
			//console.log('22222')
			alert(response.messages)
			$scope.entity=''
			$scope.password=''
			$scope.smsCode=''
		}else {
			alert(response.messages)
		}
	})
}
	$scope.sendCode=function () {
		userService.sendCode($scope.entity.phone).success(function (response) {
			if (response.success==true){
				alert(response.messages)
			} else {
				alert(response.messages)
				$scope.entity.phone=''
			}
		})
	}

});	
