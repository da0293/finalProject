package com.wegoing.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wegoing.dto.BoardDTO;
import com.wegoing.dto.PageHandler;
import com.wegoing.dto.PrincipalDetails;
import com.wegoing.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class BoardController {
	private final BoardService service;
	
	@GetMapping("/board")
	public String boardMain(Model model) {
		/**
		 * 1번 취미 2번 블라블라  3번 반려동물  4번 커리어
		 */
		List<BoardDTO> hobby = service.selectCategory(1);
		List<BoardDTO> free = service.selectCategory(2);
		List<BoardDTO> pet = service.selectCategory(3);
		List<BoardDTO> career = service.selectCategory(4);
		model.addAttribute("hobby",hobby);
		model.addAttribute("free",free);
		model.addAttribute("pet",pet);
		model.addAttribute("career",career);
		return "board/boardmain";
	}
	@GetMapping("/board/add")
	public String addboard(@AuthenticationPrincipal PrincipalDetails userDetails,RedirectAttributes rattr,Model model) {
	
		if(userDetails == null) {
			rattr.addFlashAttribute("msg","로그인해주세요");
			return "redirect:/freeboard";
		}
		model.addAttribute("board",new BoardDTO());
		return "board/addboard";
	}
	@PostMapping("/board/add")
	public String insertBoard(@Validated @ModelAttribute("board")BoardDTO board,BindingResult bindingResult,@AuthenticationPrincipal PrincipalDetails userDetails) {
		if(bindingResult.hasErrors()) {
			log.info("erros={}",bindingResult);
			return "board/addboard";
		}
		board.setEmail(userDetails.getMdto().getEmail());
		board.setNickname(userDetails.getMdto().getNickname());
		service.insert(board);
		return "redirect:/board";
	}

	
	
	
	@GetMapping("/freeboard")
	public String freeboard(Model model,Integer page, Integer pageSize) {
		if(page == null) page =1;
        if(pageSize ==null) pageSize = 10;
        int totalCnt = service.countBoard(2);
        PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);
        Map map = new HashMap();
        map.put("offset",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("bno",2);

        List<BoardDTO> free = service.selectPage(map);
		
		model.addAttribute("ph",pageHandler);
		model.addAttribute("page",page);
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("free",free);
		
		return "board/free/freeboard";
	}
	@GetMapping("/freeboard/{bno}")
	public String freeboardDetail(@PathVariable Integer bno,Model model,
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		
		BoardDTO free =  service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		String nickname =null;
		String writer = free.getNickname();
		service.updateHit(bno);
		
		if(userDetails !=null) nickname = userDetails.getMdto().getNickname();
		if(nickname !=null &&nickname.equals(writer)) {
			model.addAttribute("nick","YES");
		}
		else {
			model.addAttribute("nick","NO");
		}
		model.addAttribute("free",free);
		return "board/free/detailBoard";
	}
	
	@GetMapping("/freeboard/{bno}/edit")
	public String freeboardEdit(@PathVariable int bno,Model model) {
		BoardDTO free = service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		model.addAttribute("free",service.selectOne(bno));
		return "board/free/editBoard";
	}
	@PostMapping("/freeboard/{bno}/edit")
	public String freeboardEditcheck(@PathVariable int bno,Model model, @RequestParam("btitle")String btitle, @RequestParam("bcontent")String content) {
		BoardDTO free = service.selectOne(bno);
		model.addAttribute("free",free);
		
		free.setBtitle(btitle);
		free.setBcontent(content);
		
		service.update(free);
		
		return "redirect:/freeboard";
	}
	@PostMapping("/freeboard/{bno}/delete")
	public String freeboardDelete(@PathVariable int bno) {
		service.deleteOne(bno);
		return "redirect:/board";
	}
	
	
	
//	================================================
	
	@GetMapping("/pet")
	public String petboard(Model model,Integer page, Integer pageSize) {
		if(page == null) page =1;
        if(pageSize ==null) pageSize = 10;
        int totalCnt = service.countBoard(3);
        PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);
        Map map = new HashMap();
        map.put("offset",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("bno",3);

        List<BoardDTO> free = service.selectPage(map);
		
		model.addAttribute("ph",pageHandler);
		model.addAttribute("page",page);
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("free",free);
		return "board/pet/petBoard";
	}
	@GetMapping("/pet/{bno}")
	public String petboardDetail(@PathVariable Integer bno,Model model,
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		
		BoardDTO free =  service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		String nickname =null;
		String writer = free.getNickname();
		service.updateHit(bno);
		
		if(userDetails !=null) nickname = userDetails.getMdto().getNickname();
		if(nickname !=null &&nickname.equals(writer)) {
			model.addAttribute("nick","YES");
		}
		else {
			model.addAttribute("nick","NO");
		}
		model.addAttribute("free",free);
		return "board/pet/detailBoard";
	}
	
	@GetMapping("/pet/{bno}/edit")
	public String petboardEdit(@PathVariable int bno,Model model) {
		BoardDTO free = service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		model.addAttribute("free",service.selectOne(bno));
		return "board/pet/editBoard";
	}
	@PostMapping("/pet/{bno}/edit")
	public String petboardEditcheck(@PathVariable int bno,Model model, @RequestParam("btitle")String btitle, @RequestParam("bcontent")String content) {
		BoardDTO free = service.selectOne(bno);
		model.addAttribute("free",free);
		
		free.setBtitle(btitle);
		free.setBcontent(content);
		
		service.update(free);
		
		return "redirect:/pet";
	}
	@PostMapping("/pet/{bno}/delete")
	public String petboardDelete(@PathVariable int bno) {
		service.deleteOne(bno);
		return "redirect:/board";
	}
//	================================================
	
	@GetMapping("/hobby")
	public String hobbyBoard(Model model,Integer page, Integer pageSize) {
		if(page == null) page =1;
        if(pageSize ==null) pageSize = 10;
        int totalCnt = service.countBoard(1);
        PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);
        Map map = new HashMap();
        map.put("offset",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("bno",1);

        List<BoardDTO> free = service.selectPage(map);
		
		model.addAttribute("ph",pageHandler);
		model.addAttribute("page",page);
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("free",free);
		return "board/hobby/hobbyBoard";
	}
	@GetMapping("/hobby/{bno}")
	public String hobbyboardDetail(@PathVariable Integer bno,Model model,
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		
		BoardDTO free =  service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		String nickname =null;
		String writer = free.getNickname();
		service.updateHit(bno);
		
		if(userDetails !=null) nickname = userDetails.getMdto().getNickname();
		if(nickname !=null &&nickname.equals(writer)) {
			model.addAttribute("nick","YES");
		}
		else {
			model.addAttribute("nick","NO");
		}
		model.addAttribute("free",free);
		return "board/hobby/detailBoard";
	}
	
	@GetMapping("/hobby/{bno}/edit")
	public String hobbyboardEdit(@PathVariable int bno,Model model) {
		BoardDTO free = service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		model.addAttribute("free",service.selectOne(bno));
		return "board/hobby/editBoard";
	}
	@PostMapping("/hobby/{bno}/edit")
	public String hobbyboardEditcheck(@PathVariable int bno,Model model, @RequestParam("btitle")String btitle, @RequestParam("bcontent")String content) {
		BoardDTO free = service.selectOne(bno);
		model.addAttribute("free",free);
		
		free.setBtitle(btitle);
		free.setBcontent(content);
		
		service.update(free);
		
		return "redirect:/hobby";
	}
	@PostMapping("/hobby/{bno}/delete")
	public String hobbyboardDelete(@PathVariable int bno) {
		service.deleteOne(bno);
		return "redirect:/board";
	}
	
//	================================================
	
	@GetMapping("/career")
	public String careerBoard(Model model,Integer page, Integer pageSize) {
		if(page == null) page =1;
        if(pageSize ==null) pageSize = 10;
        int totalCnt = service.countBoard(4);
        PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);
        Map map = new HashMap();
        map.put("offset",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("bno",4);

        List<BoardDTO> free = service.selectPage(map);
		
		model.addAttribute("ph",pageHandler);
		model.addAttribute("page",page);
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("free",free);
		return "board/career/careerBoard";
	}
	@GetMapping("/career/{bno}")
	public String careerboardDetail(@PathVariable Integer bno,Model model,
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		
		BoardDTO free =  service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		String nickname =null;
		String writer = free.getNickname();
		service.updateHit(bno);
		
		if(userDetails !=null) nickname = userDetails.getMdto().getNickname();
		if(nickname !=null &&nickname.equals(writer)) {
			model.addAttribute("nick","YES");
		}
		else {
			model.addAttribute("nick","NO");
		}
		model.addAttribute("free",free);
		return "board/career/detailBoard";
	}
	
	@GetMapping("/career/{bno}/edit")
	public String careerboardEdit(@PathVariable int bno,Model model) {
		BoardDTO free = service.selectOne(bno);
		if(free ==null) {
			return "error-page/500";
		}
		model.addAttribute("free",service.selectOne(bno));
		return "board/career/editBoard";
	}
	@PostMapping("/career/{bno}/edit")
	public String careerboardEditcheck(@PathVariable int bno,Model model, @RequestParam("btitle")String btitle, @RequestParam("bcontent")String content) {
		BoardDTO free = service.selectOne(bno);
		model.addAttribute("free",free);
		
		free.setBtitle(btitle);
		free.setBcontent(content);
		
		service.update(free);
		
		return "redirect:/career";
	}
	@PostMapping("/career/{bno}/delete")
	public String careerboardDelete(@PathVariable int bno) {
		service.deleteOne(bno);
		return "redirect:/board";
	}
}