import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;

class fileInfo{
	public String fullName;//全路径
	public String subName;//缩略路径
	public String preName;//前路径
	
	public String str_md5;//md5值
	
	public int fsize;
	
	public  boolean setInfo(String fname,String pName)
	{
		if(fname==null||pName==null)
		{
			return false;
		}
		
		File fl = new File(fname);
		if(fl==null)
		{
			return false;
		}
		else
		{
			this.fullName=fname;
			this.preName = pName;
			this.subName = fullName.substring(pName.length());
			
			FileInputStream fis;
			try {
				fis = new FileInputStream(fl);
				try {
					this.fsize = fis.available();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.str_md5 = MD5Filter.getMd5ByFile(fl);

		}
		
		
		return true;
	}
	
	
	public  String toString()
	{
		
		return String.format("{%s,%s,%d}",this.subName,this.str_md5,this.fsize);
	}
	
}



public class MD5Filter {
	
//	String v = MD5Filter.getMd5ByFile(new File("D:/GitFootBall/xinshou2.lua"));
//    System.out.println(v.toUpperCase());
     
    public static String getMd5ByFile(File file) {
    	FileInputStream in=null;
        String value = null;
	    try {
	        in = new FileInputStream(file);
	        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        md5.update(byteBuffer);
	        BigInteger bi = new BigInteger(1, md5.digest());
	        value = bi.toString(16);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	            if(null != in) {
	                try {
	                in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return value;
    }
    
 
    public static void getAllFiles(File dir)  
    {  
        System.out.println(dir.getName());   
        File[] files=dir.listFiles();  
        for(int i=0;i<files.length;i++)  
        {  
            if(files[i].isDirectory())  
            {  
                //这里面用了递归的算法  
                getAllFiles(files[i]);  
            }  
            else {  
                System.out.println(files[i]);  
            }     
        }          
    }  
    
    public static ArrayList<String> getFilesList(File dir)
    {
    	ArrayList<String> result = new ArrayList<String>();
    	if(!dir.isDirectory())
    	{
    		return result;
    	}
    	
    	File[] files = dir.listFiles();
    	for(int i=0;i<files.length;i++)
    	{
    		if(files[i].isDirectory())
    		{
    			ArrayList<String> ls= getFilesList(files[i]);
    			for (String str : ls) {
					result.add(str);
				}
    		}
    		else
    		{
    			result.add(files[i].toString());
    		}
    	}
    	
    	
    	return result;
    }
    
    
    public static void main(String[] args) {
        File dir=new File("D:/app");
        ArrayList<String> ls = getFilesList(dir);
        System.out.println("the sum number is " + ls.size());
        
        ArrayList<String> mls=new ArrayList<String>();
        
        for (String str : ls) {
        	fileInfo tmp = new fileInfo();
        	tmp.setInfo(str, "D:app/");
			mls.add(tmp.toString());
		} 
        
        
        for (String string : mls) {
			System.out.println(string);
		}
        
        
    }
}