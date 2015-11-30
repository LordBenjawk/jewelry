'use strict';

angular.module('jewelryApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


