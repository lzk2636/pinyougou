app.controller('brandController', function ($scope,$controller, brandService) {//controller

        $controller('baseController',{$scope:$scope})

    $scope.findAll = function () {
        brandService.findAll().success(
            function (reponse) {
                $scope.list = reponse;
            }
        )
    }

    $scope.findBypage = function (page, size) {
        brandService.findBypage(page, size).success(
            function (resoponse) {
                $scope.list = resoponse.rows;
                $scope.paginationConf.totalItems = resoponse.total;
            }
        )
    }


    $scope.insert = function () {
        brandService.insert($scope.x).success(
            function (resonse) {
                if (resonse.success) {
                    $scope.reloadList();
                } else {
                    alert(resonse.messages)
                }
            }
        )
    }
    $scope.findOne=function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.x=response;
            }
        )
    }

    $scope.dele=function () {
        if (confirm("你确定删除吗")) {
            brandService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    $scope.reloadList();
                }else {
                    alert(response.messages)
                }
            })
        }

    }
    $scope.query={};
    $scope.search=function (page,rows) {
        brandService.search(page,rows,$scope.query).success(
            function (resoponse) {
                $scope.list = resoponse.rows;
                $scope.paginationConf.totalItems = resoponse.total;
            }
        )
    }

})