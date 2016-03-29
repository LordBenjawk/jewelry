'use strict';

angular.module('jewelryApp')
    .factory('ShoppingCartService', function ($http) {
        return {
            addItemIntoShoppingCart: function(item) {
                if (item.quantity === undefined) return null;

                $http.get('/api/shoppingCart/addToCart/' + item.id  + "/" + item.quantity)
                    .success(function (response) {
                        return response;
                    });
            },
            getShoppingCartItems: function() {
                return $http.get('/api/shoppingCart/getCurrent')
                    .then(function (response) {
                        return response;
                    });
            },
            placeOrder: function(shoppingCart) {
                if (shoppingCart === undefined) return null;

                $http.post('api/purchaseOrders/placeOrder/', shoppingCart);
            }

        };
    });
