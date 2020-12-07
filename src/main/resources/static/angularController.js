var main = angular.module('main', ['ui.router']);

//##CONFIGURATION##
main.config(function ($stateProvider) {
    var states = [
        {
            name: 'index',
            url: '/index',
            component: 'index'
        },

        {
            name: 'courses',
            url: '/courses',
            component: 'courses',
            resolve: {
                courses: function (CourseService) {
                    return CourseService.getAllCourses();
                }
            }
        },

        {
            name: 'courses.addCourse',
            url: '/addCourse',
            component: 'addCourse',
        },

        {
            name: 'course',
            url: '/course/{courseId}',
            component: 'course',
            resolve: {
                course: function (CourseService, $transition$) {
                    return CourseService.getCourse($transition$.params().courseId);
                }
            }
        },

        {
            name: 'course.membershipsUsers',
            url: '/membershipsUsers/{courseId}',
            component: 'membershipsUsers',
            resolve: {
                membershipsUsers: function (MembershipService, $transition$) {
                    return MembershipService.getMembershipsByCourse($transition$.params().courseId);
                }
            }
        },

        {
            name: 'course.membershipsUsers.addMembership',
            url: '/addMembership/{courseId}',
            component: 'addMembership',
            resolve: {
                addMembership: function (UserService, $transition$) {
                    return UserService.getAllNonMemberUsers($transition$.params().courseId);
                }
            }
        },


        {
            name: 'users',
            url: '/users',
            component: 'users',
            resolve: {
                users: function (UserService) {
                    return UserService.getAllUsers();
                }
            }
        },

        {
            name: 'users.addUser',
            url: '/addUser',
            component: 'addUser',
        },

        {
            name: 'user',
            url: '/user/{userId}',
            component: 'user',
            resolve: {
                user: function (UserService, $transition$) {
                    return UserService.getUser($transition$.params().userId);
                }
            }
        },

        {
            name: 'user.membershipsCourses',
            url: '/membershipsCourses/{userId}',
            component: 'membershipsCourses',
            resolve: {
                membershipsCourses: function (MembershipService, $transition$) {
                    return MembershipService.getMembershipsByUser($transition$.params().userId);
                }
            }
        },

        {
            name: 'logs',
            url: '/logs',
            component: 'logs',
            resolve: {
                logs: function (LogsService) {
                    return LogsService.getAllLogs();
                }
            }
        },
    ]

    states.forEach(function (state) {
        $stateProvider.state(state);
    });
});


//##COMPONENTS##
main.component('index', {
    template: '<h3>Bem vindo ao sistema de administração de cursos!</h3>' +
        '<a href="http://localhost:8080/api/log/download" target="_blank">Baixar Logs</a> <br>' +
        '<a href="http://localhost:8080/api/courses/download" target="_blank">Baixar relação de cursos</a> <br>' +
        '<a href="http://localhost:8080/api/employees/download" target="_blank">Baixar relação de funcionários</a> <br>' +
        '<a href="http://localhost:8080/api/memberships/download" target="_blank">Baixar relação de matrículas</a> <br>'
});

main.component('courses', {
    bindings: { courses: '<' },

    template: '<h3>Cursos:</h3>' +
        '<ul>' +
        '  <li ng-repeat="course in $ctrl.courses">' +
        '    <a ui-sref="course({ courseId: {{course.id}} })">' +
        '      {{course.title}}' +
        '    </a>' +
        '  </li>' +
        '</ul>' +
        '<button ui-sref="courses.addCourse" >Novo Curso</button>' +
        '<ui-view></ui-view>'
});

main.component('addCourse', {
    bindings: { addCourse: '<' },

    template: '<h3>Adicionar curso:</h3>' +
        '<form novalidate>' +
        '<label for="courseTitle">Nome do Curso:</label>' +
        '<input type="text" ng-model="courseTitle" id="courseTitle"></input>' +
        '<br>' +
        '<label for="courseDesc">Descrição:</label>' +
        '<input type="text" ng-model="courseDesc" id="courseDesc"></input>' +
        '<br>' +
        '<label for="courseLength">Carga horária:</label>' +
        '<input type="text" ng-model="courseLength" id="courseLength"></input>' +
        '<br>' +
        '<label for="courseCost">(opcional)Valor:</label>' +
        '<input type="text" ng-model="courseCost" id="courseCost"></input>' +
        '<br>' +
        '<button type="button" ui-sref="courses">Fechar</button>' +
        '<input type="button" ui-sref="courses" ng-click="createCourse(courseTitle, courseDesc, courseLength, courseCost)" value="Salvar"></input>' +
        '</form>',
    controller: 'controller'
});

