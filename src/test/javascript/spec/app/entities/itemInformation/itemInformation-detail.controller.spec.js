'use strict';

describe('ItemInformation Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockItemInformation, MockItem, MockUser, MockCategory, MockStatus;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockItemInformation = jasmine.createSpy('MockItemInformation');
        MockItem = jasmine.createSpy('MockItem');
        MockUser = jasmine.createSpy('MockUser');
        MockCategory = jasmine.createSpy('MockCategory');
        MockStatus = jasmine.createSpy('MockStatus');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ItemInformation': MockItemInformation,
            'Item': MockItem,
            'User': MockUser,
            'Category': MockCategory,
            'Status': MockStatus
        };
        createController = function() {
            $injector.get('$controller')("ItemInformationDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:itemInformationUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
