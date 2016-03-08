'use strict';

angular.module('jewelryApp').controller('PurchaseOrderDetailsDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PurchaseOrderDetails', 'Item', 'PurchaseOrder',
        function($scope, $stateParams, $modalInstance, entity, PurchaseOrderDetails, Item, PurchaseOrder) {

        $scope.purchaseOrderDetails = entity;
        $scope.items = Item.query();
        $scope.purchaseorders = PurchaseOrder.query();
        $scope.load = function(id) {
            PurchaseOrderDetails.get({id : id}, function(result) {
                $scope.purchaseOrderDetails = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:purchaseOrderDetailsUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.purchaseOrderDetails.id != null) {
                PurchaseOrderDetails.update($scope.purchaseOrderDetails, onSaveSuccess, onSaveError);
            } else {
                PurchaseOrderDetails.save($scope.purchaseOrderDetails, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
