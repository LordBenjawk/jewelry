'use strict';

angular.module('jewelryApp').controller('ColorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Color', 'Item',
        function($scope, $stateParams, $modalInstance, entity, Color, Item) {

        $scope.color = entity;
        $scope.items = Item.query();
        $scope.load = function(id) {
            Color.get({id : id}, function(result) {
                $scope.color = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:colorUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.color.id != null) {
                Color.update($scope.color, onSaveSuccess, onSaveError);
            } else {
                Color.save($scope.color, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