main.component('course', {
    bindings: { course: '<' },
    template: '<h3>Curso:</h3>' +
        '<div>Curso: {{$ctrl.course.title}} <input  type="text" ng-model="courseTitle"></input></div>' +
        '<div>Id: {{$ctrl.course.id}}</div>' +
        '<div>Descrição: {{$ctrl.course.description}} <input  type="text" ng-model="courseDesc"></input></div>' +
        '<div>Carga Horária: {{$ctrl.course.courseLength}} <input  type="text" ng-model="courseLength"></input></div>' +
        '<div>Valor: {{$ctrl.course.cost}} <input  type="text" ng-model="courseCost"></input></div>' +
        '<button ui-sref="courses" ng-click="deleteCourse($ctrl.course.id)">Deletar</button>' +
        '<button ui-sref="course({ courseId: {{$ctrl.course.id}} })" ng-click="updateCourse($ctrl.course.id, courseTitle, courseDesc, courseLength, courseCost)">Alterar</button>' +
        '<button ui-sref="courses">Voltar</button>' +

        '<button ui-sref="course.membershipsUsers({ courseId: {{$ctrl.course.id}} })">Ver Funcionários matriculados</button>' +
        '<ui-view></ui-view>',
    controller: 'controller'
});

main.component('membershipsUsers', {
    bindings: { membershipsUsers: '<' },
    template: '<h3>{{$ctrl.membershipsUsers.memberships.length}} Usuários neste curso:</h3>' +
        '<ul>' +
        '  <li ng-repeat="membership in $ctrl.membershipsUsers.memberships">' +
        '    <a ui-sref="user({ userId: {{membership.employee.id}} })">' +
        '      {{membership.employee.name}}' +
        '    </a> - ' +
        '<button ui-sref="course.membershipsUsers({ courseId: membership.course.id })"' +
        ' ng-click="deleteMembership(membership.id)">Deletar</button>' +
        '  </li>' +
        '</ul>' +
        '<button ui-sref="course.membershipsUsers.addMembership({ courseId: {{$ctrl.membershipsUsers.courseId}} })">Matricular funcionários</button>' +
        '<ui-view></ui-view>',
    controller: 'controller'
});

main.component('addMembership', {
    bindings: { addMembership: '<' },

    template: '<h3>Matricular funcionário:</h3>' +
        '<ul>' +
        '  <li ng-repeat="user in $ctrl.addMembership.users">' +
        '      {{user.name}} - ' +
        '<button ui-sref="course.membershipsUsers.addMembership({ courseId: {{$ctrl.addMembership.courseId}} })"' +
        ' ng-click="createMembership($ctrl.addMembership.courseId, user.id)">Matricular</button>' +
        '  </li>' +
        '</ul>',
    controller: 'controller'
});

main.component('users', {
    bindings: { users: '<' },

    template: '<h3>Funcionários:</h3>' +
        '<ul>' +
        '  <li ng-repeat="user in $ctrl.users">' +
        '    <a ui-sref="user({ userId: {{user.id}} })">' +
        '      {{user.name}}' +
        '    </a>' +
        '  </li>' +
        '</ul>' +
        '<button ui-sref="users.addUser" >Novo Funcionário</button>' +
        '<ui-view></ui-view>',
    controller: 'controller'

});

main.component('addUser', {
    bindings: { addUser: '<' },

    template: '<h3>Adicionar Funcionário:</h3>' +
        '<form novalidate>' +
        '<label for="userName">Nome do funcionário:</label>' +
        '<input type="text" ng-model="userName" id="userName"></input>' +
        '<br>' +
        '<label for="userPhone">Telefone:</label>' +
        '<input type="text" ng-model="userPhone" id="userPhone"></input>' +
        '<br>' +
        '<label for="userAddress">Endereço:</label>' +
        '<input type="text" ng-model="userAddress" id="userAddress"></input>' +
        '<br>' +
        '<label for="userDate">Data de admissão:</label>' +
        '<input type="text" ng-model="userDate" id="userDate"></input>' +
        '<br>' +
        '<button type="button" ui-sref="users">Fechar</button>' +
        '<input type="button" ui-sref="users" ng-click="createUser(userName, userPhone, userAddress, userDate)" value="Salvar"></input>' +
        '</form>',
    controller: 'controller'
});

