angular.module('goalApp')
	.component('home', {
		templateUrl: '/app/template/goal/home.html',
		controller: function($scope, $location, GoalApi, PlanApi, CommentApi) {
			
			$scope.goToAccount = function(profile) {
				$location.path("/list/" + profile.id);
			};
			
			$scope.deleteComment =function(comment) {
				CommentApi.delete({id: comment.id});
				_.remove($scope.comments, {id: comment.id});
			};
			
			$scope.send = function(comment, goal) {
				CommentApi.save({id: goal.id}, comment, function(resp) {
					if(resp.code == 10) {
						toastr.success('Comment sent successfully');
						$scope.comments.push(resp.comment);
						$scope.newComment = {};
					}else {
						toastr.error('There is an error!');
					}
				});
			};

			$scope.goToGoal = function(goal) {
				$location.path('/goal/' + goal.id);
			};

			$scope.getComments = function(goal, $index) {
				$('#planList' + $index).collapse('hide');
				$scope.comments = CommentApi.list({id: goal.id});
			};

			$scope.getPlans = function(goal, $index) {
				$('#commentList' + $index).collapse('hide');
				PlanApi.list({ id: goal.id }, function(resp) {
					$scope.plans = resp;
				});
			};


			$scope.init = function() {
				$scope.list = GoalApi.listHome();
				$scope.newComment = {};
			};
			$scope.init();
		}
	});