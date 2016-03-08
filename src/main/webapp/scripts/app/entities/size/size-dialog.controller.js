'use strict';

angular.module('jewelryApp').controller('SizeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Size', 'Item',
        function($scope, $stateParams, $modalInstance, entity, Size, Item) {

        $scope.size = entity;
        $scope.items = Item.query();
        $scope.load = function(id) {
            Size.get({id : id}, function(result) {
                $scope.size = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:sizeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.size.id != null) {
                Size.update($scope.size, onSaveSuccess, onSaveError);
            } else {
                Size.save($scope.size, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
