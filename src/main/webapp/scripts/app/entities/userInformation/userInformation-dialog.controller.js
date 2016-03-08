'use strict';

angular.module('jewelryApp').controller('UserInformationDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'UserInformation', 'User',
        function($scope, $stateParams, $modalInstance, $q, entity, UserInformation, User) {

        $scope.userInformation = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            UserInformation.get({id : id}, function(result) {
                $scope.userInformation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jewelryApp:userInformationUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userInformation.id != null) {
                UserInformation.update($scope.userInformation, onSaveSuccess, onSaveError);
            } else {
                UserInformation.save($scope.userInformation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
