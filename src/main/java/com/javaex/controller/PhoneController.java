package com.javaex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller   //import도 시켜줘야함 , 어너테이션? 주석의 개념
@RequestMapping(value="/phone")
public class PhoneController {
	
	@Autowired
	private PhoneDao phoneDao;
	
	//필드
	
	//생성자
	
	//메소드g/s
	
	/**메소드 일반**메소드마다 기능 1개씩 --> 기능마다url 부여**/
	/**
	 * 테스트
	@RequestMapping(value="/writeForm", method= {RequestMethod.GET ,RequestMethod.POST})
	public String writeForm() {
		//로직구현
		System.out.println("writeForm");
		return "/WEB-INF/views/writeForm.jsp";
	}
	
	@RequestMapping(value="/list" , method= {RequestMethod.GET , RequestMethod.POST})
	public String list() {
		//로직구현
		System.out.println("list");
		return "/WEB-INF/views/list.jsp";
	}
	**/
		//리스트
		@RequestMapping(value="/list", method= {RequestMethod.GET ,RequestMethod.POST})
		public String list(Model model){ //Model도 import
			
			System.out.println("list");
			
			//
			List<PersonVo> personList = phoneDao.getPersonList();
			System.out.println(personList.toString());
			
			
			//model --> date를 보내는 방법 --> 담아놓으면 된다.
			model.addAttribute("pList", personList);
			
			return "list";
		}
		//등록폼
		@RequestMapping(value="/writeForm", method= {RequestMethod.GET ,RequestMethod.POST})
		public String writeForm() {
			
			System.out.println("writeForm");	
			return "writeForm";
		}
	
		//등록
		//http://localhost:8088/phonebook3/phone/write?name=김이슬&hp=010-1111-1111&company=02-1111-1111
		@RequestMapping(value="write", method={RequestMethod.GET ,RequestMethod.POST})
		public String write(@RequestParam("name") String name,
							@RequestParam("hp") String hp,
							@RequestParam("company") String company) {
			System.out.println("write :"+ name + "," + hp + "," + company );
			
			PersonVo personVo = new PersonVo(name,hp,company);
			System.out.println(personVo.toString());
			
			phoneDao.personInsert(personVo); //personVo 를 db안에 저장해줌 

			return "redirect:/phone/list";
		}

		//수정폼 -->modifyForm		
		@RequestMapping(value="/modifyForm", method= {RequestMethod.GET ,RequestMethod.POST})
		public String modifyForm(Model model,
								@RequestParam("id") int id) {  //이렇게 써도 되는지? 
			
			System.out.println("modifyForm");	
			System.out.println(id);
			
			//id로 한사람의 정보 불러오기
			PersonVo personVo = phoneDao.getPerson(id);
			
			System.out.println("수정할 사람의 정보 : "+personVo);
			
			//date전달.
			model.addAttribute("oneVo", personVo);
			
			return "modifyForm";
		}
			
		//수정 -->modify
		@RequestMapping(value="/modify2", method= {RequestMethod.GET ,RequestMethod.POST})
		public String modify2(@RequestParam("name") String name,
					         @RequestParam("hp") String hp,
						     @RequestParam("company") String company,
						     @RequestParam("person_id") int id) {			
			
			System.out.println("modify : "+name + "," + hp + "," + company + "," + id );
			
			PersonVo personVo = new PersonVo(id,name,hp,company);
			System.out.println(personVo.toString());
			
			phoneDao.personUpdate(personVo); //정보 수정 

			return "redirect:/phone/list";			
		}
		
		// 다른방식 
		//-->@ModelAttribute PersonVo personVo -->@ModelAttribut 생략가능
		//(@RequestParam 과 PersonVo personVo = new PersonVo(id,name,hp,company) 을 대체할수 있다.)
		// PersonVo personVo 로 넣을때는 없을수도 있는 경우에 대비해주지 않아도 된다.
		@RequestMapping(value="/modify", method= {RequestMethod.GET ,RequestMethod.POST})
		public String modify(@ModelAttribute PersonVo personVo) {			
			System.out.println("modify");
			
			System.out.println(personVo.toString());
			
			phoneDao.personUpdate(personVo); //정보 수정 

			return "redirect:/phone/list";			
		}
		
		//삭제 -->delete
		@RequestMapping(value="/delete2", method= {RequestMethod.GET ,RequestMethod.POST})
		public String delete2(@RequestParam("persin_id") int id) {			
			
			System.out.println("delete2 : "+ id );
			
			phoneDao.personDelete(id); //정보 수정 

			return "redirect:/phone/list";			
		}	
		//삭제다른방법
		@RequestMapping(value="/delete/{person_id}", method= {RequestMethod.GET ,RequestMethod.POST})
		public String delete(@PathVariable("person_id") int id) {			
			
			System.out.println("delete : "+ id );
			
			phoneDao.personDelete(id); //정보 수정 

			return "redirect:/phone/list";			
		}	
	
	//메소드 단위로 기능을 정의
	//이전에는 doGet() 메소드 1개에 파라미터로 구분
	
	
	
	
	
	
}
