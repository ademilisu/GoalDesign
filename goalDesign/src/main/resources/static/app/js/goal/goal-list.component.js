angular.module('goalApp')
	.component('goalList', {
		templateUrl: '/app/template/goal/goal-list.html',
		controller: function($scope, $routeParams, GoalApi, $location, AccountApi) {
			$scope.id = $routeParams.id;
			
			$scope.selectGoal = function(goal) {
				$location.path('/goal/' + goal.id);
			};

			$scope.deleteGoal = function(goal) {
				GoalApi.delete({ id: goal.id }, function() {
					toastr.success('Successfully deleted');
					_.remove($scope.goals, { id: goal.id });
				});
			};

			$scope.addGoal = function() {
				if ($scope.newGoal.title == null) {
					toastr.warning("Empty title!");
				} else {
					GoalApi.save($scope.newGoal, function(response) {
						if (response.code == 10) {
							$scope.goals.unshift(response.goal);
							toastr.success("new Goal successfully created");
						} else if (response.code == 1) {
							toastr.error('There is a goal with same name');
						} else {
							toastr.error('There is an error!');
						}
					});
				}
				$scope.newGoal = {};
			};

			$scope.init = function() {
				$scope.newGoal = {};
				if($scope.id === 'me') {
					$scope.visible = true;
					$scope.usr = user;
					$scope.goals = GoalApi.list({id: $scope.usr.id});
				} else {
					AccountApi.getProfile({id: $scope.id}, function(resp) {
						$scope.goals = GoalApi.list({id: resp.id});
						$scope.usr = resp;
						if($scope.usr.username === user.username) {
							$scope.visible = true;
						}
					});
				}
			}
			$scope.init();
		}
	});
