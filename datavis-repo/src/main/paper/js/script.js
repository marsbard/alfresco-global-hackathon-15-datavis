angular
.module('animateApp', [])
.directive('ball', function ($timeout) {
    return {
        restrict:'E',
        link:function (scope, element, attrs) {
            element.addClass('circle');

            scope.$watch(attrs.x, function (x) {
                element.css('left', x + 'px');
            });
            scope.$watch(attrs.y, function (y) {
                element.css('top', y + 'px');
            });
            scope.$watch(attrs.color, function (color) {
                element.css('backgroundColor', color);
            });
        }
    };
})
        
.controller('AnimateCtrl', function($scope, $timeout) {

    function buildShape() {
        var maxVelocity = 200;
        return {
            color:'#' + (Math.random() * 0xFFFFFF << 0).toString(16),
            x:Math.min(380, Math.max(20, (Math.random() * 380))),
            y:Math.min(180, Math.max(20, (Math.random() * 180))),

            velX:(Math.random() * maxVelocity),
            velY:(Math.random() * maxVelocity)
        };
    }

    ;

    // Publish list of shapes on the $scope/presentationModel
    // Then populate the list with 100 shapes randomized in position
    // and color
    $scope.shapes = [];
    for (i = 0; i < 1; i++) {
        $scope.shapes.push(buildShape());
    }

    // Start timer-based, changes of the shape properties
    animator($scope.shapes, $timeout);

});

function animator(shapes, $timeout) {
    (function tick() {
        var i;
        var now = new Date().getTime();
        var maxX = 600;
        var maxY = 600;
        var now = new Date().getTime();

        for (i = 0; i < shapes.length; i++) {
            var shape = shapes[i];
            var elapsed = (shape.timestamp || now) - now;

            shape.timestamp = now;
            shape.x += elapsed * shape.velX / 1000;
            shape.y += elapsed * shape.velY / 1000;

            if (shape.x > maxX) {
                shape.x = 2 * maxX - shape.x;
                shape.velX *= -1;
            }
            if (shape.x < 30) {
                shape.x = 30;
                shape.velX *= -1;
            }

            if (shape.y > maxY) {
                shape.y = 2 * maxY - shape.y;
                shape.velY *= -1;
            }
            if (shape.y < 20) {
                shape.y = 20;
                shape.velY *= -1;
            }
        }

        $timeout(tick, 30);
    })();
}
