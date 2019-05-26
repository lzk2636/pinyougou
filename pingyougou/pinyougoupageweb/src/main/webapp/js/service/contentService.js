app.service("contentService",function ($http) {
        this.findCategory=function (categoryId) {
            return $http.get('/content/findCategoryId.do?categoryId='+categoryId)
        }
})