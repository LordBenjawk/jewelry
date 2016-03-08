'use strict';

angular.module('jewelryApp')
	.controller('ItemInformationDeleteController', function($scope, $modalInstance, entity, ItemInformation) {

        $scope.itemInformation = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ItemInformation.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });