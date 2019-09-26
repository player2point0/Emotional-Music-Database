package views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import model.SongInfo;
import org.h2.mvstore.MVMap;

import model.SongHistoryModel;
import model.customer;
import org.json.JSONArray;
import org.json.JSONObject;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Stats extends DynamicWebPage {

    public Stats (DatabaseInterface db, FileStoreInterface fs) {
        super(db, fs);

    }

    public boolean process(WebRequest toProcess)
    {
        if(toProcess.path.equalsIgnoreCase("Stats"))
        {
            String stringToSendToWebBrowser = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
                    "    <title>Stats</title>\n" +

                    "\t\t<link href=\"https://afeld.github.io/emoji-css/emoji.css\" rel=\"stylesheet\">\n" +
                    DiscoverView.getNavBarCss() +
                    "\t\t<link href=\"css/stats.css\" rel=\"stylesheet\" type=\"text/css\">\t\t\n" +
                    "    <script src=\"https://d3js.org/d3.v4.min.js\"></script>\n"+
                    "\t\t<script src=\"js/Stats.js\"></script>\n"+
                    "  </head>\n" +
                    "  <body>\n" +
                    DiscoverView.getNavBar() +
                    "<div id=\"graph-container\">"+
                    "    <svg id=\"graph\"></svg>\n"+
                    "</div>"+
                    "  </body>\n" +
                    "</html>\n";

            toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
            return true;
        }

        else if(toProcess.path.equalsIgnoreCase("UserStats"))
        {
            //get logged in user
            String email = toProcess.cookies.get("email");
            String password =  toProcess.cookies.get("password");
            customer currentUser = getCustomer(email, password);

            JSONObject responseData = new JSONObject();
            JSONArray responseDates = new JSONArray();

            if(currentUser == null)
            {
                responseData.put("UnknownUser", true);
                toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, responseData.toString() );
                return true;
            }
            else responseData.put("UnknownUser", false);

            //get song history
            MVMap<String, SongHistoryModel> SongHistory = db.s.openMap("SongHistory");
            SongHistoryModel userSongHistory = SongHistory.get(currentUser.songHistoryId);

            //open songs
            MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");

            if(userSongHistory.timeStamps.size() == 0)
            {
                responseData.put("NoSongs", true);
                toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, responseData.toString() );
                return true;
            }
            else responseData.put("NoSongs", false);

            //index for songId and date
            int index = 0;
            LocalDate currentLoopDate = userSongHistory.timeStamps.get(0).toLocalDate();//start date
            JSONObject currentDateData = new JSONObject();
            currentDateData.put("date", currentLoopDate);
            currentDateData.put("songsPlayed", 0);
            currentDateData.put("happy", 0);
            currentDateData.put("angry", 0);
            currentDateData.put("excited", 0);
            currentDateData.put("sad", 0);
            currentDateData.put("relaxed", 0);

            //loop through playHistory till end
            while (index < userSongHistory.timeStamps.size())
            {
                LocalDate tempDate = userSongHistory.timeStamps.get(index).toLocalDate();

                if(tempDate.isEqual(currentLoopDate))
                {
                    String songId = userSongHistory.videoIds.get(index);
                    double songHappy = 0;
                    double songAngry = 0;
                    double songExcited = 0;
                    double songSad = 0;
                    double songRelaxed = 0;
                    int songIncr = 0;

                    if(SongRatings.containsKey(songId))
                    {
                        SongInfo tempSong = SongRatings.get(songId);
                        //group together by days
                        songHappy = tempSong.happy;
                        songAngry = tempSong.angry;
                        songExcited = tempSong.excited;
                        songSad = tempSong.sad;
                        songRelaxed = tempSong.relaxed;
                        songIncr = 1;
                    }


                    double happy = Double.parseDouble(currentDateData.get("happy").toString()) + songHappy;
                    double angry = Double.parseDouble(currentDateData.get("angry").toString()) + songAngry;
                    double excited = Double.parseDouble(currentDateData.get("excited").toString()) + songExcited;
                    double sad = Double.parseDouble(currentDateData.get("sad").toString()) + songSad;
                    double relaxed = Double.parseDouble(currentDateData.get("relaxed").toString()) + songRelaxed;
                    int songsPlayed = Integer.parseInt(currentDateData.get("songsPlayed").toString()) + songIncr;

                    currentDateData.put("happy", happy);
                    currentDateData.put("angry", angry);
                    currentDateData.put("excited", excited);
                    currentDateData.put("sad", sad);
                    currentDateData.put("relaxed", relaxed);
                    currentDateData.put("songsPlayed", songsPlayed);

                    index++;
                }

                else if(tempDate.isBefore(currentLoopDate)) index++;

                else if(tempDate.isAfter(currentLoopDate))
                {
                    int songsPlayedCount = Integer.parseInt(currentDateData.get("songsPlayed").toString());

                    //add date to response data
                    if(songsPlayedCount > 0)
                    {
                        responseDates.put(responseDates.length(), currentDateData);
                    }
                    //move date along
                    currentLoopDate = currentLoopDate.plusDays(1);

                    currentDateData = new JSONObject();
                    currentDateData.put("date", currentLoopDate);
                    currentDateData.put("songsPlayed", 0);
                    currentDateData.put("happy", 0);
                    currentDateData.put("angry", 0);
                    currentDateData.put("excited", 0);
                    currentDateData.put("sad", 0);
                    currentDateData.put("relaxed", 0);
                }
            }

            //edge case
            int songsPlayedCount = Integer.parseInt(currentDateData.get("songsPlayed").toString());

            //add date to response data
            if(songsPlayedCount > 0)
            {
                responseDates.put(responseDates.length(), currentDateData);
            }
            //add day to json object
            responseData.put("dates", responseDates);

            toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, responseData.toString() );
            return true;
        }

        return false;
    }

    public customer getCustomer(String email, String password)
    {
        MVMap<String, customer> custo = db.s.openMap("customer");
        List<String> keys = custo.keyList();

        for(int i = 0;i<keys.size();i++)
        {
            customer c = custo.get(keys.get(i));

            if(c.email.equals(email) && c.password.equals(password))
            {
                //valid login
                return c;
            }
        }

        return null;
    }
}
