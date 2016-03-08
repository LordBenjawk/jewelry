'use strict';

angular.module('jewelryApp').controller('ShoppingCartDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ShoppingCart', 'User',
        function($scope, $stateParams, $modalInstance, entity, ShoppingCart, User) {

        $scope.shoppingCart = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            ShoppingCart.get({id : id}, function(result) {
                $scope.shoppingCart = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:shoppingCartUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.shoppingCart.id != null) {
                ShoppingCart.update($scope.shoppingCart, onSaveSuccess, onSaveError);
            } else {
                ShoppingCart.save($scope.shoppingCart, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
