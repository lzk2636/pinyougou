 //控制层 
app.controller('goodsController' ,function($scope,$controller,itemCatService,goodsService,brandService){
	
	$controller('baseController',{$scope:$scope});//继承
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}}
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){

		goodsService.findOne(id).success(
			function(response){
                $scope.entity= response;
                editor.html($scope.entity.goodsDesc.introduction)
				//图片
				$scope.entity.goodsDesc.itemImages=(JSON.parse($scope.entity.goodsDesc.itemImages))
				//扩展属性列表
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems)
				//规格
				$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems)
				for (var i=0;i<$scope.entity.tbItems.length;i++){
					$scope.entity.tbItems[i].spec=JSON.parse($scope.entity.tbItems[i].spec)
				}


			}
		);				
	}
	
	//保存 
	$scope.save=function(){
       // $scope.entity.goodsDesc.introduction=editor.html()
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	$scope.IsStatus=['未审核','已审核','审核没通过','已关闭']
	$scope.categoryList=[];
	$scope.brandlist=[];
	$scope.initDataCategory=function () {
		itemCatService.findAll().success(function (response) {
			for (var i=0;i<response.length;i++){
				$scope.categoryList[response[i].id]=response[i].name
			}
			//console.log($scope.categoryList)
		})
        brandService.findAll().success(function (response) {
            for (var i=0;i<response.length;i++){
              //  console.log(response[i])
                $scope.brandlist[response[i].id]=response[i].name

            }
           // console.log($scope.brandlist)
        })
	}
	$scope.setStatus=function (status) {
		goodsService.Upstatus($scope.selectIds,status).success(function (response) {
			if (response.success){
				$scope.reloadList();
				$scope.selectIds=[];
			} else {
				alert(response.messages)
			}
		})
	}

});	
