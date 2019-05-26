app.controller('cartController',function ($scope,cartService) {
    $scope.entity={itemList:[]}
    $scope.overAll=0;
    $scope.sendPrice=10;
    $scope.retrunPrice=0;
    $scope.cartCookies=function () {
        cartService.cartCookies().success(
            function (response) {
                if (response){
                    $scope.entity=response;
                    console.log($scope.entity)

                $scope.list= cartService.doNum(response)
                    //console.log($scope.list)
                    $scope.overAll=$scope.list.totalFees+$scope.sendPrice-$scope.retrunPrice;
                }
            }
        )
    }
    $scope.cart=function (itemId,num) {
        //console.log(11111111)
        cartService.cartItem(itemId,num).success(function (response) {
           // console.log(222222)
            if (response.success){

                $scope.cartCookies();
            } else {
                alert(response.messages)
            }
        })
    }
    $scope.num=function () {
        console.log(222222)
        $scope.totalNum=0;
        $scope.totalFees=0;
        for (var i=0;i<$scope.entity.length;i++){
          var item= $scope.entity[i].itemList
            for (var j=0;j<item.length;j++){
               $scope.totalNum+=item[j].num
                $scope.totalFees+=item[j].totalFee
            }
        }
    }
    $scope.op={}
    $scope.cartDelete=function (itemId) {
        cartService.cartItem(itemId,-9999).success(function (response) {
            // console.log(222222)
            if (response.success){

                $scope.cartCookies();
            } else {
                alert(response.messages)
            }
        })
    }
        $scope.addressName=function () {
            cartService.addr().success(function (response) {
                if (response) {
                    $scope.addresses = response;
                    console.log(response)
                    for(var i=0;i<$scope.addresses.length;i++){
                        if ($scope.addresses[i].isDefault=='1'){
                           $scope.item=$scope.addresses[i];
                           break
                        }
                    }
                }
            })
        }
        $scope.addrees=function (addess) {
            $scope.item=addess
        }
        $scope.isSelected=function (addess) {
            if ($scope.item==addess){
                return true;
            } else {
                return false;
            }
        }
    $scope.addRess={contact:'',address:'',mobile:'',alias:'',notes:''}
        $scope.add=function () {
        var object;
        if ($scope.addRess.id!=null) {
            object=cartService.updates($scope.addRess)
        }else {
            object=cartService.add($scope.addRess)
        }
       object.success(function (respone) {
           if (respone.success){
               $scope.addressName();
               $scope.addRess={}
           } else {
               alert(respone.messages)
           }
       })
        }


        $scope.isWrite=function (value) {
            $scope.addRess.alias=value;
        }
        $scope.query=function (id) {
            cartService.queryOne(id).success(function (response) {
                if (response){
                    $scope.addRess=response;
                }
            })
        }
        $scope.delete=function (id) {
            cartService.deleteItem(id).success(function (response) {
                if (response.success){
                    $scope.addressName();
                } else {
                    alert(response.messages)
                }
            })
        }
        $scope.checke=function (checked) {

            if (checked.target.checked){
                $scope.addRess.isDefault=1

            }else {
                $scope.addRess.isDefault=0

            }

        }
        $scope.order={paymentType:'1'}
        $scope.selectPage=function (type) {
            $scope.order.paymentType=type
        }
        $scope.upOrder=function () {
        $scope.order.receiver=$scope.item.contact;
        $scope.order.receiverAreaName=$scope.item.address;
        $scope.order.receiverMobile=$scope.item.mobile;
            cartService.submitOrder($scope.order).success(function (response) {
               // alert(response.messages)
                if (response.success){
                    if ($scope.order.paymentType=='1'){
                        location.href="pay.html"
                    } else {
                        location.href="paysuccess.html"
                    }
                }else {
                    alert(response.messages)
                }
            })
        }
})