main.component('user', {
    bindings: { user: '<' },
    template: '<h3>Funcionário:</h3>' +
        '<div>Nome: {{$ctrl.user.name}} <input type="text" ng-model="userName" id="userName"></input></div>' +
        '<div>Id: {{$ctrl.user.id}}</div>' +
        '<div>Telefone: {{$ctrl.user.phone}} <input type="text" ng-model="userPhone" id="userPhone"></input></div>' +
        '<div>Endereço: {{$ctrl.user.address}} <input type="text" ng-model="userAddress" id="userAddress"></input></div>' +
        '<div>Data de admissão: {{$ctrl.user.admissionDate}} <input type="text" ng-model="userDate" id="userDate"></input></div>' +
        '<button ui-sref="users" ng-click="deleteUser($ctrl.user.id)">Deletar</button>' +
        '<button ui-sref="user({ userId: {{$ctrl.user.id}} })" ng-click="updateUser($ctrl.user.id, userName, userPhone, userAddress, userDate)">Alterar</button>' +

        '<button ui-sref="users">Voltar</button>' +

        '<button ui-sref="user.membershipsCourses({ userId: {{$ctrl.user.id}} })">Ver Matrículas</button>' +
        '<ui-view></ui-view>',
    controller: 'controller'
});

main.component('membershipsCourses', {
    bindings: { membershipsCourses: '<' },

    template: '<h3>Cursos deste usuário:</h3>' +
        '<ul>' +
        '  <li ng-repeat="membership in $ctrl.membershipsCourses">' +
        '    <a ui-sref="course({ courseId: {{membership.course.id}} })">' +
        '      {{membership.course.title}}' +
        '    </a> - ' +
        '<button ui-sref="user.membershipsCourses({ userId: membership.employee.id })"' +
        ' ng-click="deleteMembership(membership.id)">Deletar</button>' +
        '  </li>' +
        '</ul>' +
        '<ui-view></ui-view>',
    controller: 'controller'
});


main.component('logs', {
    bindings: { logs: '<' },

    template: '<h3>logs:</h3>' +
        '<p>Existem {{$ctrl.logs.length}} logs</p>' +
        '<p ng-if="$ctrl.logs.length > 0">último log criado em: {{$ctrl.logs[$ctrl.logs.length-1].dateTime}}</p>' +
        '<ul>' +
        '  <li ng-repeat="log in $ctrl.logs">' +
        '      <p>Tipo: {{log.type}} ' +
        '      / Cadastro: {{log.title}} ' +
        '      / Data: {{log.dateTime}} ' +
        '      / Operação: {{log.operation}}<p>' +
        '  </li>' +
        '</ul>'
});


//##SERVICES##
main.service('CourseService', function ($http) {
    var service = {
        getAllCourses: function () {
            return $http.get('http://localhost:8080/api/courses/', { cache: true }).then(function (resp) {
                return resp.data;
            });
        },

        getCourse: function (id) {
            function courseMatchesParam(course) {
                return Number(course.id) === Number(id);
            }

            return service.getAllCourses().then(function (course) {
                return course.find(courseMatchesParam);
            });
        }
    }

    return service;
});

main.service('UserService', function ($http) {
    var service = {
        getAllUsers: function () {
            return $http.get('http://localhost:8080/api/employees/', { cache: true }).then(function (resp) {
                return resp.data;
            });
        },

        getAllNonMemberUsers: function (courseId) {
            return $http.get('http://localhost:8080/api/employees/', { cache: true }).then(function (resp) {
                var users = [];
                users = resp.data;
                var memberships = [];
                return $http.get('http://localhost:8080/api/memberships/course/' + courseId, { cache: true }).then(function (resp) {
                    memberships = resp.data;
                    usersFinalList = { courseId: Number(courseId), users: [] };

                    for (let i = 0; i < users.length; i++) {
                        let add = true;
                        for (let x = 0; x < memberships.length; x++) {
                            if (memberships[x].employee.id === users[i].id) {
                                add = false;
                            }
                        }
                        if (add) {
                            usersFinalList.users.push(users[i]);
                        }
                    }
                    return usersFinalList;
                });
            });
        },

        getUser: function (id) {
            function userMatchesParam(user) {
                return Number(user.id) === Number(id);
            }

            return service.getAllUsers().then(function (user) {
                return user.find(userMatchesParam);
            });
        }
    }

    return service;
});


