package it.bookshop.model.ObjectForm;

public class BookInfoResponse {
	/*
	 * classe utilizzata nelle richieste ajax della sezione di analisi delle vendite del venditore	 
	 */

		private String operation;
		private long bookID;
		private String title;
		private int soldcopies;
		private double totearn;
	

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public long getBookID() {
			return bookID;
		}

		public void setBookID(long bookID) {
			this.bookID = bookID;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	
		public int getSoldcopies() {
			return soldcopies;
		}

		public void setSoldcopies(int soldcopies) {
			this.soldcopies = soldcopies;
		}

		public double getTotearn() {
			return totearn;
		}

		public void setTotearn(double totearn) {
			this.totearn = totearn;
		}

	
	
}
