angular.module('main', [])
.controller('memberships', function($scope, $http) {
    $http.get('localhost:8080/api/courses/2').
        then(function(response) {
            console.log(response);
            $scope.membershipList = response.data;
        });
});