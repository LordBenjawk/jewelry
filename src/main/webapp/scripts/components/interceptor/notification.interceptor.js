 'use strict';

angular.module('jewelryApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jewelryApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jewelryApp-params')});
                }
                return response;
            }
        };
    });
