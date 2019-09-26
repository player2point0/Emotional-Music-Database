package views;

import java.io.PrintWriter;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;

public abstract class DynamicWebPage
{
	public DatabaseInterface db;
	public FileStoreInterface fs;
	
	public DynamicWebPage(DatabaseInterface db,FileStoreInterface fs)
	{
		this.db = db;
		this.fs = fs;
	}
	
	public abstract boolean process(WebRequest toProcess);
	
	public void appendBootstrapHeader(PrintWriter pw, String title, String keywords, String description)
	{
		pw.append("<head>");
		pw.append("<meta charset=\"utf-8\">");
		pw.append("<title>"+title+"</title>");
		pw.append("<meta  name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		if(keywords!=null)
			pw.append("<meta  name=\"keywords\" content=\""+keywords+"\">");
		if(description!=null)
			pw.append("<meta  name=\"description\" content=\""+description+"\">");
		pw.append("<link href=\"/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">");
		pw.append("<link href=\"/css/font-awesome.min.css\" rel=\"stylesheet\">");
		pw.append("<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,300,600,700\" rel=\"stylesheet\">");
		pw.append("<link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic\" rel=\"stylesheet\">");
		pw.append("<link href=\"/css/blocks.css\" rel=\"stylesheet\">");
		pw.append("<!--[if lt IE 9]>");
		pw.append("<script src=\"/js/html5shiv.js\"></script>");
		pw.append("<script src=\"/js/respond.min.js\"></script>");
		pw.append("<![endif]-->");
		pw.append("</head>");
	}
	
	public void appendBootrstrapScripts(PrintWriter pw)
	{
		pw.append("<script type=\"text/javascript\" src=\"/js/jquery-1.11.1.min.js\"></script>");
		pw.append("<script type=\"text/javascript\" src=\"/js/bootstrap.min.js\"></script>");
	}
	
}
