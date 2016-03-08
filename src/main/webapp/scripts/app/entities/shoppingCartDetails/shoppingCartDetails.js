'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('shoppingCartDetails', {
                parent: 'entity',
                url: '/shoppingCartDetailss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.shoppingCartDetails.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shoppingCartDetails/shoppingCartDetailss.html',
                        controller: 'ShoppingCartDetailsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shoppingCartDetails');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('shoppingCartDetails.detail', {
                parent: 'entity',
                url: '/shoppingCartDetails/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.shoppingCartDetails.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shoppingCartDetails/shoppingCartDetails-detail.html',
                        controller: 'ShoppingCartDetailsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shoppingCartDetails');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ShoppingCartDetails', function($stateParams, ShoppingCartDetails) {
                        return ShoppingCartDetails.get({id : $stateParams.id});
                    }]
                }
            })
            .state('shoppingCartDetails.new', {
                parent: 'shoppingCartDetails',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCartDetails/shoppingCartDetails-dialog.html',
                        controller: 'ShoppingCartDetailsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCartDetails', null, { reload: true });
                    }, function() {
                        $state.go('shoppingCartDetails');
                    })
                }]
            })
            .state('shoppingCartDetails.edit', {
                parent: 'shoppingCartDetails',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCartDetails/shoppingCartDetails-dialog.html',
                        controller: 'ShoppingCartDetailsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ShoppingCartDetails', function(ShoppingCartDetails) {
                                return ShoppingCartDetails.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCartDetails', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('shoppingCartDetails.delete', {
                parent: 'shoppingCartDetails',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/shoppingCartDetails/shoppingCartDetails-delete-dialog.html',
                        controller: 'ShoppingCartDetailsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ShoppingCartDetails', function(ShoppingCartDetails) {
                                return ShoppingCartDetails.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shoppingCartDetails', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
