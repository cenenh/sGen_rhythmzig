package com.sgen.rp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgen.DAO.DeviceDAO;
import com.sgen.DAO.LogsDAO;
import com.sgen.DAO.UsersDAO;
import com.sgen.DTO.*;
import com.sgen.gcm.GCMSender;
import com.sgen.util.GCMUtil;
import com.sgen.util.ResultStatus;

public class EnterLog extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 기계에 입력하면 바로 보내준다....흠....
		/*
		 * userCode: String (실패 시 FAIL, OTP로 열렸을 때 OTP/OTP FAIL) deviceCode :
		 * String deviceStatus : int(방범모드, 여행모드,?) //device status는 device
		 * table에서 받아오긔 /enterLog
		 */
		int enterlogIdx = 0;
		String userCode = request.getParameter("userCode");
		String deviceCode = request.getParameter("deviceCode");
		String returnResult = null;
		int deviceStatus = 0; // 우선 0 으로 설정, 그리고 DB에서 값 가져오기
		int result = 0;
		boolean lock = false;
		boolean SET_OTP_ORIGIN = false;
		DeviceDTO deviceDTO = new DeviceDTO();
		DeviceDAO deviceDAO = new DeviceDAO();

		// To send a message to android check userCode
		String message = "";
		GCMUtil gcmUtil = new GCMUtil(deviceCode, userCode);

		deviceDTO.setDeviceCode(deviceCode);
		deviceDTO.setOtp(ResultStatus.OTP_ORIGIN);
		int currentDeviceStatus = deviceDAO.getDeviceStatus(deviceDTO);

		if (userCode.equals(ResultStatus.FAIL_5_LOCK)) 
		{
			// GCM TO APP
			lock = true;
			System.out.println("5 FAILS, LOCK WARNINIG");
			deviceDAO.SET_OTP_TO_ORIGIN(deviceDTO); // otp reset
			SET_OTP_ORIGIN = true;
		}
		
		LogsDTO logsDTO = new LogsDTO();
		LogsDAO logsDAO = new LogsDAO();
		logsDTO.setEnterlogIdx(enterlogIdx);
		logsDTO.setUserCode(userCode);
		logsDTO.setDeviceCode(deviceCode);
		logsDTO.setDeviceStatus(deviceStatus);
		// logsDTO.setEnterTime(enterTime);

		result = logsDAO.addLogs(logsDTO);
		
		//GCM 보낼때 예외처리
		if (userCode.equals(ResultStatus.FAIL)) {
			message = "출입이 실패하였습니다. " + "[ " + gcmUtil.getCurrentTime() + " ]";
		} else if (userCode.equals(ResultStatus.OTPO)) {
			message = "OTP 출입이 성공하였습니다. " + "[ " + gcmUtil.getCurrentTime() + " ]";
		} else if (userCode.equals(ResultStatus.OTPX)) {
			message = "OTP 출입이 실패하였습니다. " + "[ " + gcmUtil.getCurrentTime() + " ]";
		} else if(userCode.equals(ResultStatus.LOCK)){
			message =  "5번 출입 실패! LOCK 되었습니다. " + "[ " + gcmUtil.getCurrentTime() + " ]";
		} else {
			message = gcmUtil.getUserName() + "님이 출입하셨습니다. " + "[ "+ gcmUtil.getCurrentTime() + " ]";
		}
		gcmUtil.sendToAllGCM(message); //GCM 전송!
		
		//OTP접속 후에 OTP 번호 초기화
		if (logsDTO.getUserCode().equals(ResultStatus.OTPO)) {
			deviceDAO.SET_OTP_TO_ORIGIN(deviceDTO); // otp reset
			SET_OTP_ORIGIN = true;
			System.out.println("OTP IS SET TO ORIGIN");
		}
		
		//그냥 서버에 로그 찍기 & 라즈베리파이에 응답 보내주긔
		if (lock == false && result == 1 || lock == false && result > 0)
			returnResult = "ADD LOGS SUCCESS";
		else if (lock == true && result == 1 || lock == true && result > 0)
			returnResult = "[Locked Warning] ADD LOGS SUCCESS";
		else
			returnResult = "ADD LOGS FAIL";
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(returnResult);
	}


}
