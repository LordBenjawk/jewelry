'use strict';

describe('Category Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCategory, MockItem, MockStatus;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCategory = jasmine.createSpy('MockCategory');
        MockItem = jasmine.createSpy('MockItem');
        MockStatus = jasmine.createSpy('MockStatus');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Category': MockCategory,
            'Item': MockItem,
            'Status': MockStatus
        };
        createController = function() {
            $injector.get('$controller')("CategoryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:categoryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
