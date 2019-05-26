app.service('orderService',function($http){
    this.findAll=function () {
        return $http.get('../order/findAll.do')
    }
    this.findByPage=function (page,size) {
        return $http.get('../order/byPage.do?rows='+page+'&size='+size)
    }
    this.findOne=function (id) {
        return $http.get('../order/findOne.do?id='+id)
    }
    this.updateData=function (id) {
        return $http.get('../order/updateOne.do?id='+id)
    }
        this.findPagePayNo=function (page,size) {
            return $http.get('../order/payNo.do?rows='+page+'&size='+size)
        }
    this.findPagePayOver=function (page,size) {
        return $http.get('../order/payOver.do?rows='+page+'&size='+size)
    }
    this.findPagePayToSend=function (page,size) {
        return $http.get('../order/payToSend.do?rows='+page+'&size='+size)
    }
    this.updateStatus=function (id) {
        return $http.get('../order/updateOneOrderStatus.do?id='+id)
    }
    this.updateStatusToSeven=function (id) {
        return $http.get('../order/updateStatusToSeven.do?id='+id)
    }
    this.evaluate=function (page,size) {
        return $http.get('../order/evaluate.do?rows='+page+'&size='+size)
    }
    this.findOnes=function (ids) {
     return  $http.get('../order/findOnes.do?id='+ids)
    }
})