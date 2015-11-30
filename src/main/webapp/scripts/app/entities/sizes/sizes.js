'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sizes', {
                parent: 'entity',
                url: '/sizess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.sizes.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sizes/sizess.html',
                        controller: 'SizesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sizes');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sizes.detail', {
                parent: 'entity',
                url: '/sizes/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.sizes.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sizes/sizes-detail.html',
                        controller: 'SizesDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sizes');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Sizes', function($stateParams, Sizes) {
                        return Sizes.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sizes.new', {
                parent: 'sizes',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sizes/sizes-dialog.html',
                        controller: 'SizesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sizes', null, { reload: true });
                    }, function() {
                        $state.go('sizes');
                    })
                }]
            })
            .state('sizes.edit', {
                parent: 'sizes',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sizes/sizes-dialog.html',
                        controller: 'SizesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sizes', function(Sizes) {
                                return Sizes.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sizes', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sizes.delete', {
                parent: 'sizes',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sizes/sizes-delete-dialog.html',
                        controller: 'SizesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Sizes', function(Sizes) {
                                return Sizes.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sizes', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
