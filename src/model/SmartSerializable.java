package model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;

public class SmartSerializable implements Externalizable
{
	public void readExternal(ObjectInput in) 
	        throws IOException, ClassNotFoundException 
    {
		while(true)
		{
			Object field = in.readObject();
			if(!(field instanceof String))
				return;
			String fieldname = (String)field;
			if(fieldname.equals("}"))
				break;
			Class c = getClass();
			try
			{
				Field f = c.getField(fieldname);
				f.set(this, in.readObject());
			}
			catch(Exception e)
			{
				
			}
		}
    }
    public void writeExternal(ObjectOutput out) 
        throws IOException 
    {
		try
		{
	       	//write the field names then the objects
	    	Field[] fs = getClass().getFields();
	    	for(int i=0;i<fs.length;i++)
	    	{
	    		out.writeObject(fs[i].getName());
	    		out.writeObject(fs[i].get(this));
	    	}
			out.writeObject("}");
		}
		catch(Exception e)
		{
			
		}
    }
}
