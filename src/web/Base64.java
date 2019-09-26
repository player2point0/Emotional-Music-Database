package web;

import java.util.Formatter;

public class Base64 
{
	// private property
	static final String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 
	public static String encode(String input)
	{
		String output = "";
		int chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		int i = 0;

		chr1 = chr2 = chr3 = 0;
		input = _utf8_encode(input);
 
		boolean nochr2 = false;
		boolean nochr3 = false;
		while (i < input.length()) {
 
			chr1 = input.charAt(i++);
			if(i<input.length())
				chr2 = input.charAt(i++);
			else
				nochr2 = true;
			if(i<input.length())
				chr3 = input.charAt(i++);
			else
				nochr3 = true;
 
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
 
			if (nochr2) {
				output = output +
				_keyStr.charAt(enc1) + _keyStr.charAt(enc2);
			} else if (nochr3) {
				output = output +
				_keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
				_keyStr.charAt(enc3);
			} else {
				output = output +
				_keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
				_keyStr.charAt(enc3) + _keyStr.charAt(enc4);
			}
 
 
		}
 
		return output;
	}

	public static String encode(byte[] input)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : input)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	public static String decode(String input)
	{
		if(input==null)
		{
			return null;
		}
		String output = "";
		int chr1, chr2, chr3;
		int enc1, enc2, enc3, enc4;
		char venc3 = 0;
		char venc4 = 0;
		int i = 0;
 
		boolean nochr2 = false;
		boolean nochr3 = false;

		while (i < input.length()) {
 
			enc1 = _keyStr.indexOf(input.charAt(i++));
			enc2 = _keyStr.indexOf(input.charAt(i++));
			if(i<input.length())
				venc3 = input.charAt(i++);
			else
				nochr2 = true;
			if(i<input.length())			
				venc4 = input.charAt(i++);
			else
				nochr3 = true;
			
			if (!nochr2) {
				enc3 = _keyStr.indexOf(venc3);
			} else {
				enc3 = 64;
			}
			if (!nochr3) {
				enc4 = _keyStr.indexOf(venc4);
			} else {
				enc4 = 64;
			}
 
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
 
			output = output + (char)(chr1);
 
			if (enc3 != 64) {
				output = output + (char)(chr2);
			}
			if (enc4 != 64) {
				output = output + (char)(chr3);
			}
 
		}
 
		output = _utf8_decode(output);
 
		return output;
	}

	private static String _utf8_encode(String input)
	{
		input = input.replace("\r\n", "\n");
		String utftext = "";
		
		for (int n = 0; n < input.length(); n++) {
			 
			char c = input.charAt(n);
 
			if (c < 128) {
				utftext += (char)(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += (char)((c >> 6) | 192);
				utftext += (char)((c & 63) | 128);
			}
			else {
				utftext += (char)((c >> 12) | 224);
				utftext += (char)(((c >> 6) & 63) | 128);
				utftext += (char)((c & 63) | 128);
			}
		}
 
		return utftext;
	}

	private static String _utf8_decode(String utftext)
	{
		String string = "";
		int i = 0;
		int c = 0;
		int c2 = 0;
		int c3 = 0;
 
		while ( i < utftext.length() ) {
 
			c = utftext.charAt(i);
 
			if (c < 128) {
				string += (char)(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charAt(i+1);
				string += (char)(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charAt(i+1);
				c3 = utftext.charAt(i+2);
				string += (char)(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
 
		return string;		
	}
}
