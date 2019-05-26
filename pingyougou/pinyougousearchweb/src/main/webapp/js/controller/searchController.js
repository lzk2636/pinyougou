app.controller('searchController',function ($scope,$location,searchService) {
    ///搜索对象
    $scope.searchMap={keywords:'',category:'',brand:'',spec:{},price:'',pageSize:40,pageNum:1,sort:'',sortField:''}
    $scope.listMap={totalPage:0}
    $scope.search=function () {
        searchService.search($scope.searchMap).success(function (response) {
            console.log(response)
            $scope.listMap=response

            $scope.makePage()

        })
    }
    $scope.makePage=function(){
        var fristPage=1;
        var lastPape=$scope.listMap.totalPage;
        $scope.isFrist=true
        $scope.isEnd=true
        if ($scope.listMap.totalPage>5){//1 2 3 4 5 6
          /*  alert(lastPape)*/
            if ($scope.searchMap.pageNum<=3){//1 ,2,3
                fristPage=1
             //   alert(lastPape)
                lastPape=5
                $scope.isFrist=false
            } else if ($scope.searchMap.pageNum>=$scope.listMap.totalPage-2) {// 96 97 98 99 100
                fristPage=$scope.listMap.totalPage-4;
                $scope.isEnd=false
            }else {
                fristPage=$scope.searchMap.pageNum-2;
                lastPape=$scope.searchMap.pageNum+2;
            }
        }else {
            $scope.isFrist=false
            $scope.isEnd=false
        }
        ////循环产生页码标签
        $scope.pageTotal=[];
        for (var i=fristPage;i<=lastPape;i++){
            $scope.pageTotal.push(i)
        }
        console.log($scope.pageTotal)
    }
    $scope.selectItem=function (key,value) {
        if (key=='brand'||key=='category'||key=='price'){
            $scope.searchMap[key]=value;
        }else {
            $scope.searchMap.spec[key]=value;
        }
        $scope.search()
    }
    $scope.deleteItem=function (key) {

        if (key=='brand'||key=='category'||key=='price'){
            $scope.searchMap[key]="";
        }else {
        delete    $scope.searchMap.spec[key]
        }
        $scope.search()
    }
    $scope.pageByNum=function (pages) {
        console.log(page+"2")
        var page=parseInt(pages)
        if (page<1 || page>$scope.listMap.totalPage) {
            return;
        }
        $scope.searchMap.pageNum=page;
        $scope.search();
    }
    $scope.isFristpage=function () {
        if ($scope.searchMap.pageNum==1){
            return true;
        } else {
            return false;
        }
    }

    $scope.isEndPage=function () {

        if ($scope.searchMap.pageNum==$scope.listMap.totalPage){
            return true;
        } else {
            return false;
        }
    }
    $scope.searchSort=function (sort,sortField) {
        $scope.searchMap.sort=sort;
        $scope.searchMap.sortField=sortField;
        $scope.search()
    }
    $scope.isBrand=function () {
        for (var i=0;i<$scope.listMap.brand.length;i++){
           // console.log(11111)
           // console.log($scope.listMap.brand[i].text)
            if ($scope.searchMap.keywords.indexOf($scope.listMap.brand[i].text)>=0){
                //console.log(11111)
                return true
            }
        }
        return false
    }
    $scope.reloadOnSearch=function () {
      $scope.searchMap.keywords=$location.search()['keyword'];
      $scope.search()
    }
})