'use strict';

angular.module('jewelryApp')
	.controller('ItemDeleteController', function($scope, $modalInstance, entity, Item) {

        $scope.item = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Item.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });