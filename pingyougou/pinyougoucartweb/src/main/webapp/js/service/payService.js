app.service('payService',function ($http) {
    this.payFor=function () {
        return $http.get('../pay/native.do')
    }
    this.statusNo=function (no) {
        return $http.get('../pay/status.do?out_trade_no='+no)
    }
})