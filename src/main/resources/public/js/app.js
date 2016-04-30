(function () {
    var app = angular.module("projectManagerSPA", [
        'ui.router',
        'ui.bootstrap',
        'toaster'
    ]);

    app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function($stateProvider, $urlRouterProvider, $httpProvider) {
        $httpProvider.interceptors.push(function ($q) {
            return {
                'responseError': function (rejection) {
                    var defer = $q.defer();
                    if(rejection.status === 401) {
                        console.log("unauthenticated");
                        window.location = "#/login";
                    }
                    if(rejection.status === 400) {
                        console.log("bad request");
                    }
                    if(rejection.status === 403) {
                        console.log("unauthorized")
                    }
                    if(rejection.status === 500) {
                        console.log("internal server error")
                    }

                    console.log(rejection.data);

                    defer.reject(rejection);
                    return defer.promise;
                }
            };
        });

        $httpProvider.interceptors.push(function () {
            return {
                request: function (config) {

                    if(config.headers.username == null && config.headers.password == null) {
                        config.headers.username = localStorage.getItem('username');
                        config.headers.password = localStorage.getItem('password');
                    }
                    return config;
                }
            };
        });

        $urlRouterProvider.otherwise('projects');

        $stateProvider.state('organizationList', {
            url: '/organizations',
            templateUrl: '/partials/organization.list.html',
            controller: 'organizationListController'
        });

        $stateProvider.state('organizationCreate', {
            url: '/organizations/new',
            templateUrl: '/partials/organization.create.html',
            controller: 'organizationCreateController'
        });

        $stateProvider.state('organizationEdit', {
            url: '/organizations/:organizationId',
            templateUrl: '/partials/organization.edit.html',
            controller: 'organizationEditController'
        });

        $stateProvider.state('projectList', {
            url: '/projects',
            templateUrl: '/partials/project.list.html',
            controller: 'projectListController'
        });

        $stateProvider.state('projectCreate', {
            url: '/projects/new',
            templateUrl: '/partials/project.create.html',
            controller: 'projectCreateController'
        });

        $stateProvider.state('projectEdit', {
            url: '/projects/:projectId',
            templateUrl: '/partials/project.edit.html',
            controller: 'projectEditController'
        });

        $stateProvider.state('userList', {
            url: '/users',
            templateUrl: '/partials/user.list.html',
            controller: 'userListController'
        });

        $stateProvider.state('userLogin', {
            url: '/login',
            templateUrl: '/partials/user.login.html',
            controller: 'userLoginController'
        });

        $stateProvider.state('userLogout', {
            url: '/logout',
            controller: ['authenticationService', '$state', function (authenticationService, $state) {
                authenticationService.logOut();
                $state.go('userLogin');
            }]
        });
    }]);

    app.run(['$state', 'authenticationService', '$rootScope', function ($state, authenticationService, $rootScope) {
        if(!authenticationService.isAuthenticated()) {
            $state.go('userLogin', {});
        } else {
            authenticationService.getCurrentUser(function (response) {
                $rootScope.currentUser = response.data;
                console.log(response.data);
            }, function () {

            })
        }
    }])
})();
