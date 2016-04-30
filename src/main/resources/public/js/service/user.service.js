(function () {
    'use strict';
    angular.module('projectManagerSPA').factory('userService', ['$http', function ($http) {
        return {
            getList: function () {
                $http({});
            },
            registerOrganizationMember: function (username, password, organizationId) {
                return $http({
                    'method': 'POST',
                    'url': '/api/users',
                    'data': {
                        'username': username,
                        'password': password,
                        'organizationId': organizationId
                    }
                }).then(successCallback);
            },
            registerOrganizationOwner: function (username, password) {
                return $http({
                    'method': 'POST',
                    'url': '/api/users/organization-owners',
                    'data': {
                        'username': username,
                        'password': password
                    }
                }).then(successCallback);
            }
        };
    }]);
})();