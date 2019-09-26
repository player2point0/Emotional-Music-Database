$(".search-result, .search-result img").mouseenter(
  function(){
    $(this).animate({color: "green",
    backgroundColor: "rgb( 20, 20, 20 )"},'slow');
  });

$(".search-result, .search-result img").mouseleave(
  function() {
    $(this).css("color", "red");
  });
