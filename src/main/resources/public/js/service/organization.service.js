(function () {
    angular.module('projectManagerSPA').factory('organizationService', ['$http', function ($http) {
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
            create: function (name, description, successCallback) {
                return $http({
                    'method': 'POST',
                    'url': '/api/organizations',
                    'data': {
                        'name': name,
                        'description': description
                    }
                }).then(successCallback, function (error) {
                    console.log(error);
                });
            },
            update: function (id, name, description, successCallback) {
                return $http({
                    'method': 'PUT',
                    'url': '/api/organizations/' + id,
                    'data': {
                        'name': name,
                        'description': description
                    }
                }).then(successCallback);
            },
            addMember: function (organizationId, userId, successCallback) {
                return $http({
                    'method': 'POST',
                    'url': '/api/organizations/' + organizationId + '/members',
                    'data': {
                        'userId': userId
                    }
                }).then(successCallback);
            },
            removeMember: function (organizationId, userId, successCallback) {
                return $http({
                    'method': 'DELETE',
                    'url': '/api/organizations/' + organizationId + '/members/' + userId
                }).then(successCallback);
            }
        };
    }]);
})();