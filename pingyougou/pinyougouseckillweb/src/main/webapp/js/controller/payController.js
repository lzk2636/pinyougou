app.controller('payController',function ($scope,payService,$location,seckillerService) {
    $scope.payInit=function () {
        payService.payFor().success(function (response) {
            console.log(response)
            $scope.id=response.id
            $scope.total_fee=response.total_fee
            $scope.out_trade_no=response.out_trade_no;
           $scope.img= response.code_url;
            $scope.status()
        })

    }
    $scope.status=function () {
        console.log($scope.out_trade_no)
        payService.statusNo($scope.out_trade_no).success(function (response) {
            if (response.success){
                location.href="/paysuccess.html#?menory="+$scope.total_fee
            } else {
                if (response.messages=='超时异常') {
                   // $scope.status()
                    $scope.skill();
                }else {
                    location.href="/payfail.html"
                }

            }
        })
    }
    $scope.menory=function () {
        return $location.search()['menory']
    }
    $scope.skill=function () {
        seckillerService.skillTime($scope.id).success(function (response) {

            if (response.success){
                $scope.total_fee=response.total_fee
                $scope.out_trade_no=response.out_trade_no;
                $scope.img= response.code_url;
            }else {
                alert(response.messages)
            }
        })
    }
})