app.controller('itemsController',function ($scope,$http) {
	$scope.num=1;
	$scope.itemArrtitem={};
	$scope.mune={};
	$scope.addNum=function(x){
		$scope.num+=x;
		if($scope.num<1){
			$scope.num=1;
		}
	}
	$scope.checkedCode=function(key,value){
		$scope.itemArrtitem[key]=value
		getItem();
	}
	$scope.isCheck=function(key,value){
		if($scope.itemArrtitem[key]==value){
		return true;
		}else{
		return false;
		}
	}
	
	$scope.initData=function(){
			$scope.mune=plist[0];
			console.log($scope.mune)
			$scope.itemArrtitem=JSON.parse(JSON.stringify($scope.mune.spec))
	}	
//对象	
	itemObject=function (map1,map2){
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}
		}
		
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}
		}
		return true;
	
	}
	getItem=function (){
	for(var i=0;i<plist.length;i++){
		if(itemObject(plist[i].spec,$scope.itemArrtitem)){
			$scope.mune=plist[i];
			return;
		}
		$scope.mune={id:1,title:'..........',price:'?????',spec:{}}
	}
	}
	$scope.shoppingId=function(){
	//alert($scope.mune.id)
		$http.get("http://localhost:9107/cart/cartList.do?itemId="+$scope.mune.id+"&num="+$scope.num,{'withCredentials':true}).success(function (response) {
			if (response.success){
				location.href="http://localhost:9107/cart.html"
			} else{
				alert(response.messages)
			}
		})
	}
})