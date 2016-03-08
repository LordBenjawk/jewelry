'use strict';

angular.module('jewelryApp').controller('PurchaseOrderDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PurchaseOrder', 'PurchaseOrderDetails', 'User', 'Status',
        function($scope, $stateParams, $modalInstance, entity, PurchaseOrder, PurchaseOrderDetails, User, Status) {

        $scope.purchaseOrder = entity;
        $scope.purchaseorderdetailss = PurchaseOrderDetails.query();
        $scope.users = User.query();
        $scope.statuss = Status.query();
        $scope.load = function(id) {
            PurchaseOrder.get({id : id}, function(result) {
                $scope.purchaseOrder = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:purchaseOrderUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.purchaseOrder.id != null) {
                PurchaseOrder.update($scope.purchaseOrder, onSaveSuccess, onSaveError);
            } else {
                PurchaseOrder.save($scope.purchaseOrder, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
