'use strict';

describe('Sizes Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSizes, MockItem;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSizes = jasmine.createSpy('MockSizes');
        MockItem = jasmine.createSpy('MockItem');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Sizes': MockSizes,
            'Item': MockItem
        };
        createController = function() {
            $injector.get('$controller')("SizesDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:sizesUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
