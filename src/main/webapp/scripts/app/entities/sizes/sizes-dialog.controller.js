'use strict';

angular.module('jewelryApp').controller('SizesDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Sizes', 'Item',
        function($scope, $stateParams, $modalInstance, entity, Sizes, Item) {

        $scope.sizes = entity;
        $scope.items = Item.query();
        $scope.load = function(id) {
            Sizes.get({id : id}, function(result) {
                $scope.sizes = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:sizesUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sizes.id != null) {
                Sizes.update($scope.sizes, onSaveSuccess, onSaveError);
            } else {
                Sizes.save($scope.sizes, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
