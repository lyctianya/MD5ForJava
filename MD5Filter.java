import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;

class fileInfo {
	public String fullName;//
	public String subName;//
	public String preName;//

	public String str_md5;//

	public int fsize;

	public boolean setInfo(String fname, String pName) {
		if (fname == null || pName == null) {
			return false;
		}

		File fl = new File(fname);
		if (fl == null) {
			return false;
		} else {
			this.fullName = fname.replace("\\", "/");
			this.preName = pName.replace("\\", "/");
			this.subName = fullName.substring(pName.length()+1);

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

	public String toString() {

		return String.format("{[\"name\"]=\"%s\",[\"md5\"]=\"%s\",[\"size\"]=%d}", this.subName, this.str_md5,
				this.fsize);
	}

}

public class MD5Filter {

	// String v = MD5Filter.getMd5ByFile(new
	// File("D:/GitFootBall/xinshou2.lua"));
	// System.out.println(v.toUpperCase());

	public static String getMd5ByFile(File file) {
		FileInputStream in = null;
		String value = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	public static void getAllFiles(File dir) {
		System.out.println(dir.getName());
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				getAllFiles(files[i]);
			} else {
				System.out.println(files[i]);
			}
		}
	}

	public static ArrayList<String> getFilesList(File dir) {
		ArrayList<String> result = new ArrayList<String>();
		if (!dir.isDirectory()) {
			return result;
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				ArrayList<String> ls = getFilesList(files[i]);
				for (String str : ls) {
					result.add(str);
				}
			} else {
				result.add(files[i].toString());
			}
		}

		return result;
	}

	static void writeLine(FileWriter fw, String str, int num) {
		try {
			for (int i = 0; i < num; i++) {
				fw.write("\t");
			}
			fw.write(str);
			fw.write(",\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	static void writeLine(FileWriter fw, String str, int num,boolean noflag) {
		try {
			for (int i = 0; i < num; i++) {
				fw.write("\t");
			}
			fw.write(str);
			if(noflag)
			{
				fw.write("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void createFilesList(String verson, String dirPath) {

		File dir = new File(dirPath);
		ArrayList<String> ls = getFilesList(dir);
		ArrayList<String> mls = new ArrayList<String>();

		for (String str : ls) {
			fileInfo tmp = new fileInfo();
			tmp.setInfo(str, dirPath);
			mls.add(tmp.toString());
		}


		FileWriter writer = null;
		try {
			writer = new FileWriter("filelist.lua");
			writer.write("local info={\n");
			writeLine(writer,"[\"verson\"]=\""+verson+"\"",1);
			writeLine(writer,"[\"files\"]={",1,true);
			
			for (String str : mls) {
				writeLine(writer,str,2);
			}
			
			writeLine(writer,"}",1);
			writer.write("}\n");
			writer.write("return info\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String verson="1.0.0.0";
		String dirPath=null;
		while(true)
		{
			System.out.println("请输入要处理的文件夹：");
			Scanner sc = new Scanner(System.in);
			dirPath = sc.nextLine();
			File f = new File(dirPath);
			if(f.isDirectory())
			{
				break;
			}
			else
			{
				System.out.println("输入文件夹错误，请重新输入！");
			}
		}
		
		while(true)
		{
			System.out.println("请输入版本号：格式为 x.x.x.x");
			Scanner sc = new Scanner(System.in);
			verson = sc.nextLine();
			if(verson.matches("[0-9]+.[0-9]+.[0-9]+.[0-9]+"))
			{
				break;
			}
			else
			{
				System.out.println("输入版本号错误，请重新输入！");
			}
			
		}
		
		
		

		createFilesList(verson, dirPath);
	}
}