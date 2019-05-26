app.controller('payController',function ($scope,payService,$location) {
    $scope.payInit=function () {
        payService.payFor().success(function (response) {
            console.log(response)
            $scope.total_fee=response.total_fee
            $scope.out_trade_no=response.out_trade_no;
           $scope.img= response.code_url;
            $scope.status()
        })

    }
    $scope.status=function () {
        payService.statusNo($scope.out_trade_no).success(function (response) {
            if (response.success){
                location.href="/paysuccess.html#?menory="+$scope.total_fee
            } else {
                if (response.messages=='超时异常') {
                    $scope.payFor()
                }
                location.href="/payfail.html"
            }
        })
    }
    $scope.menory=function () {
        return $location.search()['menory']
    }
})