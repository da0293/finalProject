package com.wegoing.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wegoing.dto.ClubDTO;
import com.wegoing.dto.ClubMemberDTO;
import com.wegoing.dto.MemberDTO;
import com.wegoing.dto.PrincipalDetails;
import com.wegoing.service.ClubMemberService;
import com.wegoing.service.ClubService;
import com.wegoing.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class ClubMemberController {
	private ClubService cs;
	private ClubMemberService cms; 
	private MemberService ms;
	
	@GetMapping("/club/{clno}/member")
	public String clubMember(@PathVariable int clno, Model model,
							@AuthenticationPrincipal PrincipalDetails userDetails) {
		ClubDTO cdto = cs.getOne(clno);
		String loginEmail = userDetails.getMdto().getEmail();
		log.info("Email >>>>> " + loginEmail);
		
		List<MemberDTO> partner = ms.getMyPartners(loginEmail);
		List<MemberDTO> member = ms.getMembersInfo(clno);
		List<String> memEmail = cms.getEmailByClno(clno);

		model.addAttribute("club", cdto);
		model.addAttribute("partnerList", partner);		
		model.addAttribute("memberList", member);
		model.addAttribute("emailList", memEmail);
		
		return "club/clubMember";
	}
	
	@PostMapping("/addClubMember")
	@ResponseBody
	public String addClubMember(@RequestParam("clno") int clno,
								@RequestParam("email") String email) {
		log.info("추가 이메일 >>>>> " + email);
		ClubMemberDTO cmdto = ClubMemberDTO.builder()
										   .clno(clno)
										   .crank("guest")
										   .cstatus("n")
										   .email(email)
										   .build();
		cms.addClubMember(cmdto);
		return "redirect:/club/{clno}/member";
	}

}