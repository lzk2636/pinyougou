 //控制层 
app.controller('itemCatController' ,function($scope,$controller ,itemCatService, typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){

		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;
				alert($scope.entity.typeId)
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			$scope.entity.parentId=$scope.parentIds;
			$scope.entity.typeId=$scope.entity.typeId.id;
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			$scope.entity.parentId=$scope.parentIds;
			$scope.entity.typeId=$scope.entity.typeId.id;
		serviceObject=itemCatService.add( $scope.entity  );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					/*//重新查询
		        	$scope.reloadList();//重新加载*/
					$scope.findParentId($scope.parentIds)
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//逐级查询
	$scope.parentIds=0;
	$scope.findParentId=function (parentId) {
		$scope.parentIds=parentId;
		itemCatService.selectParentId(parentId).success(function (response) {
			$scope.list=response;
		})
	}

	$scope.grade=1;

	$scope.setGrape=function (valuse) {
		$scope.grade=valuse;
	}
	$scope.selectId=function (Entity) {
	//	alert($scope.grade)
		if ($scope.grade==1){
			$scope.entity_1=null;
			$scope.entity_2=null;
		}
		if ($scope.grade==2){
			$scope.entity_1=Entity;
		}
		if ($scope.grade==3){
			$scope.entity_2=Entity;
		}
		$scope.findParentId(Entity.id)
	}
	$scope.toList={data:[]}
	$scope.findAllType=function () {
		typeTemplateService.simpleExample().success(function (response) {
		$scope.toList={data:response}
		})
	}
	$scope.findOnes=function(id){

		itemCatService.findOnes(id).success(
			function(response){
				$scope.entity= response.itemCat;
				$scope.entity.typeId=response.map;
			}
		);
	}
});	
