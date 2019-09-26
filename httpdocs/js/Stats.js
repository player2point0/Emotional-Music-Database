$( document ).ready(function() {
    
    //horizontal scroll
    /*
    $("body").mousewheel(function(event, delta) {

        this.scrollLeft -= (delta * 30);
      
        event.preventDefault();
  
     });
    */
   
    //get the data from the stats api
    $.get( "/UserStats", function( data ) {

        let responseData = JSON.parse(data);

        console.log(responseData);

        if(responseData.NoSongs)
        {
            $("#graph-container").text("No songs found");
            $("#graph-container").css("text-align", "center");
            return;
        }

        if(responseData.UnknownUser)
        {
            $("#graph-container").text("Unknown User");
            $("#graph-container").css("text-align", "center");
            return;
        }

        let dateArr = responseData.dates          

        //drawHorizontalBarGraph(dateArr);
        drawVerticalLineGraph(dateArr);
        
    });	
    
});

function drawVerticalLineGraph(dateArr)
{
    let dayWidth = 500;
    let graphHeight = $("#graph-container").height();

    drawEmotionLine(dateArr, dayWidth, graphHeight, "happy", "yellow");
    drawEmotionLine(dateArr, dayWidth, graphHeight, "angry", "orange");
    drawEmotionLine(dateArr, dayWidth, graphHeight, "excited", "red");
    drawEmotionLine(dateArr, dayWidth, graphHeight, "sad", "blue");
    drawEmotionLine(dateArr, dayWidth, graphHeight, "relaxed", "green");

    addMaxEmotionLabel(dateArr, dayWidth, graphHeight, graphHeight * 0.1);
    addVerticalDateLabels(dateArr, dayWidth, graphHeight);

    //set graph container width
    d3.select("#graph")
    .attr("width", dateArr.length * dayWidth);
}

function addVerticalDateLabels(dateArr, dayWidth, graphHeight)
{
    //<i class="em-svg em-grin"></i>
    d3.select("#graph")
    .selectAll()
    .data(dateArr)
    .enter()
    .append("text")
    .attr("x", (d, i) => {
        return i * dayWidth;
    })
    .attr("y", graphHeight * 0.9)
    .text((d, i) => d.date);
}

function addMaxEmotionLabel(dateArr, dayWidth, graphHeight, displaySize)
{
    d3.select("#graph")
    .selectAll()
    .data(dateArr)
    .enter()
    .append("a")
    .attr("href", (d) => {

        let happy = Math.floor(d.happy / d.songsPlayed);
        let angry = Math.floor(d.angry / d.songsPlayed);
        let excited = Math.floor(d.excited / d.songsPlayed);
        let sad = Math.floor(d.sad / d.songsPlayed);
        let relaxed = Math.floor(d.relaxed / d.songsPlayed);

        return "/search?happy="+happy
        +"&angry="+angry
        +"&excited="+excited
        +"&sad="+sad
        +"&relaxed="+relaxed;
    })
    .append("svg:foreignObject")
    .attr("x", (d, i) => {
        return (i * dayWidth);
    })
    .attr("y", (d) => {
        let vals = [d.happy, d.angry, d.excited, d.sad, d.relaxed]
        let max = Math.max(...vals);

        let yMulti = (max / d.songsPlayed) / 100;
        let y = (1 - yMulti) * graphHeight;
        
        if(y > graphHeight * 0.9) y -= graphHeight * 0.05;
        else if(y < graphHeight * 0.1) y += graphHeight * 0.05;

        return y - (displaySize / 2);
    })
    .style("font-size", displaySize / 2)
    .style("width", displaySize)
    .style("height", displaySize)
    .html((d) => {
        let vals = [d.happy, d.angry, d.excited, d.sad, d.relaxed]
        let max = Math.max(...vals);

        if(d.happy == max) return '<i class="em-svg em-grin"></i>';
        
        else if(d.angry == max) return '<i class="em-svg em-angry"></i>';

        else if(d.excited == max) return '<i class="em-svg em-heart_eyes"></i>';
        
        else if(d.sad == max) return '<i class="em-svg em-cry"></i>';

        else if(d.relaxed == max) return '<i class="em-svg em-sleeping"></i>';   
    });
    
}

function drawEmotionLine(dateArr, dayWidth, graphHeight, emotion, strokeColor)
{
    let lineGenerator = d3.line();
    let points = [];

    for(let i = 0;i<dateArr.length;i++)
    {
        let d = dateArr[i];
        let x = i * dayWidth;
        let yMulti = (d[emotion] / d.songsPlayed) / 100;
        let y = ((1 - yMulti) * graphHeight);

        if(y > graphHeight * 0.9) y -= graphHeight * 0.05;
        else if(y < graphHeight * 0.1) y += graphHeight * 0.05;

        points.push([x, y]);
    }
    
    let pathData = lineGenerator(points);

    d3.select('#graph')
    .append("path")
    .attr('d', pathData)
    .attr("class", "line")
    .attr("stroke", strokeColor);
}

function drawHorizontalBarGraph(dateArr)
{
    let dayHeight = 120;
    let emotionHeight = 20;

    drawEmotionBar(dateArr, dayHeight, emotionHeight, 0, "happy", "yellow");
    drawEmotionBar(dateArr, dayHeight, emotionHeight, 1 * emotionHeight, "angry", "orange");
    drawEmotionBar(dateArr, dayHeight, emotionHeight, 2 * emotionHeight, "excited", "red");
    drawEmotionBar(dateArr, dayHeight, emotionHeight, 3 * emotionHeight, "sad", "blue");
    drawEmotionBar(dateArr, dayHeight, emotionHeight, 4 * emotionHeight, "relaxed", "green");

    addHorizontalDateLabels(dateArr, dayHeight, dayHeight / 2);

    //set graph container height
    d3.select("#graph")
    .attr("height", dateArr.length * dayHeight);
}

function addHorizontalDateLabels(dateArr, dayHeight, heightOffset)
{
    d3.select("#graph")
    .selectAll()
    .data(dateArr)
    .enter()
    .append("text")
    .attr("x", "90%")
    .attr("y", (d, i) => {
        return (i * dayHeight) + heightOffset;
    })
    .text((d) => d.date);
}

function drawEmotionBar(dateArr, dayHeight, emotionHeight, heightOffset, emotion, fillColor)
{
    d3.select("#graph")
    .selectAll()
    .data(dateArr)
    .enter()
    .append("rect")
    .attr("width", (d) => {
        return (d[emotion] / d.songsPlayed) + "%"; 
    })
    .attr("height", emotionHeight)
    .attr("x", 0)
    .attr("y", (d, i) => {
        return (i * dayHeight) + heightOffset;
    })
    .attr("class", "emotion")
    .style("fill", fillColor)
    .append("title")
    .text((d) => emotion +" "+(d[emotion] / d.songsPlayed) + "%");
}