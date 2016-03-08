'use strict';

angular.module('jewelryApp').controller('ImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Image', 'Item',
        function($scope, $stateParams, $modalInstance, entity, Image, Item) {

        $scope.image = entity;
        $scope.items = Item.query();
        $scope.load = function(id) {
            Image.get({id : id}, function(result) {
                $scope.image = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:imageUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.image.id != null) {
                Image.update($scope.image, onSaveSuccess, onSaveError);
            } else {
                Image.save($scope.image, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
