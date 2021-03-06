package com.sgen.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sgen.DAO.UsersDAO;
import com.sgen.DTO.UsersDTO;
import com.sgen.gcm.GCMSender;
import com.sun.org.apache.xml.internal.utils.IntVector;

public class GCMUtil {
	// To send a message to android check userCode
	UsersDTO usersDTO;
	UsersDAO usersDAO;
	ArrayList<UsersDTO> userlist;
	String currentTime;

	public GCMUtil(String deviceCode, String userCode) {
		usersDAO = new UsersDAO();
		usersDTO = new UsersDTO();
		usersDTO.setDeviceCode(deviceCode);
		usersDTO.setUserCode(userCode);
		userlist = usersDAO.getUsers(usersDTO);
		findUserName(userCode);
	}
	public GCMUtil(String deviceCode){
		String adminUserCode = null;
		usersDAO = new UsersDAO();
		usersDTO = new UsersDTO();
		usersDTO.setDeviceCode(deviceCode);
		userlist = usersDAO.getUsers(usersDTO);
		usersDTO = usersDAO.getAdminUserCode(usersDTO);
		
	}

	public String getCurrentTime() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("MM-dd hh:mm");
		String str = dayTime.format(new Date(time));
		return str;
	}

	public void sendToAllGCM(String message) {
		System.out.println("=======GCM TEST=======");
		for (int i = 0; i < userlist.size(); i++) {
			System.out.println("userlist.get(i).getRegId(" + i + ") : "
					+ userlist.get(i).getRegId());
			System.out.println("userlist.get(i).getUserCode(" + i + ") : "
					+ userlist.get(i).getUserCode());
			if (userlist.get(i).getRegId() != "0") {
				GCMSender.sendMessage(userlist.get(i).getRegId(), message);
				System.out.println("GCM send to "
						+ userlist.get(i).getUserCode());
			}
		}
		System.out.println("=========================");
	}

	public void sendToAdminGCM(String message){
		GCMSender.sendMessage(usersDTO.getRegId(), message);
	}
	
	public ArrayList<UsersDTO> findUserName(String userCode) {
		for (int i = 0; i < userlist.size(); i++) {
			if (userlist.get(i).getUserCode().equals(userCode)) {
				usersDTO.setUserName(userlist.get(i).getUserName());
			}
		}
		if (usersDTO.getUserName() == null)
			usersDTO.setUserName(userCode);

		return userlist;
	}
	
	public String getUserName(){
		return usersDTO.getUserName();
	}
}