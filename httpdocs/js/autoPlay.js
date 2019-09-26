var player;

function onYouTubeIframeAPIReady() {
	player = new YT.Player("player", {
		events: {
			onReady: onPlayerReady,
			onStateChange: onStateChange
		}
	});
}

function onPlayerReady(event) {
	var videoId = currentId();

	player.loadVideoById(videoId);
	event.target.playVideo();
}

function currentId() {
	var url = window.location.href;
	var params = url.split('?');
	var videoId;
	//get id
	for (var i = 0; i < params.length; i++) {
		console.log(params[i]);
		if (params[i].startsWith("id=")) {
			videoId = params[i].substring(3);
			return videoId;
		}
	}
}

function onStateChange(event) {
	console.log("state change");

	if (event.data == 0) {
		//video ended
		console.log("video ended");
		//redirect
		getNextVideoId();
	}
}

function getNextVideoId() {
	//get cookie array
	var arr = JSON.parse(localStorage.getItem("autoPlayVideoIds"));
	//pop last element
	var lastItem = arr.pop();


	if (lastItem == currentId()) {
		lastItem = arr.pop();
	}

	if (lastItem == undefined) {
		redirect("discover");
		return;
	}
	localStorage.setItem("autoPlayVideoIds", JSON.stringify(arr));

	redirect("music?id=" + lastItem);

}

function redirect(url) {
	var link = document.createElement('a');
	link.href = url;
	document.body.appendChild(link);
	link.click();
}