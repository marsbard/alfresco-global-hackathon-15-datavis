/* My Balls */

var serviceUrl = "http://localhost:8080/alfresco/s/ootb/datavis/counters.json?guest=true"

var interval=900;
var stagger=300;
var ykick_scale=4;


var G={};
G.counts={};
G.counts.created=0;
G.counts.updated=0;
G.counts.deleted=0

G.prev={};
G.prev.created=0;
G.prev.updated=0;
G.prev.deleted=0


var pokeBallVectors = function(d){

	// here we just set the values, the ball loop will
	// take them off, work out diffs and set the prev values
	
//	G.counts.created = d.createCount;
//	G.counts.updated = d.updateCount;
//	G.counts.deleted = d.deleteCount;
	
	setTimeout(function(){G.counts.created=d.createCount}, stagger * 0);
	setTimeout(function(){G.counts.updated=d.updateCount}, stagger * 1);
	setTimeout(function(){G.counts.deleted=d.deleteCount}, stagger * 2);
	
}
var getCounters = function(){
	$.ajax({
		url: serviceUrl,	
		error: ajaxError,
		success: pokeBallVectors,
	});
}
var getInitialCounts = function(){
	$.ajax({
		url: serviceUrl + "&init=1",
		error: ajaxError,
		success: function(d){
			G.counts.created=d.createCount;
			G.counts.updated=d.updateCount;
			G.counts.deleted=d.deleteCount;
			G.prev.created=d.createCount;
			G.prev.updated=d.updateCount;
			G.prev.deleted=d.deleteCount;
		}
	})

	setInterval(getCounters, interval);

}
getInitialCounts();

function ajaxError(msg){
	//alert("Error during ajax: " + msg);
	// just ignore it
}



var checkDiff = function(what){
	var diff=0;
	var thiscount=0;
	switch(what){
	case "created":
		if(G.counts.created != G.prev.created ){
			diff = G.counts.created - G.prev.created;
			G.prev.created = G.counts.created;
			thiscount=G.counts.created;
		}
		break;
	case "updated":
		if(G.counts.updated != G.prev.updated ){
			diff = G.counts.updated - G.prev.updated;
			G.prev.updated = G.counts.updated;
			thiscount=G.counts.updated;
		}
		break;
	case "deleted":
		if(G.counts.deleted != G.prev.deleted ){
			diff = G.counts.deleted - G.prev.deleted;
			G.prev.deleted = G.counts.deleted;
			thiscount=G.counts.deleted;
		}
		break;
	}
//	if(console) {
//		if(diff > 0)
//		console.log(what + " diff " + diff + " #" + thiscount);
//	}

	
	return diff
}


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

	this.ykick=0;
	
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

	// hackathon - here is where we kick the balls vertically
	this.vector.y -= this.ykick * ykick_scale;
	this.ykick=0;
	
	
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



function onFrame() {
	for (var i = 0, l = balls.length; i < l; i++){
		var kick=0;
		switch(i){
		case 0: kick=checkDiff("created"); break
		case 1: kick=checkDiff("updated"); break
		case 2: kick=checkDiff("deleted"); break
		}
		balls[i].ykick=kick;
		balls[i].iterate();
		
	}
		
}
