package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;

@Repository
public class PhoneDao {
	
	@Autowired
	private DataSource dataSource;
	
	
	//0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs =null; //select문에 사용됨
	
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";
	
	//생성자
	
	//디폴드 생성자 생략 (다른 생성자 없음)
	
	//메소드 g/s
	
	//메소드 일반
	
	//(공통되는부분 골라내기)
	
	//접속 메소드(DB접속)
	public void getConnection() {
		
		try {
			
			conn = dataSource.getConnection();
			
			//System.out.println("[접속성공]");
			
		}catch(SQLException e) {
			System.out.println("error:" + e );
		}	
		
	}
	
	//자원정리
	public void close() {	
		try {
			if(rs != null){
				 rs.close();
			}
				 
			if(pstmt != null) {
				pstmt.close();
					
			}
			if(conn != null) {
				conn.close();
			}
				
			}catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
	


	//1. 리스트가져오기
	public List<PersonVo> getPersonList(){
		
		List<PersonVo> phoneList = new ArrayList<PersonVo>();
		//db접속
		getConnection();
		
		try {
			
			String query = "";
			
			//3.sql문 준비 / 바인딩 실행 (* 는 왠만하면 쓰지 않기)
			query += " select person_id, ";
			query += "        name, ";
			query += "        hp, ";
			query += "        company ";
			query += " from person ";
			
			//System.out.println(query);
			
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				PersonVo vo = new PersonVo(personId , name , hp , company);
				phoneList.add(vo);
			}
				
		}catch(SQLException e) {
			System.out.println("error:" + e );
		}
	
		//자원정리
		close();
				
		return phoneList;
		
	}
	
	//2. 등록 (select)
	public int personInsert(PersonVo personVo) {
		
		getConnection();
		
		int count = 0;
		
		try {
			//3. sql문 준비 /바인딩 /실행
			String query = "";
			query += " insert into person ";
			query += " values (seq_person_id.nextval, ?, ?, ?)";
			//ORA-00911: invalid character 에러 (자바에서 sql로 쿼리를 날릴때 ; 가 들어있을 경우)
			
			//System.out.println(query); 확인용
			
			pstmt = conn.prepareStatement(query);
			pstmt.setNString(1, personVo.getName());
			pstmt.setNString(2, personVo.getHp());
			pstmt.setNString(3, personVo.getCompany());
			
			count = pstmt.executeUpdate();
			
			//4.결과처리
			
			System.out.println("[" + count + "건 등록되었습니다.]");
			 
		}catch(SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return count;
	}
	
	
	//3.수정(update)
	public int personUpdate(PersonVo personVo) {
		
		getConnection();
		
		int count = 0;
		
		try {
			//3.sql문 준비
			/*
			 --update문 (데이터 수정) //이정재 휴대폰번호, 회사번호 수정
					update person
					set name = '유정재',
						hp = '010-9999-9999',
					    company = '02-9999-9999'
					where person_id = 4;
			 */
			
			String query = "";
			query += " update person ";
			query += " set   name = ?, ";
			query += "       hp = ?, ";
			query += "       company = ? ";
			query += " where person_id = ? ";
			//중간에 from절이 들어가 있었음... 그래서 update문이 제대로 안돌아감
			
			System.out.println(query);
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, personVo.getName());
			pstmt.setString(2, personVo.getHp());
			pstmt.setString(3, personVo.getCompany());
			pstmt.setInt(4, personVo.getPerson_id());
		 
			count = pstmt.executeUpdate();
			//executeUpdate() --> 수행 결과로 int 타입의 값을 반환
			//executeQuery() --> 수행결과로 ResultSet 객체의 값을 반환
			//execute() --> 수행결과로 Boolean 타입의 값을 반환
			
			//4.결과처리
			System.out.println("[" + count + "건 수정되었습니다.]");
			
			
		}catch(SQLException e) {
			System.out.println("error:" + e );
		}
		
		//자원정리
		
		close();
		
		
		return count;
		
	}
	
	
	
	//4.삭제(delete)
	public int personDelete(int personId) {
		
		getConnection();
		
		int count = 0;
		
		try {
			//3.sql문 준비
			/*
			delete문 (데이터 삭제) // 서장훈행 삭제
			delete from person
			where person_id = 5;
		    */
			
			String query ="";
			query += " delete from person ";
			query += " where person_id = ? ";
			
			//System.out.println(query);
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);
			
			count = pstmt.executeUpdate();
			
			//4.결과처리
			
			System.out.println("[" + count + "건 삭제되었습니다.]");
			
		}catch(SQLException e) {
			System.out.println("error:" + e );
		}	
		
		//자원정리
		close();		
		
		return count;
	}
	
	//5.검색
	public List<PersonVo> getSearchList(String search){
		
		List<PersonVo> searchList = new ArrayList<PersonVo>();
		
		getConnection();
		
		try {
			//3. sql문 준비
			/*
			 select person_id,
			        name,
			        hp,
			        company
		    from person
			where name like '%이%'
			or hp like '%이%'
			or company like '%이%';
			*/
			String query = "";
			
			query += " select person_id, ";
			query += "        name, ";
			query += "        hp, ";
			query += "        company ";
			query += " from person ";
			query += " where name like ? ";
			query += " or hp like ? ";
			query += " or company like ? ";
			query += " order by person_id "; // 검색 후 person_id가 0으로 표기됨 --> 변함없음
			//System.out.println(query);
			
			//-- 검색 문자 입력값 (search)
			
			String str = "%" + search + "%";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, str);
			pstmt.setString(2, str);
			pstmt.setString(3, str);
			
			rs = pstmt.executeQuery();
			
			//4.결과처리

			while (rs.next()) {
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				
				PersonVo vo = new PersonVo(name , hp, company);
				searchList.add(vo);
			}
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		
		close();
		
		return searchList;
		
	}
	
	
	
	
	//사람 1명 정보 가져오기
	
	public PersonVo getPerson(int personId) {
		
		PersonVo personVo = null;
		
		//db접속
		getConnection();
		
		try {
			
			String query = "";
			
			//3.sql문 준비 / 바인딩 실행 (* 는 왠만하면 쓰지 않기)
			query += " select person_id, ";
			query += "        name, ";
			query += "        hp, ";
			query += "        company ";
			query += " from person ";
			query += " where person_id = ? ";
			
			//System.out.println(query);
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1,personId);
			
			rs = pstmt.executeQuery(); //날리다
			
			//결과처리
			
			while (rs.next()) {
				int personID = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				personVo = new PersonVo(personID , name , hp , company);
				//phoneList.add(vo);
			}
				
		}catch(SQLException e) {
			System.out.println("error:" + e );
		}
	
		//자원정리
		close();
				
		return personVo;
		
	}
	
	
	
	

}
