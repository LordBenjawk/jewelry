'use strict';

angular.module('jewelryApp')
    .factory('ListItem', function ($resource, DateUtils) {
        return $resource('api/listItem/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
