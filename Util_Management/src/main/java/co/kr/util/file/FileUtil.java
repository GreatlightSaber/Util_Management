package co.kr.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {
	
	/** Buffer size */
	public static final int BUFFER_SIZE = 8192;

	public static final String SEPERATOR = File.separator;
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public void uploadFilesOriginal(String fileGroupSeq, MultipartFile mFile, String where, long maxFileSize) 
	{
		String tmp = mFile.getOriginalFilename();
		if (tmp.lastIndexOf("\\") >= 0) {
			tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
		}
		
        where = makeNewFilePath(where);
		
		if (mFile.getSize() > 0) {
			InputStream is = null;

			try {
				is = mFile.getInputStream();
				
				String filepath =  where + File.separator + File.separator + makeSaveFileName(tmp);
				saveFile(is, new File(filepath.replaceAll("\\.\\.", "")));
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private long saveFile(InputStream is, File file) throws IOException {
		// 디렉토리 생성
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		OutputStream os = null;
		long size = 0L;

		try {
			os = new FileOutputStream(file);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFFER_SIZE];

			while ((bytesRead = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
				size += bytesRead;
				os.write(buffer, 0, bytesRead);
			}
		} finally {
//			EgovResourceCloseHelper.close(os);
			if(os != null) os.close();
		}

		return size;
	}
	
	// 파일 디렉터리 경로 생성
	private String makeNewFilePath(String where) {
		//YYYY/MM 형식의 디렉토리생성
        try {
        	SimpleDateFormat sdt = new SimpleDateFormat("YYYY/MM");
        	String namePrefix = sdt.format(Calendar.getInstance().getTime());
        	
        	if(where.endsWith("/") == false) where += "/";
        	
        	where  = where + namePrefix;
        	
        	File destinationDir = new File(where);
			FileUtils.forceMkdir(destinationDir);
		} catch (IOException e1) {
			
		}
		return where;
	}
	
	// 파일 명 생성
	private String makeSaveFileName(String tmp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		return format.format(new Date())+"_"+ UUID.randomUUID().toString().replaceAll("-", "").toLowerCase() + "_" + tmp;
	}
}
