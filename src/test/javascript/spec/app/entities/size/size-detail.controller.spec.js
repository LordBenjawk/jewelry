'use strict';

describe('Size Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSize, MockItem;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSize = jasmine.createSpy('MockSize');
        MockItem = jasmine.createSpy('MockItem');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Size': MockSize,
            'Item': MockItem
        };
        createController = function() {
            $injector.get('$controller')("SizeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:sizeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
