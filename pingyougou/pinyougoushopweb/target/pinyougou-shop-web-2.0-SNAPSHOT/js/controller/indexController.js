app.controller("indexController",function ($scope,loginSerice) {

    $scope.loginNames=function () {
        loginSerice.login().success(function (response) {
            $scope.loginName=response.loginName;
        })
    }
})