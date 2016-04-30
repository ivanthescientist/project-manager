(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('projectListController', ['authenticationService', '$scope', '$state', 'projectService', function (authenticationService, $scope, $state, projectService) {
        projectService.getList(function (response) {
            $scope.list = response.data;
        });
    }]);
})();