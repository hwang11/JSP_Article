package mvjsp.chap17.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mvjsp.chap17.board.dao.ArticleDao;
import mvjsp.chap17.board.model.Article;
import mvjsp.chap17.board.model.ArticleListModel;
import mvjsp.jdbc.JdbcUtil;
import mvjsp.jdbc.connection.ConnectionProvider;

public class ListArticleService {
	private static ListArticleService instance = new ListArticleService();
	public static ListArticleService getInstance(){
		return instance;
	}
	public static final int COUNT_PER_PAGE = 10;
	
	public ArticleListModel getArticleList(int requestPageNumber) {
		if(requestPageNumber < 0) throw new IllegalArgumentException("page number<0 : "+requestPageNumber);
		ArticleDao articleDao = ArticleDao.getInstance();
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			int totalArticleCount = articleDao.selectCount(conn);
			if(totalArticleCount == 0) {
				return new ArticleListModel();
			}
			int totalPageCount = calculateTotalPageCount(totalArticleCount);
			
			int firstRow = (requestPageNumber - 1)* COUNT_PER_PAGE +1; //해당 페이지 첫번째 행 
			int endRow = firstRow + COUNT_PER_PAGE -1;//해당 페이지 마지막 행 
			
			if(endRow > totalArticleCount) {
				endRow = totalArticleCount;
			}
			List<Article> articleList = articleDao.select(conn, firstRow, endRow);
			ArticleListModel articleListModel = new ArticleListModel(
					articleList,requestPageNumber,totalPageCount,firstRow,endRow);
			return articleListModel;
		}catch(SQLException e) {
			throw new RuntimeException("DB 에러:"+e.getMessage(),e);
		}
		finally {
			JdbcUtil.close(conn);
		}
	}
	public int calculateTotalPageCount(int totalArticleCount) {
		if(totalArticleCount == 0) return 0;
		int pageCount = totalArticleCount/COUNT_PER_PAGE;
		if(totalArticleCount % COUNT_PER_PAGE > 0) { //나머지가 있으면 
			pageCount++;
		}
		return pageCount;
	}
}
