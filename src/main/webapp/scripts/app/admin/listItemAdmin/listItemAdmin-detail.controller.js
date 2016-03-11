'use strict';

angular.module('jewelryApp')
    .controller('ListItemAdminDetailController', function ($scope, $rootScope, $stateParams, entity, ListItem, FileUploader) {
        $scope.itemInformation = entity;

        var uploader = $scope.uploader = new FileUploader();

        // FILTERS

        uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
        });

        $scope.load = function (id) {
            ListItem.get({id: id}, function(result) {
                $scope.itemInformation = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:listItemUpdate', function(event, result) {
            $scope.itemInformation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
