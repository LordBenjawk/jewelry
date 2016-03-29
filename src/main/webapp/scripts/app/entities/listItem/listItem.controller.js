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

        $scope.processItemImages = function(itemInformations) {
            $(itemInformations).each(function(i,v) {
                if (v.items && v.items.length > 0) {
                    $(v.items).each(function(j,k) {
                        if (k.images && v.image === undefined) {
                            v.image = k.images[0].name;
                        }
                    });
                } else {
                    v.image = "default/full.png";
                }
            });

            return itemInformations;
        };
    });
