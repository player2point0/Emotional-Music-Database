function addNewFriend()
{
	var friendName = prompt("Enter Friend's Name");
	
	$.get( "/AddFriend?FriendName="+friendName, function( data ) {

		let responseData = JSON.parse(data);
						
		if(responseData.added == false)
		{
			alert("friend not found");
		}
		else location.reload();
	});	
}