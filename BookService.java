package service;

import Entity.Book;

public interface BookService {

	Book getBookById(Long bookId);

	void saveBook(Book book);

}
