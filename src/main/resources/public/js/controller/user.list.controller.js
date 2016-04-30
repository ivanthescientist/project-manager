(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('userListController', ['authenticationService', '$scope', '$state', 'userService', function (authenticationService, $scope, $state, userService) {
        userService.getList(function (response) {
            $scope.list = response.data;
        });
    }]);
})();