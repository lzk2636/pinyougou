app.controller('baseController',function ($scope) {
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }

    }
    //重新加载列表 数据
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage)
    }
    //更新复选
    $scope.selectIds=[];
    $scope.updateItem=function ($event,id) {

        if ($event.target.checked){
            $scope.selectIds.push(id)
        }else {
            var index= $scope.selectIds.indexOf();
            $scope.selectIds.splice(index,1)
        }
    }
    $scope.ChageJson=function (jsonString,key) {
        var json =JSON.parse(jsonString);

        var value="";
        for (var i=0;i<json.length;i++){
            if (i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }
    $scope.checkedIsNull=function (list,key,value) {
        for(var i=0;i<list.length;i++){
            if(list[i][key]==value){
                console.log(i)
                return list[i];
            }
        }
        return null;

    }
})