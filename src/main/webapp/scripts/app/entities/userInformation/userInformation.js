'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userInformation', {
                parent: 'entity',
                url: '/userInformations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.userInformation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userInformation/userInformations.html',
                        controller: 'UserInformationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userInformation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userInformation.detail', {
                parent: 'entity',
                url: '/userInformation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.userInformation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userInformation/userInformation-detail.html',
                        controller: 'UserInformationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userInformation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserInformation', function($stateParams, UserInformation) {
                        return UserInformation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userInformation.new', {
                parent: 'userInformation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userInformation/userInformation-dialog.html',
                        controller: 'UserInformationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    priceTier: null,
                                    vip: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userInformation', null, { reload: true });
                    }, function() {
                        $state.go('userInformation');
                    })
                }]
            })
            .state('userInformation.edit', {
                parent: 'userInformation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userInformation/userInformation-dialog.html',
                        controller: 'UserInformationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserInformation', function(UserInformation) {
                                return UserInformation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userInformation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userInformation.delete', {
                parent: 'userInformation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userInformation/userInformation-delete-dialog.html',
                        controller: 'UserInformationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserInformation', function(UserInformation) {
                                return UserInformation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userInformation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
