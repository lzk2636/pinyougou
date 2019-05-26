app.service('seckillerService',function ($http) {
    this.findAll=function () {
        return $http.get('/seckillGoods/Add.do')
    }
    this.findOne=function (id) {
        return $http.get('/seckillGoods/One.do?id='+id)
    }
    this.skillTime=function (sellid) {
        return $http.get('/seckillOrder/seckillTime.do?seckillId='+sellid)

    }
})