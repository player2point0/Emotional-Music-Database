function deleteSong(songID)
{
	let readyToDelete = confirm("Delete Song?");
					
	if(readyToDelete)
	{
		$.get( "/removesong?id="+songID, function( data ) {

			let responseData = JSON.parse(data);
						
			if(responseData.deleted)
			{
				//$("#"+songID).html(data);
				$("#"+songID).remove();

				//remove the song id from the autoPlay ids in the local storage
				let arr = JSON.parse(localStorage.getItem("autoPlayVideoIds"));

				for(let i = 0;i<arr.length;i++)
				{
					if(arr[i] == songID)
					{
						console.log("removing autoplay song id");
						arr.splice(i, 1);
					}
				}

				localStorage.setItem("autoPlayVideoIds", JSON.stringify(arr));
			}

			else 
			{
				alert( "Nah mate, "+responseData.votesNeeded+" more votes needed" );
			}
		});		
	}
}