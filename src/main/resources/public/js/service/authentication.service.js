(function () {
   angular.module('projectManagerSPA').factory('authenticationService', ['$http', '$rootScope', function ($http, $rootScope) {
       return {
           getCookie: function () {
               return {
                   'username': localStorage.getItem('username'),
                   'password': localStorage.getItem('password')
               };
           },
           isAuthenticated: function () {
               return localStorage.getItem('username') != null && localStorage.getItem('password') != null;
           },
           getCurrentUser: function (successCallback, failureCallback) {
               if($rootScope.currentUser != null) {
                   return $rootScope.currentUser;
               }

               return $http({
                   'method': 'GET',
                   'url': '/api/users/me',
                   'headers': {
                       'username': this.getCookie().username,
                       'password': this.getCookie().password
                   }
               }).then(function (response) {
                   $rootScope.currentUser = response.data;
                   successCallback(response);
               }, failureCallback);
           },
           logIn: function (username, password, successCallback) {
               localStorage.setItem('username', username);
               localStorage.setItem('password', password);

               this.getCurrentUser(successCallback, function () {
                   localStorage.clear();
               })
           },
           logOut: function () {
               $rootScope.currentUser = null;
               localStorage.clear();
           }
       };
   }])
})();