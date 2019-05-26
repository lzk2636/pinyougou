 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location   ,goodsService,updateService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
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
	$scope.findOne=function(){
		var id=$location.search()['id']
		//alert(id)
		if (id==null){
			return
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//文本框
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
	$scope.save=function(){
		$scope.entity.goodsDesc.introduction=editor.html()
		var object=null
		if ($scope.entity.goods.id!=null){
			object=goodsService.update($scope.entity)
		} else {
			object=goodsService.add($scope.entity)
		}
		object.success(function (response) {
			if(response.success){
				alert(response.messages)
				/*$scope.entity={};
				editor.html('')*/
				location.href="/admin/goods.html"
			}else{
				alert(response.messages);
			}
		})

	}
	
	//保存
	$scope.add=function(){
		$scope.entity.goodsDesc.introduction=editor.html()
		goodsService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
				alert(response.messages)
					$scope.entity={};
				editor.html('')
				}else{
					alert(response.messages);
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

	$scope.addImage=function () {
		updateService.updateFile().success(function (response) {
			if (response.success){
				$scope.entity_image.url=response.messages;
			} else {
				alert(response.messages)
			}
		})
	}
	$scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}}
    $scope.saveImage=function () {
		$scope.entity.goodsDesc.itemImages.push($scope.entity_image)
	}
	$scope.removeImage=function (index) {
		$scope.entity.goodsDesc.itemImages.splice(index,1)
	}
	//三级查询
	$scope.findParent=function () {
		itemCatService.selectParentId(0).success(function (response) {
			$scope.itemsList1=response;
		})
	}
	$scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
		itemCatService.selectParentId(newValue).success(function (response) {
			$scope.itemsList2=response;
		})
	})
	$scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {
		itemCatService.selectParentId(newValue).success(function (response) {
			$scope.itemsList3=response;
		})
	})
	$scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {
		itemCatService.findOne(newValue).success(function (response) {
			$scope.entity.goods.typeTemplateId=response.typeId;
		})
	})
	$scope.$watch('entity.goods.typeTemplateId',function (newValue,oldvalue) {
		typeTemplateService.findOne(newValue).success(function (resopnse) {
			$scope.typeTemplate=resopnse
			$scope.typeTemplate.tolist=resopnse.brandIds;
		//	alert($scope.typeTemplate.tolist)
			$scope.typeTemplate.tolist=JSON.parse($scope.typeTemplate.tolist)
		//	$scope.entity.goods.customAttributeItems=resopnse.customAttributeItems;
			if ($location.search()['id']==null){
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems)
			}

		})
		typeTemplateService.findMapOptions(newValue).success(function (response) {
			$scope.specItemList=response;
		})
		//$scope.entity.goodsDesc.specificationItems=[]
	})

		$scope.checkedList=function ($event,name,valve) {

		var object=	$scope.checkedIsNull($scope.entity.goodsDesc.specificationItems,'attributeName',name);
		if (object!=null){
		console.log(object+'2')
			if ($event.target.checked){
				object.attributeValue.push(valve)
			} else {
				object.attributeValue.splice(object.attributeValue.indexOf(valve),1)
			}
					if (object.attributeValue.length==0){
						$scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1)
					}
		}else {
			console.log(object+'1')
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[valve]})
		}
		}
		$scope.itemListSpec=function () {

			$scope.entity.tbItems=[{spec:{},isDefault:'0', status:'0',price:100,num:9999}];
			 var items=$scope.entity.goodsDesc.specificationItems;
			for (var i=0;i<items.length;i++){
				$scope.entity.tbItems=addColumn($scope.entity.tbItems,items[i].attributeName,items[i].attributeValue);//1 1[{"attributeName":"网络","attributeValue":["移动3G"]},

			}


		}
		addColumn=function(list,Name,Value) {
		var newList=[];
		for (var i=0;i<list.length;i++){//[{spec:{},isDefault:'0', status:'0',price:100,num:9999}];
			var old=list[i];//[{spec:{},isDefault:'0', status:'0',price:100,num:9999}];
			console.log('list='+list.length+"Name="+Name)
			for (var j=0;j<Value.length;j++){//{"attributeName":"网络","attributeValue":["移动3G"]},
				console.log('value.list='+Value.length+"value="+Value)
				var newRow=	JSON.parse(JSON.stringify(old))//[{spec:{},isDefault:'0', status:'0',price:100,num:9999}];
				//alert("value=0"+Value[j])
				newRow.spec[Name]=Value[j];//[{spec:{},isDefault:'0', status:'0',price:100,num:9999}];=newRow.spec{attributeValue":["移动3G"]}
				console.log("spec="+newRow.spec[Name])
				newList.push(newRow)
			}
		}
		return newList;

	}
	$scope.IsStatus=['未审核','已审核','审核没通过','已关闭']
	$scope.categoryList=[];
	$scope.initDataCategory=function () {
		itemCatService.findAll().success(function (response) {
			for (var i=0;i<response.length;i++){
				$scope.categoryList[response[i].id]=response[i].name
			}
			console.log($scope.categoryList)
		})

	}
	$scope.checkMen=function (Name,Value) {
////specificationItems
	var object=$scope.checkedIsNull($scope.entity.goodsDesc.specificationItems,'attributeName',Name);
	console.log("name="+Name)
	console.log("Object="+object)
	if (object!=null){
				if (object.attributeValue.indexOf(Value)>=0){

					return true;
				} else {
					return false;
				}

	} else {
		//alert(222222)
		return false;
	}



	}
	$scope.isMarket=function (isMarket) {
		goodsService.isMarket($scope.selectIds,isMarket).success(function (response) {
			if (response.success){
				$scope.reloadList();
				$scope.selectIds=[];
			} else {
				alert(response.messages)
			}
		})
	}
});	
