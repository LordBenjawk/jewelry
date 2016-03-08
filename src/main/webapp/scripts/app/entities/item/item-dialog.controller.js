'use strict';

angular.module('jewelryApp').controller('ItemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Item', 'Price', 'ItemInformation', 'Size', 'Color', 'Image', 'PurchaseOrderDetails', 'Status',
        function($scope, $stateParams, $modalInstance, entity, Item, Price, ItemInformation, Size, Color, Image, PurchaseOrderDetails, Status) {

        $scope.item = entity;
        $scope.prices = Price.query();
        $scope.iteminformations = ItemInformation.query();
        $scope.sizes = Size.query();
        $scope.colors = Color.query();
        $scope.images = Image.query();
        $scope.purchaseorderdetailss = PurchaseOrderDetails.query();
        $scope.statuss = Status.query();
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
}]);
