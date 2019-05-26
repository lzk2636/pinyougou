app.controller("indexController", function ($scope, loginSerice, orderService, $location, $interval) {

    $scope.loginNames = function () {
        loginSerice.login().success(function (response) {
            $scope.loginName = response.loginName;
        })
    }
    $scope.find = function () {
        orderService.findAll().success(function (response) {
            //  $scope.entity=response;
        })
    }
    $scope.entity = {}
    $scope.list = []
    $scope.entity.pageNo = 1;
    $scope.entity.pageSize = 6;
    $scope.findByPage = function () {

        orderService.findByPage($scope.entity.pageNo, $scope.entity.pageSize).success(function (response) {

            $scope.entity = response
            $scope.num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            var num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            for (var i = 1; i <= num; i++) {
                $scope.list[i] = i;
            }
        })
    }
    $scope.findTobig = function (No) {
        $scope.pagelimit(No)
        $scope.findByPage();
    }
    $scope.findtoPay = function (NO) {
        $scope.pagelimit(No);
        $scope.findPayNo();
    }
    /*
    * 页面分页当前页大小确认
    * */
    $scope.pagelimit = function (No) {
        $scope.entity.pageNo += No
        if ($scope.entity.pageNo < 1) {
            $scope.entity.pageNo = 1;
        }
        if ($scope.entity.pageNo > (($scope.entity.totalRecord / $scope.entity.pageSize))) {
            $scope.entity.pageNo = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize);
        }
    }

    $scope.toPage = function (i) {
        $scope.entity.pageNo = i;
        $scope.findByPage();
    }
    $scope.pageButton = function (num) {
        $scope.entity.pageNo = num;
        $scope.findByPage();
    }
    $scope.payFor = function (orderId) {
        location.href = "pay.html#?id=" + orderId;
    }
    $scope.initData = function () {
        var id = $location.search()['id']
        orderService.findOne(id).success(function (respone) {
            console.log(respone)
            $scope.orderPay = respone
            $scope.payResult(id);
        })
    }
    $scope.payResult = function (id) {
        orderService.updateData(id).success(function (response) {
            if (response.success) {
                $interval(function () {
                    location.href = 'paysuccess.html#?Num=' + $scope.orderPay.payment;
                }, 5000, 5)
            } else {
                alert(response.messages)
            }

        })

    }
    $scope.pageMum = function () {
        // console.log($scope.orderPay.payment)
        return $location.search()['Num'];
    }
    $scope.findPayNo = function () {
        orderService.findPagePayNo($scope.entity.pageNo, $scope.entity.pageSize).success(function (resonse) {
            // console.log(resonse)
            $scope.entity = resonse;
            $scope.num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            var num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            for (var i = 1; i <= num; i++) {
                $scope.list[i] = i;
            }
        })
    }
    $scope.pageNoPay = function (i) {
        $scope.entity.pageNo = i;
        $scope.findPayNo();
    }
    $scope.payfor = function (page) {
        $scope.entity.pageNo = page;
        $scope.findPayNo();
    }
    $scope.payOver = function () {
        orderService.findPagePayOver($scope.entity.pageNo, $scope.entity.pageSize).success(function (resonse) {
            $scope.entity = resonse
            $scope.num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            var num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            for (var i = 1; i <= num; i++) {
                $scope.list[i] = i;
            }
        })
    }
    $scope.PayOver = function (No) {
        $scope.pagelimit(No);
        $scope.payOver();
    }
    $scope.pageCheck = function (i) {
        $scope.entity.pageNo = i;
        $scope.payOver();
    }
    /*
    * 查询已付款但未发货的订单
    * */
    $scope.payToSend=function () {
        orderService.findPagePayToSend($scope.entity.pageNo,$scope.entity.pageSize).success(function (response) {
            $scope.entity=response;
            $scope.num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            var num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            for (var i = 1; i <= num; i++) {
                $scope.list[i] = i;
            }
        })
    }
    /*
    * 上下页
    * */
        $scope.findtoSend=function (No) {
        $scope.pagelimit(No)
        $scope.payToSend()
        }
        /*
        * 获取第几页
        * */
        $scope.getPage=function (no) {
            $scope.entity.pageNo=no;
            $scope.payToSend()
        }
    /*
   * 修改订单状态
   * */
    $scope.updates=function (id) {
        orderService.updateStatus(id).success(function (response) {
            if (response.success){
                $scope.payToSend()
            } else{
                alert(response.messages)
            }
        })
    }
        /*
        * 修改订单状态为7
        * */
    $scope.updatesToSeven=function (id) {
        orderService.updateStatusToSeven(id).success(function (response) {
            if (response.success){
                $scope.payOver()
            } else{
                alert(response.messages)
            }
        })
    }
    /*
    * 评价列表分页查询
    * */
    $scope.evaluates=function () {
        orderService.evaluate($scope.entity.pageNo,$scope.entity.pageSize).success(function (reponse) {
            $scope.entity=reponse
            $scope.num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            var num = Math.ceil($scope.entity.totalRecord / $scope.entity.pageSize)
            for (var i = 1; i <= num; i++) {
                $scope.list[i] = i;
            }
        })
    }
    $scope.findtoEvaluate=function (No) {
        $scope.pagelimit(No);
        $scope.evaluates()
    }
    $scope.pageEvaluate=function (i) {
        $scope.entity.pageNo=i;
        $scope.evaluates();
    }
    $scope.IdToOrderDetail=function (id) {
        location.href='/home-orderDetail.html#?id='+id
    }
    $scope.orderDetail=function () {
     var ids= $location.search()['id']
        orderService.findOnes(ids).success(function (response) {
            $scope.Detail=response;
        })
    }
    $scope.allTime=function () {
        $scope.second=Math.floor((new Date("2019-4-30").getTime()-new Date().getTime())/1000);
        console.log($scope.second)
       time= $interval(function ( ) {
            $scope.second-=1;
            if ($scope.second<0){
                $interval.cancel(time)
            }
           $scope.splice= $scope.forMore($scope.second)
        },1000)

    }
    $scope.forMore=function (allTime) {
        var Day=Math.floor(allTime/(60*60*24))
        var H=Math.floor((allTime-Day*60*60*24)/(60*60))
        var M=Math.floor((allTime-Day*24*60*60-H*60*60)/60)
        var s=Math.floor(allTime-Day*24*60*60-H*60*60-M*60)
        if (Day>1){
            return Day+"天"+H+":"+M+":"+s;
        }else {
            return H+":"+M+":"+s;
        }
    }

})