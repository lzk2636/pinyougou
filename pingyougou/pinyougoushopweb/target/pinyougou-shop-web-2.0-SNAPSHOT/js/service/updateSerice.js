app.service("updateService",function ($http) {
    this.updateFile=function () {
        var formData=new FormData();
        formData.append('file',file.files[0])
       return $http({
            url:'../upLoad.do',
            method:'post',
            data:formData,
            headers:{'Content-Type':undefined},
            transformRequest: angular.identity
        })
    }
})