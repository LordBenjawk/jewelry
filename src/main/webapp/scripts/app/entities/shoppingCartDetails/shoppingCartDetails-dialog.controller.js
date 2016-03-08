'use strict';

angular.module('jewelryApp').controller('ShoppingCartDetailsDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ShoppingCartDetails', 'ShoppingCart', 'Item',
        function($scope, $stateParams, $modalInstance, entity, ShoppingCartDetails, ShoppingCart, Item) {

        $scope.shoppingCartDetails = entity;
        $scope.shoppingcarts = ShoppingCart.query();
        $scope.items = Item.query();
        $scope.load = function(id) {
            ShoppingCartDetails.get({id : id}, function(result) {
                $scope.shoppingCartDetails = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:shoppingCartDetailsUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.shoppingCartDetails.id != null) {
                ShoppingCartDetails.update($scope.shoppingCartDetails, onSaveSuccess, onSaveError);
            } else {
                ShoppingCartDetails.save($scope.shoppingCartDetails, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
