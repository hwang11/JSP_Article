package mvjsp.chap17.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mvjsp.chap17.board.model.Article;
import mvjsp.jdbc.JdbcUtil;

public class ArticleDao {
	private static ArticleDao instance = new ArticleDao();
	public static ArticleDao getInstance() {
		return instance;
	}
	private ArticleDao() {
		
	}
	public int selectCount(Connection conn) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement(); 
			rs = stmt.executeQuery("select count(*) from article"); //SELECT쿼리를 실행한다.
			rs.next();
			return rs.getInt(1);
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}

	public List<Article> select(Connection conn,int firstRow,int endRow) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select article_id,"
					+"group_id,sequence_no, posting_date,"
					+"read_count,writer_name,password,title"
					+"from article order by sequence_no desc limit ?,?");
			
			pstmt.setInt(1, firstRow-1); //시작시점 
			pstmt.setInt(2, endRow - firstRow + 1); // 개수 
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				return Collections.emptyList();
			}
			List <Article> articleList = new ArrayList<Article>();
			do {
				Article article = makeArticleFromResultSet(rs,false);
				articleList.add(article);
			}
			while(rs.next()); //
			return articleList;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	private Article makeArticleFromResultSet(ResultSet rs,boolean readContent)throws SQLException{
		Article article = new Article();
		article.setId(rs.getInt("article_id"));
		article.setGroupId(rs.getInt("group_id"));
		article.setSequenceNumber(rs.getString("sequence_no"));
		article.setPositingDate(rs.getTimestamp("posting_date"));
		article.setReadCount(rs.getInt("read_count"));
		article.setWriteName(rs.getString("writer_name"));
		article.setPassword(rs.getString("password"));
		article.setTitle(rs.getString("title"));
		if(readContent) {
			article.setContent(rs.getString("content"));
		}
		
		return article; //
	}
	public int insert(Connection conn, Article article) throws SQLException{
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("insert into article "
										+ "(group_id, sequence_no, posting_date, read_count, "
										+ "writer_name, password, title, content) "
										+ "values (?,?,?,0,?,?,?,?)");
			pstmt.setInt(1, article.getGroupId()); 
			pstmt.setString(2, article.getSequenceNumber());
			pstmt.setTimestamp(3, new Timestamp(article.getPositingDate().getTime()));
			pstmt.setString(4, article.getWriterName());
			pstmt.setString(5, article.getPassword());
			pstmt.setString(6, article.getTitle());
			pstmt.setString(7, article.getContent());
			
			int insertCount = pstmt.executeUpdate();
			
			if(insertCount >0) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select last_insert_id() from article");
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
			return -1; // 안되면.
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
		
	}
}
