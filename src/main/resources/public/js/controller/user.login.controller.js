(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('userLoginController', ['authenticationService', '$scope', '$state',function (authenticationService, $scope, $state) {
        $scope.logIn = function () {
            authenticationService.logIn($scope.username, $scope.password, function () {
                $state.go('organizationList');
            });
        };

        $scope.logOut = function () {
            $state.go('userLogout');
        };
    }]);
})();