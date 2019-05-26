app.controller('seckillController',function ($scope,seckillerService,$location,$interval) {
    $scope.find=function () {
        seckillerService.findAll().success(function (response) {
            $scope.list=response

        })
    }
    $scope.findWith=function () {
        var id=$location.search()['id'];
        seckillerService.findOne(id).success(function (response) {
            $scope.entity=response;
            $scope.second=Math.floor((new Date(response.endTime).getTime()-new Date().getTime())/1000)
            time= $interval(function () {
                $scope.second=$scope.second-1;
                if ($scope.second<=0){
                    $interval.cancel(time)
                }
             $scope.dataFormact=$scope.setData($scope.second);
            },1000)
        })
    }
$scope.setData=function (allSecond) {
   var day=Math.floor((allSecond/(60*60*24)))
    var  hour=Math.floor((allSecond-day*60*60*24)/(60*60))
    var mins=Math.floor((allSecond-day*60*60*24-hour*60*60)/(60))
    var s=Math.floor(allSecond-day*24*60*60-hour*60*60-mins*60);
           if (day>0){
           return day+"天"+hour+":"+mins+":"+s;
           }else {
               return hour+":"+mins+":"+s;
           }
}
$scope.skill=function () {
    seckillerService.skillTime($scope.entity.id).success(function (response) {

        if (response.success){
            window.location.href='pay.html'
        }else {
            alert(response.messages)
        }
    })
}

})