'use strict';

angular.module('jewelryApp').controller('PriceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Price', 'Item',
        function($scope, $stateParams, $modalInstance, entity, Price, Item) {

        $scope.price = entity;
        $scope.items = Item.query();
        $scope.load = function(id) {
            Price.get({id : id}, function(result) {
                $scope.price = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:priceUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.price.id != null) {
                Price.update($scope.price, onSaveSuccess, onSaveError);
            } else {
                Price.save($scope.price, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
