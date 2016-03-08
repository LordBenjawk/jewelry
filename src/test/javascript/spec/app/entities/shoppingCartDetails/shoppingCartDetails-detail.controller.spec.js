'use strict';

describe('ShoppingCartDetails Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockShoppingCartDetails, MockShoppingCart, MockItem;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockShoppingCartDetails = jasmine.createSpy('MockShoppingCartDetails');
        MockShoppingCart = jasmine.createSpy('MockShoppingCart');
        MockItem = jasmine.createSpy('MockItem');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ShoppingCartDetails': MockShoppingCartDetails,
            'ShoppingCart': MockShoppingCart,
            'Item': MockItem
        };
        createController = function() {
            $injector.get('$controller')("ShoppingCartDetailsDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:shoppingCartDetailsUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
