/* My Balls */

var serviceUrl = "http://localhost:8080/alfresco/s/ootb/counters.json?guest=true"

//
//var getCounters = function(){
//	$.ajax({
//		url: serviceUrl,	
//		error: ajaxError,
//		success: pokeBallVectors,
//	});
//}

function ajaxError(msg){
	alert("Error during ajax: " + msg);
}

var pokeBallVectors = function(d){
	alert("pokeBallVectors:" + d);
}

//var vectors = [];

//setTimeout(fakeCounters,1000);
//var fakeCounters = function(){
//	alert("HOASD");
//	for(var i=0; i<3; i++){
//		var kick=Math.random()*10;
//		alert(i + "=" + kick);
//		balls[i].vector.y -= kick;
//	}
//}

//setTimeout(fakeit, 100);
//
//var fakeit= function(){
//	alert("blah");
//};


var Ball = function(point, vector) {
	if (!vector || vector.isZero()) {
//		this.vector = Point.random() * 5;
	} else {
		this.vector = vector * 2;
	}
	this.point = point;
	this.dampen = 0.4;
	this.gravity = 3;
	this.bounce = -0.6;

	var color = {
		hue: Math.random() * 360,
		saturation: 1,
		brightness: 1
	};
	var gradient = new Gradient([color, 'black'], true);

//	var radius = this.radius = 50 * Math.random() + 30;
	var radius= this.radius = 40;
	// Wrap CompoundPath in a Group, since CompoundPaths directly 
	// applies the transformations to the content, just like Path.
	var ball = new CompoundPath({
		children: [
			new Path.Circle({
				radius: radius
			}),
			new Path.Circle({
				center: radius / 3,
				radius: radius / 2
			})
		],
		fillColor: new Color(gradient, 0, radius, radius / 3),
	});

	this.item = new Group({
		children: [ball],
		transformContent: false,
		position: this.point
	});
}

Ball.prototype.iterate = function(ballIdx) {
	var size = view.size;
	this.vector.y += this.gravity;
//	this.vector.x *= 0.99;
	var pre = this.point + this.vector;
	
	// some x dampening probably no longer relevant (marsbard)
	if (pre.x < this.radius || pre.x > size.width - this.radius)
		this.vector.x *= -this.dampen;
	
	// bouncing
	if (pre.y <= this.radius || pre.y >= size.height - this.radius) {
//		if (Math.abs(this.vector.x) < 3)
//			this.vector = Point.random() * [150, 100] + [-75, 20];
		this.vector.y *= this.bounce;
	}

	
	var max = Point.max(this.radius, this.point + this.vector);
	this.item.position = this.point = Point.min(max, size - this.radius);
//	this.item.rotate(Math.random()*10-5);
};


var balls = [];
for (var i = 0; i < 3; i++) {
	//var position = Point.random() * view.size,
		//vector = (Point.random() - [0.5, 0]) * [50, 100],
	var position= new Point(i*300, 50),
		vector = new Point(0, 10),
		ball = new Ball(position, vector);
	balls.push(ball);
}

var fontSize='16pt';
var textItem = new PointText({
	point: [0, 30],
	fillColor: 'black',
	fontSize: fontSize,
	content: 'onCreateNode'
});

var textItem = new PointText({
	point: [200, 30],
	fillColor: 'black',
	fontSize: fontSize,
	content: 'onUpdateNode'
});

var textItem = new PointText({
	point: [400, 30],
	fillColor: 'black',
	fontSize: fontSize,
	content: 'onDeleteNode'
});


//var lastDelta;
//function onMouseDrag(event) {
//	lastDelta = event.delta;
//}
//
//function onMouseUp(event) {
//	var ball = new Ball(event.point, lastDelta);
//	balls.push(ball);
//	lastDelta = null;
//}

function onFrame() {
	for (var i = 0, l = balls.length; i < l; i++)
		balls[i].iterate();
}
