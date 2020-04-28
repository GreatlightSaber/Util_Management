package co.kr.util.file;

import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel; 
import com.jcraft.jsch.ChannelSftp; 
import com.jcraft.jsch.JSch; 
import com.jcraft.jsch.JSchException; 
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException; 


/**
 * ##################################################################
 * 오류가 발생하는 DTO 및 다른 객체는 custom 하여 재사용 가능하게 사용성 향상 필요
 * ##################################################################
 * */

public class SFTPUploader {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass()); 
	private Session session = null; 
	private Channel channel = null; 
	private ChannelSftp channelSftp = null;
	private int auth = Integer.parseInt("770", 8); 
	
	// SFTP 서버연결 
	public void init(String _host, String _user, String _pwd){
		String url = _host;
		String user = _user;
		String password = _pwd;
		
		System.out.println(url); 
		//JSch 객체 생성
        JSch jsch = new JSch();
        try {
        	//세션객체 생성 ( user , host, port ) 	
            session = jsch.getSession(user, url);
            
            //password 설정
            session.setPassword(password);
            
            //세션관련 설정정보 설정
            java.util.Properties config = new java.util.Properties();
            
            //호스트 정보 검사하지 않는다.
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            
            //접속
            session.connect();

            //sftp 채널 접속
            channel = session.openChannel("sftp");
            channel.connect();
           
        } catch (JSchException e) {
            e.printStackTrace();
        }
        channelSftp = (ChannelSftp) channel;
        
	}

	// 파일 업로드
	public void upload( String dir , List<String> imgs, List<String> pdfs){ 
		FileInputStream in = null;
		File file = null;
		
		try{ 
			this.mkdirDir(dir);
			
			for (String imgPath : imgs) {
				file = new File(imgPath);
				in = new FileInputStream(file);
				channelSftp.cd(dir);
				channelSftp.put(in,file.getName());
			}
			
			for (String pdf : pdfs) {
				file = new File(pdf);
				in = new FileInputStream(file);
				channelSftp.cd(dir);
				channelSftp.put(in,file.getName());
			}
		}catch(SftpException se){
			se.printStackTrace();
		}catch(FileNotFoundException fe){
			fe.printStackTrace();
		}finally{
			try{
				in.close();
			} catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}
	
	// 파일 업로드
	public IFImgDto upload( String uploadFtpFolder, String writeFolder, BaseService baseService, ContractDto contract, String interface_target, InterfaceService interfaceService, ProfileCls profileCls){
		FileInputStream in = null;
		File file = null;
		IFImgDto iFImgDto = null;
		
		try{ 
			String pageNm = "";
			// 페이지 별 PDF, JPG 생성
			for (IFContractItem iFContractItem : contract.getDATA()) {
				this.mkdirDir(uploadFtpFolder); // 디렉토리 생성
				pageNm = new HashHelper().testMD5(iFContractItem.getCUST_NM());
				file = new File(writeFolder+pageNm+".jpg");
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+pageNm+".jpg");
				System.out.println("Upload Success");
				
				file = new File(writeFolder+pageNm+".pdf");
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+pageNm+".pdf");
				System.out.println("Upload Success");
				
				// 전자서명 완료 통보(local 아닐 경우만)
				iFImgDto = interfaceService.signPrivateComplate(contract, uploadFtpFolder, interface_target, baseService, iFContractItem);
				// 예외 발생 && 통신 에러
				if(iFImgDto == null) return null;
				if(!iFImgDto.getStatus().equals("SUCCESS")) return iFImgDto;
			}
			
			this.mkdirDir(uploadFtpFolder); // 디렉토리 생성
			
			file = new File(writeFolder+"1.jpg");
			if(file.exists()) {
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+"1.jpg");
				System.out.println("Upload Success");
				
				file = new File(writeFolder+"1.pdf");
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+"1.pdf");
				System.out.println("Upload Success");
			}
			
			
			file = new File(writeFolder+"2.jpg");
			if(file.exists()) {
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+"2.jpg");
				System.out.println("Upload Success");
				
				file = new File(writeFolder+"2.pdf");
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+"2.pdf");
				System.out.println("Upload Success");
			}
			
			
			file = new File(writeFolder+"3.jpg");
			if(file.exists()) {
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+"3.jpg");
				System.out.println("Upload Success");
				
				file = new File(writeFolder+"3.pdf");
				in = new FileInputStream(file);
				channelSftp.cd(uploadFtpFolder);
				channelSftp.put(in,file.getName());
				channelSftp.chmod(auth, uploadFtpFolder+"3.pdf");
				System.out.println("Upload Success");
			}
			
			// 전자서명 완료 통보(local 아닐 경우만)
			iFImgDto = interfaceService.signContractComplate(contract, uploadFtpFolder, interface_target, baseService, pageNm, writeFolder);
			// 예외 발생 && 통신 에러
			if(iFImgDto == null) return null;
			if(!iFImgDto.getStatus().equals("SUCCESS")) return iFImgDto;
				
			iFImgDto = new IFImgDto();
			iFImgDto.setStatus("SUCCESS");
			return iFImgDto;
		}catch(SftpException se){
			iFImgDto = new IFImgDto();
			iFImgDto.setStatus("FAIL");
			iFImgDto.setMessage(se.toString());
			return iFImgDto;
		}catch(FileNotFoundException fe){
			iFImgDto = new IFImgDto();
			iFImgDto.setStatus("FAIL");
			iFImgDto.setMessage(fe.toString());
			return iFImgDto;
		}finally{
			try{
				in.close();
			} catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}
	
	private String mkdirDir(String path) throws SftpException {
		SftpATTRS attrs=null;

		try {
		    attrs = channelSftp.stat(path);
		} catch (Exception e) {
		    System.out.println(path+" not found");
		}

		if (attrs != null) {
			// 폴더 삭제(파일 모두 삭제후 폴더 삭제) START
		    System.out.println("Directory exists IsDir So Remove START");
		    Collection<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);
		    for (ChannelSftp.LsEntry item : fileAndFolderList) {
		        if (!item.getAttrs().isDir()) {
		            channelSftp.rm(path + "/" + item.getFilename()); // Remove file.
		        }
		    }
		    channelSftp.rmdir(path); // delete the parent directory after empty
		    System.out.println("Directory exists IsDir So Remove END");
		    // 폴더 삭제(파일 모두 삭제후 폴더 삭제) END
		    
		    channelSftp.mkdir(path);
		    channelSftp.chmod(auth, path);
		} else {
		    System.out.println("Creating dir "+path);
		    channelSftp.mkdir(path);
		    channelSftp.chmod(auth, path); 
		}

		channelSftp.cd(path);
    	return path;
	}
	
	// 단일 파일 다운로드 
	public InputStream download(String dir, String fileNm){
		InputStream in = null;
		String path = "...";
		try{ //경로탐색후 inputStream에 데이터를 넣음
			channelSftp.cd(path+dir);
			in = channelSftp.get(fileNm);
			
		}catch(SftpException se){
			se.printStackTrace();
		}
		
		return in;
	}

	// 파일서버와 세션 종료
	public void disconnect(){
		channelSftp.quit();
		session.disconnect();
	}
}
