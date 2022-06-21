let app = angular.module('goalApp', ['ngRoute', 'ngResource', 'ngFileUpload']);

app.config(['$routeProvider', '$locationProvider',
	function($routeProvider, $locationProvider) {
		$locationProvider.html5Mode({ enabled: true });

		$routeProvider
			.when('/profile', {
				template: '<profile><//profile>'
			})
			.when("/friends", {
				template: '<friends></friends>'
			})
			.when("/goal/:id", {
				template: '<goal></goal>'
			})
			.when("/list/:id", {
				template: '<goal-list></goal-list>'
			})
			.when("/", {
				template: "<goal-list></goal-list>"
			})
			.when('/login', {
				template: "<login></login"
			})
			.when('/signup', {
				template: "<signup></signup>"
			})
			.otherwise({
				redirectTo: '/list'
			})

	}]);


app.factory("AccountApi", ['$resource', function($resource) {

	let base = "/accounts/requests";

	return $resource(base, {}, {
		login: {
			method: 'POST',
			url: '/login'
		},
		signup: {
			method: 'POST',
			url: '/signup'
		},
		logout: {
			method: 'GET',
			url: '/logout'
		},
		getUserInfo: {
			method: 'GET',
			url: '/accounts/profile'
		},
		getProfile: {
			method: 'GET',
			url: '/accounts/:id/profile'
		},
		listFriends: {
			method: 'GET',
			url: '/accounts/friends',
			isArray: true
		},
		deleteFriend: {
			method: 'POST',
			url: '/accounts/friends/remove'
		},
		listRequests: {
			method: 'GET',
			url: base,
			isArray: true
		},
		acceptRequest: {
			method: 'POST',
			url: base + '/accept'
		},
		cancelRequest: {
			method: 'POST',
			url: base + '/cancel'
		},
		searchAccount: {
			method: 'GET',
			url: '/accounts/search/:id',
			isArray: true
		},
		remove: {
			method: 'POST',
			url: '/accounts/remove'
		}
	});
}]);


app.factory('GoalApi', ['$resource', function($resource) {

	let base = "/goals";

	return $resource(base, {}, {
		listHome: {
			method: 'GET',
			url: '/goals/home/list',
			isArray: true
		},
		list: {
			method: 'GET',
			url: base + '/list/:id',
			isArray: true
		},
		get: {
			method: 'GET',
			url: base + '/:id'
		},
		save: {
			method: 'POST',
			url: base
		},
		update: {
			method: 'PUT',
			url: base
		},
		delete: {
			method: 'DELETE',
			url: base + '/:id'
		},
		share: {
			method: 'GET',
			url: base + '/:id/share'
		}
	});
}]);

app.factory('PlanApi', ['$resource', function($resource) {

	return $resource({}, {}, {
		list: {
			method: 'GET',
			url: '/goals/:id/plans',
			isArray: true
		},
		get: {
			method: 'GET',
			url: '/plans/:id'
		},
		save: {
			method: 'POST',
			url: '/goals/:id/plans'
		},
		update: {
			method: 'PUT',
			url: '/goals/plans'
		},
		delete: {
			method: 'DELETE',
			url: '/plans/:id'
		}
	});
}]);

app.factory('CommentApi', ['$resource', function($resource) {

	let base = '/goals';

	return $resource(base, {}, {
		list: {
			method: 'GET',
			url: base + '/:id/comments',
			isArray: true
		},
		save: {
			method: 'POST',
			url: base + '/:id/comments'
		},
		delete: {
			method: 'DELETE',
			url: base + '/comments/:id'
		}

	});
}]);


app.controller('HeaderController', function($scope, $location, AccountService, NotificationService, AccountApi) {

	$scope.gotToSocial = function() {
		$scope.notify = undefined;
		NotificationService.setRequest(null);
		$location.path('/friends');
	}

	$scope.logout = function() {
		AccountService.setUser(null);
		AccountApi.logout();
		$location.path('/login');
	};

	$scope.setUserInfo = function() {
		AccountApi.getUserInfo().$promise.then(function(resp) {
			if (resp.username != null) {
				NotificationService.setUser(resp);
				AccountService.setUser(resp);
				$scope.account = AccountService.getUser();
			} else {
				AccountService.setUser(null);
				$location.path('/login');
			}
			$scope.init();
		});
	};
	$scope.setUserInfo();

	$scope.checkUserInfo = function() {
		if (AccountService.isLoggedIn()) {
			$scope.visible = true;
			$scope.account = AccountService.getUser();
		} else {
			$scope.visible = false;
			$location.path('/login');
		}
	};

	$scope.checkNotification = function() {
		AccountApi.getUserInfo(); //This is for reload
		if (NotificationService.listenRequest()) {
			if (location.pathname === '/friends') {
				$scope.notify = undefined;
			} else {
				if($scope.notify == undefined) {
					$scope.notify = 1;
				} else {
					$scope.notify ++;
				}
			}
		}
	};

	$scope.init = function() {
		$scope.checkUserInfo();
		AccountService.subscribe($scope.checkUserInfo);
		NotificationService.subscribe($scope.checkNotification);
	};
});


app.service('AccountService', function() {

	let observers = [];

	_setUser = function(usr) {
		user = usr;
		observers.forEach(f => f(user));
	}

	_getUser = function() {
		return user;
	}

	_subscribe = function(func) {
		observers.push(func);
	}

	_isLoggedIn = function() {
		if (user != null && user != undefined) {
			return true;
		} else {
			return false;
		}
	}

	_updateProfile = function(pro) {
		user = pro;
		observers.forEach(f => f(user));
	}

	return {
		setUser: _setUser,
		getUser: _getUser,
		subscribe: _subscribe,
		isLoggedIn: _isLoggedIn,
		updateProfile: _updateProfile
	}

});


app.service('NotificationService', function() {

	let observers = [];
	let request = null;
	let usr;

	function connect() {
		let socket = new SockJS('/myMessages-websocket');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function() {
			stompClient.subscribe('/queue/messages/' + usr.id, function(req) {
				request = JSON.parse(req.body);
				observers.forEach(f => f(request));
			});
		});
	};

	_sendRequest = function(req) {
		stompClient.send("/app/message/requests", {},
			JSON.stringify(req));
	};

	_subscribe = function(func) {
		observers.push(func);
	};

	_listenRequest = function() {
		if (request != null && request != undefined) {
			return true;
		} else return false;
	};

	_setUser = function(user) {
		usr = user;
		connect();
	}

	_getRequest = function() {
		return request;
	}

	_setRequest = function(sr) {
		request = sr;
	}

	return {
		setUser: _setUser,
		setRequest: _setRequest,
		getRequest: _getRequest,
		listenRequest: _listenRequest,
		sendRequest: _sendRequest,
		subscribe: _subscribe
	}
});



