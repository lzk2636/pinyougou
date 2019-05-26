app.service('addressService',function ($http) {
    this.provinces=function () {
        return $http.get('../address/provinces.do')
    }
    this.city=function (id) {
        return $http.get('../address/cities.do?id='+id)
    }
    this.area=function (id) {
        return $http.get('../address/ares.do?id='+id)
    }
})