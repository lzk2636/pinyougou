app.controller('contentController',function ($scope,contentService) {
    $scope.groupList=[];
    $scope.initGategory=function (categoryId) {
        contentService.findCategory(categoryId).success(function (response) {
            $scope.groupList[categoryId]=response
            console.log($scope.groupList[categoryId])
            console.log('bs='+$scope.groupList[1])
            console.log('bs='+$scope.groupList[2])
            console.log('bs='+$scope.groupList[3])
            console.log('bs='+$scope.groupList[4])
            console.log('bs='+$scope.groupList[5])
            console.log('bs='+$scope.groupList[6])
        })
    }
$scope.search=function () {
    location.href="http://localhost:9104/search.html#?keyword="+$scope.keywords;
}
})