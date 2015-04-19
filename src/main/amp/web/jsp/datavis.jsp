<!DOCTYPE html>
<html>
<head>
<!-- jquery from cdn -->
<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
<!-- Load the Paper.js library -->
<script type="text/javascript" src="/alfresco/scripts/paper-full.js"></script>

<!-- <script type="text/javascript" src="js/callservice.js"></script> -->
<!-- Load external PaperScript and associate it with myCanvas -->
<script type="text/paperscript" src="/alfresco/scripts/myballs.js" canvas="myCanvas"></script>
<!-- <script type="text/paperscript" src="/alfresco/scripts/mylines.js" canvas="myLineCanvas"></script> -->
</head>
<body>
	<!--  <button type="button" onclick="myFunction()">Change Content</button>
	<div id="myDiv"><h2>Let AJAX change this text</h2></div>-->
	<canvas id="myCanvas" resize width="700" height="600"></canvas>
	<canvas id="myLineCanvas" resize width="700" height="600"></canvas>

</body>
</html>
