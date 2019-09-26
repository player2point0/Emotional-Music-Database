package views;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class MerchView extends DynamicWebPage{

	public MerchView(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);
		
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.equalsIgnoreCase("Merch")) {

        	String stringToSendToWebBrowser = "<!DOCTYPE html>\n" + 
        			"<html>\n" + 
        			"    <head>\n" + 
        			"        <title>Merchandise | Store</title>\n" + 
        			"        <script src=\"js/merch.js\"></script>\n" + 
        			"    \n" + 
        			" <meta charset=\"utf-8\">\n" + 
        			"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + 
        			"  <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" type=\"text/css\">\n" + 
        			"  <link rel=\"stylesheet\" href=\"css/theme.css\" type=\"text/css\">\n" + 
        			"    \n" + 
        			"    </head>\n" + 
        			"    <body>\n" + 
        			"    <nav class=\"navbar navbar-expand-md navbar-dark bg-dark\">\n" + 
        			"    <div class=\"container\"> <button class=\"navbar-toggler navbar-toggler-right border-0\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbar12\">\n" + 
        			"        <span class=\"navbar-toggler-icon\"></span>\n" + 
        			"      </button>\n" + 
        			"      <div class=\"collapse navbar-collapse\" id=\"navbar12\"> <a class=\"navbar-brand d-none d-md-block\" href=\"#\">\n" + 
        			"          <i class=\"fa d-inline fa-lg fa-circle\"></i>\n" + 
        			"          <b> Emotional Music Website</b>\n" + 
        			"        </a>\n" + 
        			"        <ul class=\"navbar-nav mx-auto\">\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"./Login.html\">Login/Sign Up</a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"merch.html\">Merch</a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"Discover.html\">Discover</a> </li>\n" + 
        			"            <li class=\"nav-item\"> <a class=\"nav-link\" href=\"Search.html\">Search</a> </li>\n" + 
        			"        </ul>\n" + 
        			"      </div>\n" + 
        			"    </div>\n" + 
        			"  </nav>\n" + 
        			"  <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\" style=\"\"></script>\n" + 
        			"  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\" style=\"\"></script>\n" + 
        			"  <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js\" integrity=\"sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k\" crossorigin=\"anonymous\" style=\"\"></script>\n" + 
        			"        \n" + 
        			"        <header class=\"main-header\">\n" + 
        			"          \n" + 
        			"            <h1>Merchandise</h1>\n" + 
        			"        </header>\n" + 
        			"        \n" + 
        			"        <section class=\"container content-section\">\n" + 
        			"            <h2 class=\"section-header\">T-Shirts</h2>\n" + 
        			"            <div class=\"shop-items\">\n" + 
        			"                <div class=\"shop-item\">\n" + 
        			"                    <img class=\"shop-item-image\" src=\"images/tshirt.jpg\" width=\"500\" height=\"350\">\n" +
        			"                    <div class=\"shop-item-details\">\n" + 
        			"                         <span class=\"shop-item-title\">Nirvana T-Shirt</span>\n" + 
        			"                        <span class=\"shop-item-price\">£7.99</span>\n" + 
        			"                        <button class=\"btn btn-primary shop-item-button\" type=\"button\">ADD TO CART</button>\n" + 
        			"                    </div>\n" + 
        			"                </div>\n" + 
        			"                <div class=\"shop-item\">\n" + 
        			"                    <img class=\"shop-item-image\" src=\"images/acdc.jpg\" width=\"350\" height=\"300\">\n" +
        			"                    <div class=\"shop-item-details\">\n" + 
        			"                        <span class=\"shop-item-title\">ACDC T-Shirt</span>\n" + 
        			"                        <span class=\"shop-item-price\">£7.99</span>\n" + 
        			"                        <button class=\"btn btn-primary shop-item-button\"type=\"button\">ADD TO CART</button>\n" + 
        			"                    </div>\n" + 
        			"                </div>\n" + 
        			"                <div class=\"shop-item\">\n" + 
        			"                    <img class=\"shop-item-image\" src=\"images/kiss.jpg\" width=\"350\" height=\"300\">\n" +
        			"                    <div class=\"shop-item-details\">\n" + 
        			"                         <span class=\"shop-item-title\">Kiss T-Shirt</span>\n" + 
        			"                        <span class=\"shop-item-price\">£7.99</span>\n" + 
        			"                        <button class=\"btn btn-primary shop-item-button\" type=\"button\">ADD TO CART</button>\n" + 
        			"                    </div>\n" + 
        			"                </div>\n" + 
        			"            </div>\n" + 
        			"                <div class=\"shop-item\">\n" + 
        			"                    <img class=\"shop-item-image\" src=\"images/queen.jpg\" width=\"300\" height=\"300\">\n" +
        			"                    <div class=\"shop-item-details\">\n" + 
        			"                         <span class=\"shop-item-title\">Queen T-Shirt</span>\n" + 
        			"                        <span class=\"shop-item-price\">£7.99</span>\n" + 
        			"                        <button class=\"btn btn-primary shop-item-button\" type=\"button\">ADD TO CART</button>\n" + 
        			"                    </div>\n" + 
        			"                </div>\n" + 
        			"            </div>\n" + 
        			"        </section>\n" + 
        			"        <section class=\"container content-section\">\n" + 
        			"            <h2 class=\"section-header\">Mugs</h2>\n" + 
        			"            <div class=\"shop-items\">\n" + 
        			"                <div class=\"shop-item\">\n" + 
        			"                    <img class=\"shop-item-image\" src=\"images/mug2.jpg\">\n" +
        			"                    <div class=\"shop-item-details\">\n" + 
        			"                        <span class=\"shop-item-title\">Beatles Mug</span>\n" + 
        			"                        <span class=\"shop-item-price\">£3.99</span>\n" + 
        			"                        <button class=\"btn btn-primary shop-item-button\" type=\"button\">ADD TO CART</button>\n" + 
        			"                    </div>\n" + 
        			"                </div>\n" + 
        			"                <div class=\"shop-item\">\n" + 
        			"                    <img class=\"shop-item-image\" src=\"images/mug1.jpg\">\n" +
        			"                    <div class=\"shop-item-details\">\n" + 
        			"                        <span class=\"shop-item-title\">Nirvana Mug</span>\n" + 
        			"                        <span class=\"shop-item-price\">£3.99</span>\n" + 
        			"                        <button class=\"btn btn-primary shop-item-button\" type=\"button\">ADD TO CART</button>\n" + 
        			"                    </div>\n" + 
        			"                </div>\n" + 
        			"            </div>\n" + 
        			"        </section>\n" + 
        			"        <section class=\"container content-section\">\n" + 
        			"            <h2 class=\"section-header\">CART</h2>\n" + 
        			"            <div class=\"cart-row\">\n" + 
        			"                <span class=\"cart-item cart-header cart-column\">ITEM</span>\n" + 
        			"                <span class=\"cart-price cart-header cart-column\">PRICE</span>\n" + 
        			"                <span class=\"cart-quantity cart-header cart-column\">QUANTITY</span>\n" + 
        			"            </div>\n" + 
        			"            <div class=\"cart-items\">\n" + 
        			"            </div>\n" + 
        			"            <div class=\"cart-total\">\n" + 
        			"                <strong class=\"cart-total-title\">Total</strong>\n" + 
        			"                <span class=\"cart-total-price\">£0</span>\n" + 
        			"            </div>\n" + 
        			"            <button class=\"btn btn-primary btn-purchase\" type=\"button\">PURCHASE</button>\n" + 
        			"        </section>\n" + 
        			"        <footer class=\"main-footer\">\n" + 
        			"            <div class=\"container main-footer-container\">\n" + 
        			"                \n" + 
        			"            </div>\n" + 
        			"        </footer>\n" + 
        			"    </body>\n" + 
        			"\n" + 
        			"</html>";
	
	toProcess.r = new WebResponse( WebResponse.HTTP_OK,WebResponse.MIME_HTML, stringToSendToWebBrowser);
	return true;

        }
        return false;
}
}
