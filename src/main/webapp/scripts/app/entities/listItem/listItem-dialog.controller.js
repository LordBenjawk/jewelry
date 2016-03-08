'use strict';

angular.module('jewelryApp').controller('ListItemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ItemInformation', 'Item', 'User', 'Category', 'Status',
        function($scope, $stateParams, $modalInstance, entity, ItemInformation, Item, User, Category, Status) {
            $scope.itemInformation = entity;
            $scope.items = Item.query();
            $scope.users = User.query();
            $scope.categorys = Category.query();
            $scope.statuss = Status.query();
            $scope.load = function(id) {
                ItemInformation.get({id : id}, function(result) {
                    $scope.itemInformation = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('jewelryApp:itemInformationUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.itemInformation.id != null) {
                    ItemInformation.update($scope.itemInformation, onSaveSuccess, onSaveError);
                } else {
                    ItemInformation.save($scope.itemInformation, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function() {
                $modalInstance.dismiss('cancel');
            };
        }]);
