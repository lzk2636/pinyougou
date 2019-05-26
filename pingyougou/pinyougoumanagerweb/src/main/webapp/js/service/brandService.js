app.service("brandService",function ($http) {
    this.findAll=function () {
        return $http.get('../brand/find.do')
    }
    this.findBypage=function (page, size) {
        return $http.get('../brand/findBypage.do?page=' + page + '&size=' + size);
    }
    this.insert=function (x) {
        return   $http.post('../brand/add.do', x)
    }
    this.findOne=function (id) {
        return   $http.get('../brand/findOne.do?id='+id)
    }
    this.deleteItem=function (deletListId) {
        return   $http.get("../brand/delete.do?ids="+ deletListId)
    }
    this.search=function (page,rows,query) {
        return  $http.post('../brand/querys.do?page=' + page + '&size=' + rows,query);
    }
    this.findBrandBylist=function () {
        return $http.get('../brand/findBrandBylist.do')
    }
})