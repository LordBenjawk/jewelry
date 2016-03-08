'use strict';

angular.module('jewelryApp')
    .controller('ListItemController', function ($scope, $state, $modal, ListItem, ParseLinks) {

        $scope.items = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ListItem.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.items = $scope.processItemImages(result);
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.items = [];
        };

        $scope.processItemImages = function(items) {
            var i;

            for (i = 0; i< items.length; i++) {
                items[i].image = items[i].items[0].images[0].name;
            }
            return items;
        }
    });
