(function() {
    'use strict';
    angular
        .module('loginappApp')
        .factory('Account', Account);

    Account.$inject = ['$resource'];

    function Account ($resource) {
        var service = $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false
                ,interceptor: {
                    response: function(response) {
                        console.log(response);
                        //alert('welcome '+ response.data.login);
                        // expose response
                        return response;
                    }
                }
            }
        });
        return service;
    }
})();
