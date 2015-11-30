'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('color', {
                parent: 'entity',
                url: '/colors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.color.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/color/colors.html',
                        controller: 'ColorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('color');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('color.detail', {
                parent: 'entity',
                url: '/color/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.color.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/color/color-detail.html',
                        controller: 'ColorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('color');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Color', function($stateParams, Color) {
                        return Color.get({id : $stateParams.id});
                    }]
                }
            })
            .state('color.new', {
                parent: 'color',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/color/color-dialog.html',
                        controller: 'ColorDialogController',
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
                        $state.go('color', null, { reload: true });
                    }, function() {
                        $state.go('color');
                    })
                }]
            })
            .state('color.edit', {
                parent: 'color',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/color/color-dialog.html',
                        controller: 'ColorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Color', function(Color) {
                                return Color.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('color', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('color.delete', {
                parent: 'color',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/color/color-delete-dialog.html',
                        controller: 'ColorDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Color', function(Color) {
                                return Color.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('color', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
