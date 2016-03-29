'use strict';

angular.module('jewelryApp').controller('ListItemAdminItemDialog',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Item', 'ItemInformation', 'Category', 'Price', 'Size', 'Color', 'Image', 'PurchaseOrderDetails', '$http', '$state',
        function($scope, $stateParams, $modalInstance, entity, Item, ItemInformation, Category, Price, Size, Color, Image, PurchaseOrderDetails, $http, $state) {
            $scope.item = entity;
            $scope.iteminformations = ItemInformation.query();
            $scope.categorys = Category.query();
            $scope.prices = Price.query();
            $scope.sizes = Size.query();
            $scope.colors = Color.query();
            $scope.images = Image.query();
            $scope.purchaseorderdetailss = PurchaseOrderDetails.query();
            $scope.$state = $state;
            $scope.load = function(id) {
                Item.get({id : id}, function(result) {
                    $scope.item = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('jewelryApp:itemUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.item.id != null) {
                    Item.update($scope.item, onSaveSuccess, onSaveError);
                } else {
                    Item.save($scope.item, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function() {
                $modalInstance.dismiss('cancel');
            };

            $scope.test = function() {
                console.log(entity);
            };

            $scope.uploadFile = function(files) {
                var fd = new FormData();
                //Take the first selected file
                fd.append("file", files[0]);
                $http.post("/api/listItem/upload/" + $scope.item.id, fd, {
                    headers: {'Content-Type': undefined },
                    transformRequest: angular.identity
                })
                .then(function() {
                    $scope.load($scope.item.itemInformation.id)
                });
            };

            $scope.confirmDelete = function (id) {
                Image.delete({id: id},
                    function () {
                        $scope.load($scope.item.id)
                    });
            };

        }]);
