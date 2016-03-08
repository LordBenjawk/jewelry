'use strict';

angular.module('jewelryApp')
	.controller('ImageDeleteController', function($scope, $modalInstance, entity, Image) {

        $scope.image = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Image.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });