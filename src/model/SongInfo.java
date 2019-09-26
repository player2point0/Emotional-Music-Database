package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private final int deleteVoteCount = 1;

	public String songName;
	public double happy;
	public double angry;
	public double excited;
	public double sad;
	public double relaxed;
	public int viewCount;
	//should probably add a viewcounter for each emotion
	public String youtubeVideoId;
	public String thumbnailImg;
	public ArrayList<String> deleteIpAddresses;

	public double calcNewAvgVal(double emotion, double val)
	{
		double newTotal = emotion * (viewCount - 1);
		newTotal += val;

		return newTotal / viewCount;
	}


	//improve accuracy
	//punctuation breaks it
	public double wordPercenatge(String[] inputWords)
	{
		if(inputWords.length == 0) return 100;
		else if(inputWords[0].equals("devtest") || inputWords[0].isEmpty()) return 100;

		double percentage = 0;
		double incr = 100.0 / inputWords.length;
		List<String> searchWordsList = new ArrayList<String>(Arrays.asList(inputWords));;
		String[] songNameWords = songName.split(" ");

		//loop through each word in the songName and check it against the search words
		for(int i = 0;i<songNameWords.length;i++)
		{
			for(int j = 0;j<searchWordsList.size();j++)
			{
				String searchWord = searchWordsList.get(j).toLowerCase();
				String songWord = songNameWords[i].toLowerCase();

				if(songWord.equals(searchWord))
				{
					//if it is a match remove the word from the searchWordsList
					searchWordsList.remove(j);
					j--;
					percentage += incr;
				}
			}
		}

		return percentage;
	}

	//compare each emotion
	//return a percentage representing the similarity
	public double compareEmotions(SongInfo otherSong)
	{
		int numEmotions = 5;
		double incr = 100.0 / numEmotions;
		double outputPercentage = 0;

		//percentage difference times incr
		outputPercentage += incr * calcEmotionSimilaity(happy, otherSong.happy);
		outputPercentage += incr * calcEmotionSimilaity(angry, otherSong.angry);
		outputPercentage += incr * calcEmotionSimilaity(excited, otherSong.excited);
		outputPercentage += incr * calcEmotionSimilaity(sad, otherSong.sad);
		outputPercentage += incr * calcEmotionSimilaity(relaxed, otherSong.relaxed);

		return outputPercentage;
	}
	private double calcEmotionSimilaity(double a, double b)
	{
		//use a 1 / x graph to create an exponential increase
		double diff = Math.abs(a - b);
		double output = 1;
		double xVal;
		double yVal;

		if(diff != 0)
		{
			xVal = diff / 100.0;
			yVal = 1.0 / xVal;
		}
		else
		{
			//values are identical
			yVal = 200;
		}

		//map values
		//divide by 100
		output = yVal / 100;

		return output;
	}

	public static String simplifyTitle(String input)
	{
		boolean foundBracket = false;
		StringBuilder output = new StringBuilder(input);

		//remove anything between brackets
		for(int i = 0;i<output.length();i++)
		{
			if (output.charAt(i) == '(')
			{
				foundBracket = true;
			}

			else if (output.charAt(i) == ')')
			{
				foundBracket = false;
				output.deleteCharAt(i);
				i--;
				continue;
			}

			if(foundBracket)
			{
				output.deleteCharAt(i);
				i--;
			}
		}

		return output.toString();
	}

	//checks if enough unique ip address have voted to delete
	public boolean addDeleteVote(String ipAddress)
	{
		if(deleteIpAddresses == null)
		{
			deleteIpAddresses = new ArrayList<>();
		}

		if(!deleteIpAddresses.contains(ipAddress))
		{
			deleteIpAddresses.add(ipAddress);
		}

		return deleteIpAddresses.size() >= deleteVoteCount;
	}

	public int getVotesNeeded()
	{
		return deleteVoteCount - deleteIpAddresses.size();
	}
}

