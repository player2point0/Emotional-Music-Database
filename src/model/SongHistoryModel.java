package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SongHistoryModel  extends SmartSerializable
{
	private static final long serialVersionUID = 1L;
	public ArrayList<String> videoIds;
	public ArrayList<LocalDateTime> timeStamps;
	
	
	public void addSong(String videoId,LocalDateTime time)
	{
		//add song to the videoIds list
		videoIds.add(videoId);
		timeStamps.add(time);
	}
	
	public LocalDateTime getLatestSongTime()
	{
		if(timeStamps.size() == 0)
		{
			return null;
		}
		else 
		{
			return timeStamps.get(timeStamps.size()-1);
		}
	}
	
	public String getLatestSong() 
	{
		if(videoIds.size() == 0)
		{
			return "";
		}
		else 
		{
			return videoIds.get(videoIds.size()-1);
		}
	}
}
