'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('shoppingCart', {
                parent: 'entity',
                url: '/shoppingCarts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.shoppingCart.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shoppingCart/shoppingCarts.html',
                        controller: 'ShoppingCartController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shoppingCart');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('shoppingCart.detail', {
                parent: 'entity',
                url: '/shoppingCart/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.shoppingCart.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-detail.html',
                        controller: 'ShoppingCartDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shoppingCart');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ShoppingCart', function($stateParams, ShoppingCart) {
                        return ShoppingCart.get({id : $stateParams.id});
                    }]
                }
            })
            .state('shoppingCart.new', {
                parent: 'shoppingCart',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-dialog.html',
                        controller: 'ShoppingCartDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCart', null, { reload: true });
                    }, function() {
                        $state.go('shoppingCart');
                    })
                }]
            })
            .state('shoppingCart.edit', {
                parent: 'shoppingCart',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-dialog.html',
                        controller: 'ShoppingCartDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ShoppingCart', function(ShoppingCart) {
                                return ShoppingCart.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCart', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('shoppingCart.delete', {
                parent: 'shoppingCart',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCart/shoppingCart-delete-dialog.html',
                        controller: 'ShoppingCartDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ShoppingCart', function(ShoppingCart) {
                                return ShoppingCart.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCart', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
