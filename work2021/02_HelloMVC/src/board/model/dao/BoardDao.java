package board.model.dao;

import static common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import board.model.exception.BoardException;
import board.model.vo.Board;
import board.model.vo.BoardComment;


public class BoardDao {

	private Properties prop = new Properties();

	/**
	 * build-path의 board-query.properties의 내용을 읽어와 필드 prop에 저장한다.
	 */
	public BoardDao() {
		try {
			//클래스객체 위치찾기 : 절대경로를 반환한다. 
			String fileName = BoardDao.class.getResource("/sql/board/board-query.properties").getPath();
			prop.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public List<Board> selectBoardList(Connection conn, int cPage, int numPerPage) {
		List<Board> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectBoardList");
		
		try{
			//미완성쿼리문을 가지고 객체생성. 
			pstmt = conn.prepareStatement(query);
			
			//시작 rownum과 마지막 rownum 구하는 공식
			pstmt.setInt(1, (cPage-1)*numPerPage+1);
			pstmt.setInt(2, cPage*numPerPage);
			
			//쿼리문실
			//완성된 쿼리를 가지고 있는 pstmt실행(파라미터 없음)
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				Board b = new Board();
				//컬럼명은 대소문자 구분이 없다.
				b.setBoardNo(rset.getInt("board_no"));
				b.setBoardTitle(rset.getString("board_title"));
				b.setBoardWriter(rset.getString("board_writer"));
				b.setBoardContent(rset.getString("board_content"));
				b.setBoardOriginalFileName(rset.getString("board_original_filename"));
				b.setBoardRenamedFileName(rset.getString("board_renamed_filename"));
				b.setBoardDate(rset.getDate("board_date"));
				b.setBoardReadCount(rset.getInt("board_read_count"));
				list.add(b);
			}
			
		}catch(Exception e){
//			e.printStackTrace();
			//런타임예외로 전환해서 다시 던지기
			throw new RuntimeException("게시물 조회 오류", e);
		}finally{
			close(rset);
			close(pstmt);
		}
		
		
		return list;
	}

	public int selectBoardCount(Connection conn) {
		PreparedStatement pstmt = null;
		int totalContents = 0;
		ResultSet rset = null;
		String query = prop.getProperty("selectBoardCount");
		
		try{
			//미완성쿼리문을 가지고 객체생성. 
			pstmt = conn.prepareStatement(query);
			
			//쿼리문실행
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				totalContents = rset.getInt("cnt");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(rset);
			close(pstmt);
		}
		
		return totalContents;
	}

	public int insertBoard(Connection conn, Board board) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardWriter());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setString(4, board.getBoardOriginalFileName());
			pstmt.setString(5, board.getBoardRenamedFileName());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public Board selectOne(Connection conn, int boardNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String query = prop.getProperty("selectOne");
		//select * from board where board_no = ?
		Board b = null;
		
		try{
			//미완성쿼리문을 가지고 객체생성. 
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardNo);
			
			//쿼리문실행
			//완성된 쿼리를 가지고 있는 pstmt실행(파라미터 없음)
			rset = pstmt.executeQuery();
			while(rset.next()){
				b = new Board();
				//컬럼명은 대소문자 구분이 없다.
				b.setBoardNo(rset.getInt("board_no"));
				b.setBoardTitle(rset.getString("board_title"));
				b.setBoardWriter(rset.getString("board_writer"));
				b.setBoardContent(rset.getString("board_content"));
				b.setBoardOriginalFileName(rset.getString("board_original_filename"));
				b.setBoardRenamedFileName(rset.getString("board_renamed_filename"));
				b.setBoardDate(rset.getDate("board_date"));
				b.setBoardReadCount(rset.getInt("board_read_count"));
			}
			
		}catch(Exception e){
			//런타임예외, 구체적인 의미를 가진 예외객체로 전환해서 다시 던지기
			throw new BoardException("게시물 조회 오류", e);
		}finally{
			close(rset);
			close(pstmt);
		}
		return b;
	}

	/**
	 * 게시글 등록 직후, 게시글 번호를 가져온다.
	 * @param conn
	 * @return
	 */
	public int selectLastBoardNo(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectLastBoardNo");
		int boardNo = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if(rset.next())
				boardNo = rset.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return boardNo;
	}

	public int updateBoardReadCount(Connection conn, int boardNo) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateBoardReadCount");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int deleteBoard(Connection conn, int boardNo) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteBoard");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	public int updateBoard(Connection conn, Board b) {
		int result = 0;
		PreparedStatement pstmt = null;
		String query = prop.getProperty("updateBoard"); 
		
		try {
			//미완성쿼리문을 가지고 객체생성.
			pstmt = conn.prepareStatement(query);
			//쿼리문미완성
			pstmt.setString(1, b.getBoardTitle());
			pstmt.setString(2, b.getBoardContent());
			pstmt.setString(3, b.getBoardOriginalFileName());
			pstmt.setString(4, b.getBoardRenamedFileName());
			pstmt.setInt(5, b.getBoardNo());
			
			//쿼리문실행 : 완성된 쿼리를 가지고 있는 pstmt실행(파라미터 없음)
			//DML은 executeUpdate()
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int insertBoardComment(Connection conn, BoardComment bc) {
		int result = 0;
		PreparedStatement pstmt = null;
		String query = prop.getProperty("insertBoardComment"); 
		//insert into board_comment 
		//values(seq_board_comment_no.nextval, ?, ?, ?, ?, ?, default)
		try {
			//미완성쿼리문을 가지고 객체생성.
			pstmt = conn.prepareStatement(query);
			//쿼리문미완성
			pstmt.setInt(1, bc.getBoardCommentLevel());
			pstmt.setString(2, bc.getBoardCommentWriter());
			pstmt.setString(3, bc.getBoardCommentContent());
			pstmt.setInt(4, bc.getBoardRef());
			pstmt.setObject(5, bc.getBoardCommentRef() != 0 ? 
									bc.getBoardCommentRef() : 
										null);// 댓글인 경우 0번 댓글을 참조
			
			//쿼리문실행 : 완성된 쿼리를 가지고 있는 pstmt실행(파라미터 없음)
			//DML은 executeUpdate()
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public List<BoardComment> selectBoardCommentList(Connection conn, int boardNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectBoardCommentList");
		List<BoardComment> commentList = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			rset = pstmt.executeQuery();
			
			commentList = new ArrayList<BoardComment>();
			
			while(rset.next()) {
				BoardComment boardComment = new BoardComment();
				
				boardComment.setBoardCommentNo(rset.getInt("board_comment_no"));
				boardComment.setBoardCommentLevel(rset.getInt("board_comment_level"));
				boardComment.setBoardCommentWriter(rset.getString("board_comment_writer"));
				boardComment.setBoardCommentContent(rset.getString("board_comment_content"));
				boardComment.setBoardRef(rset.getInt("board_ref"));
				boardComment.setBoardCommentRef(rset.getInt("board_comment_ref"));
				boardComment.setBoardCommentDate(rset.getDate("board_comment_date"));
               
				commentList.add(boardComment);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("게시물 조회 오류", e);
			
		} finally {
			//5. 자원반납
			close(rset);
			close(pstmt);
		}
		return commentList;
	}


	
}