main.service('MembershipService', function ($http) {
    var service = {
        getMembershipsByCourse: function (id) {
            return $http.get('http://localhost:8080/api/memberships/course/' + id, { cache: true }).then(function (resp) {
                return { courseId: id, memberships: resp.data };
            });
        },

        getMembershipsByUser: function (id) {
            return $http.get('http://localhost:8080/api/memberships/employee/' + id, { cache: true }).then(function (resp) {
                return resp.data;
            });
        },
    }

    return service;
});

main.service('LogsService', function ($http) {
    var service = {
        getAllLogs: function () {
            return $http.get('http://localhost:8080/api/log/', { cache: true }).then(function (resp) {
                return resp.data;
            });
        }
    }

    return service;
});

//##GENERAL METHODS##

main.controller('controller', function ($scope, $http, $state) {
    $scope.courseId = null;
    $scope.courseTitle = null;
    $scope.courseDesc = null;
    $scope.courseLength = null;
    $scope.courseCost = null;

    $scope.createCourse = function (courseTitle, courseDesc, courseLength, courseCost) {
        var data = {
            title: courseTitle,
            description: courseDesc,
            courseLength: Number(courseLength),
            cost: Number(courseCost)
        };

        $http.post('http://localhost:8080/api/courses/', JSON.stringify(data)).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Curso Criado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };

    $scope.updateCourse = function (courseId, courseTitle, courseDesc, courseLength, courseCost) {
        if (courseLength !== null) {
            courseLength = Number(courseLength);
        }
        if (courseCost !== null) {
            courseCost = Number(courseCost);
        }
        var data = {
            id: courseId,
            title: courseTitle,
            description: courseDesc,
            courseLength: courseLength,
            cost: courseCost
        };

        $http.patch('http://localhost:8080/api/courses/' + courseId, JSON.stringify(data)).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Curso alterado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };

    $scope.deleteCourse = function (courseId) {
        var data = {
            "id": courseId.toString()
        };
        console.log(data);
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/api/courses/',
            data: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Curso deletado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };


    $scope.userId = null;
    $scope.userName = null;
    $scope.userPhone = null;
    $scope.userAddress = null;
    $scope.userDate = null;

    $scope.createUser = function (userName, userPhone, userAddress, userDate) {
        var data = {
            name: userName,
            phone: userPhone,
            address: userAddress,
            admissionDate: userDate
        };

        $http.post('http://localhost:8080/api/employees/', JSON.stringify(data)).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Funcionário cadastrado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };

    $scope.updateUser = function (userId, userName, userPhone, userAddress, userDate) {
        var data = {
            id: userId,
            name: userName,
            phone: userPhone,
            address: userAddress,
            admissionDate: userDate
        };

        $http.patch('http://localhost:8080/api/employees/' + userId, JSON.stringify(data)).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Cadastro de Funcionário alterado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };

    $scope.deleteUser = function (userId) {
        var data = {
            "id": userId.toString()
        };
        console.log(data);
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/api/employees/',
            data: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Cadastro de funcionário deletado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };

    $scope.membershipId = null;

    $scope.createMembership = function (courseId, userId) {
        var data = {
            course: { id: courseId },
            employee: { id: userId }
        };

        $http.post('http://localhost:8080/api/memberships/', JSON.stringify(data)).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Funcionário matriculado com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };

    $scope.deleteMembership = function (membershipId) {
        var data = {
            "id": membershipId.toString()
        };
        console.log(data);
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/api/memberships/',
            data: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            if (response.data)
                console.log(response.data);
            alert("Matrícula deletada com sucesso!");
            location.reload();
        }, function (response) {
            console.log("error");
            console.log(response);
            $scope.msg = "Service not Exists";
            $scope.statusval = response.status;
            $scope.statustext = response.statusText;
            $scope.headers = response.headers();
        });
    };
});