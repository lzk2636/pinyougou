app.service('cartService',function ($http) {
    this.cartCookies=function () {
     return   $http.get('../cart/cartCookies.do')
    }
    this.cartItem=function (itemId,num) {
        return $http.get('../cart/cartList.do?itemId='+itemId+'&num='+num)
    }
    this.doNum=function (value) {
        var doVaue={totalNum:0,totalFees:0}
        for (var i=0;i<value.length;i++){
            var item= value[i].itemList
            for (var j=0;j<item.length;j++){
                doVaue.totalNum+=item[j].num
                doVaue.totalFees+=item[j].totalFee
            }
        }
        return doVaue;
    }
    this.addr=function () {
        return $http.get('../address/userId.do')
    }
    this.add=function (value) {
        return $http.post('/address/add.do',value)
    }
    this.queryOne=function (id) {
        return $http.get('../address/findOne.do?id='+id)
    }
    this.updates=function (obj) {
        return $http.post('/address/update.do',obj)
    }
    this.deleteItem=function (id) {
        return $http.get('../address/delete.do?ids='+id)
    }
    //提交订单
    this.submitOrder=function(order){
        return $http.post('order/add.do',order);
    }

})