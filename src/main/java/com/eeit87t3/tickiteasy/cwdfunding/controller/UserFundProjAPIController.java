package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundOrderService;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.util.JWTUtil;

@RestController
@RequestMapping("/member")
public class UserFundProjAPIController {

	@Autowired
	private FundProjService projService;

	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired 
	private FundOrderService fundOrderService;
	
	
	/* [API] 查詢募資活動(可以by categoryID或不By categoryID)*/
	@GetMapping("/api/fundproject")
	public Page<FundProjDTO> findByPageApi(@RequestParam(defaultValue = "1") Integer pageNumber,
			@RequestParam(required = false) Integer categoryID,Model model) {
		Integer pageSize = 9;
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber, pageSize, categoryID);
		return page;
	}

	/* [API] 查詢單筆募資活動by ID */
	@GetMapping("/api/fundproject/{projectID}")
	public FundProjDTO findByID(@PathVariable Integer projectID) {
		return projService.findFundProjDTOById(projectID);
	}
	
	/*
	 * 錯誤
	 */	
//	@PostMapping("/api/fundprojectOrder/")
//	public ResponseEntity<?> createOrder(
//	        @RequestHeader("Authorization") String authHeader,
//	        @RequestBody Map<String, Object> requestBody) {
//	    
//	    try {
//	        // 從 Authorization Header 中提取 Token
//	        String token = authHeader.replace("Bearer ", "");
//	        
//	        // 從 Token 中獲取電子郵件
//	        String email = jwtUtil.getEmailFromToken(token);
//	        
//	        // 根據電子郵件獲取會員資料
//	        Member member = memberService.findByEmail(email);
//	        
//	        if (member == null) {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
//	        }
//	        
//	        // 取得此member的ID
//	        Integer memberID = member.getMemberID();
//
//	        // 進行後續操作...
//	        // 呼叫service給tickitID, memberID，此
//	        fundOrderService.saveFundOrderTest(memberID, requestBody);
//	        
//	        return ResponseEntity.ok("操作成功");
//	    } catch (Exception e) {
//	        // 處理 Token 無效或過期的情況
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
//	    }
//	}
//	
	@ResponseBody
	@GetMapping("/api/fundproject/getMemberID")
	public ResponseEntity<?> getMemberID(
	        @RequestHeader("Authorization") String authHeader) {
        Map<String, String> response = new HashMap<>();

	    try {
	        // 從 Authorization Header 中提取 Token
	        String token = authHeader.replace("Bearer ", "");
	        System.out.println("token"+token);
	        // 從 Token 中獲取電子郵件
	        String email = jwtUtil.getEmailFromToken(token);
	        System.out.println("email"+email);
	        // 根據電子郵件獲取會員資料
	        Member member = memberService.findByEmail(email);
	        System.out.println();
	        if (member == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
	        }
	        
	        // 取得此member的ID
	        String memberID = member.getMemberID().toString();

	        // 進行後續操作...
	        response.put("memberID", memberID);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        // 處理 Token 無效或過期的情況
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
	    }
	}


}
