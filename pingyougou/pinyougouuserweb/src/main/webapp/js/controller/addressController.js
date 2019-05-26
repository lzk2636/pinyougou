app.controller('addressController',function (addressService,$scope) {
    $scope.itemList={province:[]}
    $scope.province=function () {
        addressService.provinces().success(function (response) {
            $scope.itemList.province=response

        })
    }

    $scope.s=function () {
       $scope.$watch('itemList.province.provinceid',function (response,old) {
           if (response!=null){
               addressService.city(response).success(function (sp) {
                   $scope.itemList.city=sp
               })
           }
           $scope.$watch('itemList.city.cityid',function (newSize,olSize) {
               console.log(newSize)
               if (newSize!=null){
                   addressService.area(newSize).success(function (py) {
                       console.log(py)
                       $scope.itemList.areses=py
                   })
               }

           })

       })
    }
})