var app = angular.module('myapp',[])
//过滤器
app.filter('asHtml',function ($sce) {
    return function (data) {
        return  $sce.trustAsHtml(data)
    }
})