'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('shoppingCartCustomer', {
                parent: 'entity',
                url: '/shoppingCartCustomer',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.shoppingCartCustomer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shoppingCartCustomer/shoppingCartCustomer.html',
                        controller: 'ShoppingCartCustomerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shoppingCartCustomer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            //.state('shoppingCart.detail', {
            //    parent: 'entity',
            //    url: '/shoppingCart/{id}',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //        pageTitle: 'jewelryApp.shoppingCart.detail.title'
            //    },
            //    views: {
            //        'content@': {
            //            templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-detail.html',
            //            controller: 'ShoppingCartDetailController'
            //        }
            //    },
            //    resolve: {
            //        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            //            $translatePartialLoader.addPart('shoppingCart');
            //            return $translate.refresh();
            //        }],
            //        entity: ['$stateParams', 'ShoppingCart', function($stateParams, ShoppingCart) {
            //            return ShoppingCart.get({id : $stateParams.id});
            //        }]
            //    }
            //})
            //.state('shoppingCart.new', {
            //    parent: 'shoppingCart',
            //    url: '/new',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-dialog.html',
            //            controller: 'ShoppingCartDialogController',
            //            size: 'lg',
            //            resolve: {
            //                entity: function () {
            //                    return {
            //                        id: null
            //                    };
            //                }
            //            }
            //        }).result.then(function(result) {
            //            $state.go('shoppingCart', null, { reload: true });
            //        }, function() {
            //            $state.go('shoppingCart');
            //        })
            //    }]
            //})
            //.state('shoppingCart.edit', {
            //    parent: 'shoppingCart',
            //    url: '/{id}/edit',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-dialog.html',
            //            controller: 'ShoppingCartDialogController',
            //            size: 'lg',
            //            resolve: {
            //                entity: ['ShoppingCart', function(ShoppingCart) {
            //                    return ShoppingCart.get({id : $stateParams.id});
            //                }]
            //            }
            //        }).result.then(function(result) {
            //            $state.go('shoppingCart', null, { reload: true });
            //        }, function() {
            //            $state.go('^');
            //        })
            //    }]
            //})
            .state('shoppingCartCustomer.delete', {
                parent: 'shoppingCartCustomer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCartCustomer/shoppingCartCustomer-delete-dialog.html',
                        controller: 'ShoppingCartCustomerDeleteController',
                        size: 'md',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('shoppingCartCustomer');
                                return $translate.refresh();
                            }],
                            entity: ['ShoppingCart', function() {
                                return {id : $stateParams.id};
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCartCustomer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
