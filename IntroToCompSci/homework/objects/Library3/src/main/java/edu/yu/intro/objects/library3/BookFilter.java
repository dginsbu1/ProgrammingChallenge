package edu.yu.intro.objects.library3;
public class BookFilter{
	private String author, title, type;
	private long isbn13;
	

	//creates BookFilter
	//test: null, none filled, one/two/three/four filled,
	// 0matched, 1match, multiple match 
	private BookFilter(final Builder builder){
		this.author = builder.author;
		this.title = builder.title;
		this.type = builder.type;
		this.isbn13 = builder.isbn13;
	}

	//test: book null, match some, match none, match all, all/some filled
	public boolean filter(Book book){
		if(book == null){
			throw new IllegalArgumentException("Book can't be null");	
		}
		//checks to see if field wasn't set or equal to that of book
		if(author == null || author.equals(book.getAuthor())){
			if(title == null || title.equals(book.getTitle())){
				if(type == null || type.equals(book.getBookType())){
					if(isbn13 == 0L || isbn13 == book.getISBN13()){
						return true;
					}
				}
			}
		}
		return false;
	}
	public static class Builder{
		private String author, title, type;
		private long isbn13;
		//no aargs constructor for builder
		public Builder(){
		}

		//sets author to inputed author
		//test null, "", normal
		public Builder setAuthor(String author){
			if(!isValid(author)){
				throw new IllegalArgumentException("Author must have at least length one");
			}
			this.author = author;
			return this;

		}
		//sets title to inputted title
		//test: null, "", normal
		public Builder setTitle(String title){
			if(!isValid(title)){
				throw new IllegalArgumentException("Book title must have at least length one");		
			}
			this.title = title;
			return this;
		}
		/**
		*sets isbn field to inputted isbn
		*@param isbn a long
		*@return Buildr with isbn set
		*/
		public Builder setISBN13(long isbn13){
			if(!isValidISBN(isbn13)){
				throw new IllegalArgumentException("ISBN must be 13 digits long");
			}
			this.isbn13 = isbn13;
			return this;
		}

		/**
		* sets type to bookType
		* @param bookType String
		* @return Builder with type of 
		* @throws illegalArgumentException if type doenst match hardcover, softcover, ebook 
		*/
		//test null, "", normal, (harcover, paperback, ebook)
		public Builder setBookType(String bookType){
			//checks to make sure input is one of the three choices
	        switch (bookType) {
	            case "hardcover":  
	            	this.type = "hardcover";
	                break;
	            case "paperback":
	           		this.type = "paperback";
	                break;
	            case "ebook":
	           		this.type = "ebook";
	                break;
	            default:
	            	throw new IllegalArgumentException("Book type must be hardcover, softcover, or ebook.");
	         }
	         return this;
		}

		/**
		* makes a BookFilter based on inputted filter
		* @return BookFilter with settings configured
		*/
		public BookFilter build(){
			return new BookFilter(this);
		}

		/**
		*returns true if string is not null or empty
		*@param a string 
		*@return boolean
		*/
		private static boolean isValid(String str){
			if(str == null || str.equals("")){
			return false;
			}

			return true;
		}

		//checks to make sure ISBN is 13 integers 
		private static boolean isValidISBN(long isbn){
				String isbnString = ""+isbn;
				return (isbnString.length()==13);
		}
	}
}