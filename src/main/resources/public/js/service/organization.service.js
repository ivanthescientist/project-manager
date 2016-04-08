(function () {
    angular.module('projectManagerSPA').factory('organizationService',['$http', function ($http) {
        return {
            getList: function (successCallback) {
                return $http({
                    'method': 'GET',
                    'url': '/api/organizations'
                }).then(successCallback);
            },
            getById: function (id, successCallback) {
                return $http({
                    'method': 'GET',
                    'url': '/api/organizations/' + id
                }).then(successCallback);
            },
            create: function (command, successCallback) {
                return $http({

                }).then(successCallback);
            }
        };
    }]);
})();