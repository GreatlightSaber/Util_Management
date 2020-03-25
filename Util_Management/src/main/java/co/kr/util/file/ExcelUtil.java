package co.kr.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
public class ExcelUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
		
public Map<String, Object> uploadExcelDb(MultipartFile multipartFile) throws IOException {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		InputStream fileIn = multipartFile.getInputStream();
		String originalFileName, xtnsVal;
		int chkCnt = 0, rows = 0;
		
        try
        {
        	originalFileName = multipartFile.getOriginalFilename();
			xtnsVal = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
			// 확장자 명 확인
			if (xtnsVal.equals("xls") || xtnsVal.equals("xlsx")) {
				
				
				
				// 엑셀 파일 경로에 접근(업로드시 임시 경로)
				//FileInputStream file = new FileInputStream(new File(tempPath));
				// 해당 엑셀 파일의 워크북 가져오기
				HSSFWorkbook workbook1 = null;
				XSSFWorkbook workbook2 = null;
				if (xtnsVal.equals("xls")) {
					workbook1 = new HSSFWorkbook(fileIn);
				} else {
					workbook2 = new XSSFWorkbook(fileIn);
				}
				// 해당 엑셀 파일의 시트 가져오기 (uploaddata 시트)
				HSSFSheet sheet1 = null;
				XSSFSheet sheet2 = null;
				
				// 시트 가져오기
				if (xtnsVal.equals("xls")) {
					// 원하는 시트명 입력
					sheet1 = workbook1.getSheet("Sheet1");
					rows = sheet1.getPhysicalNumberOfRows();
				} else {
					sheet2 = workbook2.getSheet("Sheet1");
					rows = sheet2.getPhysicalNumberOfRows();
				}
				// rows = rows + 1;
				HSSFRow row1 = null;
				XSSFRow row2 = null;
				XSSFCell cell2 = null;
				HSSFCell cell1 = null;
				// row - 해당 열
				// cell - row열에 해당되는  n번째 행
				for (int j = 1; j < rows + 1; j++) {
					
					String cuid = "";
					String cuidNm = "";
					
					if (xtnsVal.equals("xls")) {
						row1 = sheet1.getRow(j);
						// 해당 열에 값 없을 시 break
						if(row1 == null) {
							break;
						}
					} else {
						row2 = sheet2.getRow(j);
						// 해당 열에 값 없을 시 break
						if(row2 == null) {
							break;
						}
					}
					
					// 첫번 째 행
					if (xtnsVal.equals("xls")) {
						cell1 = row1.getCell(0);
						if(null != cell1) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							cuid = cell1.getStringCellValue();
							System.out.println("cell1:::" + cell1.getStringCellValue());
						}
					} else {
						cell2 = row2.getCell(0);
						if(null != cell2) {
							cell2.setCellType(Cell.CELL_TYPE_STRING);
							cuid = cell2.getStringCellValue();
							System.out.println("cell2:::" + cell2.getStringCellValue());
						}
					}
				}
			}
        }catch(Exception e) {
        	logger.debug(e.getMessage());
        }finally
        {
            if (fileIn != null)
            {
                fileIn.close();
            }
        }
        return resultMap;
	}
}
