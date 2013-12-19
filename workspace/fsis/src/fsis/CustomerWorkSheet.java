package fsis;

import static fsis.TextIO.putln;

import java.util.Iterator;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import kengine.Doc;
import kengine.DocCnt;
import kengine.Engine;
import kengine.NotPossibleException;
import kengine.Query;

/**
 * @overview Represents a worksheet program that enables a user to create
 *           customer objects, display a report about them, and to search for
 *           objects of interest using keywords.
 * 
 * @attributes objects SortedSet<Customer> engine Engine
 * 
 * @abstract_properties mutable(objects)=false /\ optional(objects)=false /\
 *                      mutable(engine)=false /\ optional(engine)=false /\
 *                      size(objects) > 0 -> (for all o in objects:
 *                      o.toHtmlDoc() is in engine)
 * @author dmle
 */
public class CustomerWorkSheet {
	@DomainConstraint(type = "Collection", mutable = false, optional = false)
	private SortedSet objects;
	@DomainConstraint(type = "Engine", mutable = false, optional = false)
	private Engine engine;

	/**
	 * @effects initialise this to include an empty set of objects and an empty
	 *          engine
	 */
	public CustomerWorkSheet() {
		objects = new TreeSet<Customer>();
		engine = new Engine();
	}

	/**
	 * @effects invoke promptForCustomer to prompt the user to enter details of
	 *          a customer, create a Customer object from these details and
	 *          invoke addCustomer to add the object to this.
	 * 
	 *          If invalid details were entered then throws
	 *          NotPossibleException.
	 */
	public void enterACustomer() throws NotPossibleException {
		System.out.println("Enter a Customer: ");
		Customer c = null;
		try {
			c = this.promptForCustomer();

			if (c == null) {
				throw new NotPossibleException(
						"CustomerWorkSheet.enterACustomer: Invalid Customer object to insert.");
			}

			this.addCustomer(c);
		} catch (NotPossibleException e) {
			throw new NotPossibleException(e.getMessage());
		}
		TextIO.putln("Added: " + c.toString());
	}

	/**
	 * @effects prompt the user whether to enter details for Customer or
	 *          HighEarner, then prompt the user for the data values needed to
	 *          create an object for the selected type.
	 * 
	 *          Create and return a Customer object from the entered data.
	 *          Throws NotPosibleException if invalid data values were entered.
	 */
	public Customer promptForCustomer() throws NotPossibleException {

		Customer c = null;
		int id;
		String name, phoneNumber, adress;
		float income = 0;

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter id: ");
		id = sc.nextInt();

		sc = new Scanner(System.in);
		System.out.println("Enter name: ");
		name = sc.nextLine();

		sc = new Scanner(System.in);
		System.out.println("Enter phoneNumber: ");
		phoneNumber = sc.nextLine();

		sc = new Scanner(System.in);
		System.out.println("Enter adress: ");
		adress = sc.nextLine();

		sc = new Scanner(System.in);
		System.out
				.println("Enter income (0 for Customer; >=10^7 for HighEarner): ");
		income = sc.nextFloat();

		if (income >= 10000000.0) {
			c = new HighEarner(id, name, phoneNumber, adress, income);
		} else {
			c = new Customer(id, name, phoneNumber, adress);
		}

		return c;
	}

	/**
	 * @effects add c to this.objects and add to this.engine a Doc object
	 *          created from c.toHtmlDoc
	 */
	public void addCustomer(Customer c) {
		this.objects.add(c);
		this.engine.addDoc(new Doc(c.toHtmlDoc()));
	}

	/**
	 * @modifies System.out
	 * @effects if this.objects == null prints "empty" else prints each object
	 *          in this.objects one per line to the standard output invoke
	 *          writeHTMLReport to write an HTML report to file
	 */
	public void displayReport() {
		putln("Displaying customer report (sorted by name)...");
		if (this.objects == null || this.objects.isEmpty()) {
			putln("empty");
		} else {
			Iterator<Customer> iterator = this.objects.iterator();
			while (iterator.hasNext()) {
				Customer c = iterator.next();
				putln(c.toString());
			}
		}

		putln("Wring customer report (sorted by name) to file objects.html...");
		this.writeHTMLReport();
		putln("Report written to file objects.html");
	}

	/**
	 * @effects if objects is empty return "empty" else return a string
	 *          containing each object in this.objects one per line
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.objects == null || this.objects.isEmpty()) {
			sb.append("empty");
		} else {
			Iterator<Customer> iterator = this.objects.iterator();
			while (iterator.hasNext()) {
				Customer c = iterator.next();
				sb.append(c.toString()).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * @modifies objects.html (in the program directory)
	 * @effects if this.objects is empty write an HTML document to file with the
	 *          word "empty" in the body else write an HTML document to file
	 *          containing a table, each row of which lists an object in
	 *          this.objects
	 * 
	 *          The HTML document must be titled "Customer report".
	 */
	public void writeHTMLReport() {
		TextIO.writeFile("objects.html");
		putln("<html>");
		putln("<head><title>Customer report</title></head>");
		putln("<body>");
		putln("<table border=1>");
		putln("<tr><th>Id</th><th>Name</th><th>Phone number</th><th>Address</th><th>Income</th></tr>");
		Iterator<Customer> iterator = this.objects.iterator();
		while (iterator.hasNext()) {
			Customer c = iterator.next();
			putln("<tr>");
			putln("<td>" + c.getId() + "</td>");
			putln("<td>" + c.getName() + "</td>");
			putln("<td>" + c.getPhoneNumber() + "</td>");
			putln("<td>" + c.getAddress() + "</td>");
			if (HighEarner.class.isInstance(c)) {
				HighEarner h = (HighEarner) c;
				putln("<td>" + h.getIncome() + "</td>");
			}
			putln("</tr>");
		}
		putln("</table>");
		putln("</body></html>");
	}

	/**
	 * @modifies System.out
	 * @effects prompt the user to enter one or more keywords if keywords !=
	 *          null AND keywords.length > 0 invoke operation search(String[])
	 *          to search using keywords,
	 * 
	 *          if fails to execute the query throws NotPossibleException else
	 *          print the query string to the standard output. invoke operation
	 *          writeSearchReport(Query) to output the query to an HTML file
	 *          else print "no search keywords"
	 */
	public void search() throws NotPossibleException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter some keywords (separated by space): ");
		String keywords = sc.nextLine();

		if (keywords != null && keywords.length() > 0) {
			Query q = null;
			try {
				System.out.println("Searching for customers using keywords: ["
						+ keywords.replaceAll(" ", ", ") + "]");
				q = this.search(keywords.split(" "));
			} catch (NotPossibleException e) {
				throw new NotPossibleException(e.getMessage());
			}

			System.out.print("Query: [");
			System.out.print(keywords.replaceAll(" ", ", "));
			System.out.println("]");

			System.out.println("Matches [" + q.size() + "]:");
			System.out.print("[");
			Iterator iterator = q.matchIterator();
			while (iterator.hasNext()) {
				DocCnt d = (DocCnt) iterator.next();
				System.out.println("<" + d.getDoc().title() + ">, ");
			}

			this.writeSearchReport(q);
		} else {
			System.out.println("no search keywords");
		}
	}

	/**
	 * @effects prompt the user to enter some keywords and return an array
	 *          containing these or null if no keywords were entered
	 */
	public String[] promptForKeywords() {
		return null;
	}

	/**
	 * @requires words != null /\ words.length > 0
	 * @effects search for objects whose HTML documents match with the query
	 *          containing words and return a Query object containing the result
	 * 
	 *          If fails to execute query using words throws
	 *          NotPossibleException
	 */
	public Query search(String[] words) throws NotPossibleException {
		Query q = null;
		try {
			q = this.engine.queryFirst(words[0]);
			for (int i = 1; i < words.length; i++) {
				q = this.engine.queryMore(words[i]);
			}
		} catch (NotPossibleException e) {
			throw new NotPossibleException(e.getMessage());
		}
		return q;
	}

	/**
	 * @requires q != null
	 * @modifies search.html (in the program directory)
	 * @effects write to file an HTML document containing the query keys and a
	 *          table, each row of which lists a match
	 * 
	 *          The HTML document must be titled "Search report".
	 */
	public void writeSearchReport(Query q) {
		TextIO.writeFile("search.html");
		putln("<html>");
		putln("<head><title>Search report</title></head>");
		putln("<body>");
		putln("<b>Query: </b>");
		String[] keywords = q.keys();
		putln("[" + keywords[0]);
		for (int i = 1; i < keywords.length; i++) {
			putln(", " + keywords[i]);
		}
		putln("]");
		putln("<br><b>Result:</b>");
		putln("<table border=1>");
		putln("<tr><th>Documents</th><th>Sum freqs</th></tr>");
		// TODO: result
		Iterator iterator = q.matchIterator();
		while (iterator.hasNext()) {
			DocCnt d = (DocCnt) iterator.next();
			putln("<tr>");
			putln("<td>");
			putln(d.getDoc().title());
			putln("</td>");
			putln("<td>");
			putln(d.getCount());
			putln("</td>");
			putln("</tr>");
		}
		putln("</table>");
		putln("</body></html>");
	}

	/**
	 * @effects initialise a CustomerWorkSheet ask the users to create the five
	 *          Customer objects display report about the objects ask the users
	 *          to enter a keyword query to search for objects and display the
	 *          result
	 */
	public static void main(String[] args) {
		// initialise a CustomerWorkSheet
		putln("Initialising program...");
		CustomerWorkSheet worksheet = new CustomerWorkSheet();

		try {
			// ask user to create 5 test customer objects
			putln("\nCreating some customers...");
			// int num = 5;
			// for (int i = 0; i < num; i++) {
			// worksheet.enterACustomer();
			// }
			Customer c1 = new Customer(1, "James", "12345678", "HCM");
			Customer c2 = new Customer(2, "Peter", "12345679", "Hanoi");
			Customer c3 = new Customer(3, "Lucas", "12345694", "HCM");
			Customer c4 = new Customer(4, "John john", "12345678", "Hanoi");
			Customer c5 = new HighEarner(5, "Andrew john", "12345680", "Hanoi",
					100000000);

			worksheet.addCustomer(c1);
			worksheet.addCustomer(c2);
			worksheet.addCustomer(c3);
			worksheet.addCustomer(c4);
			worksheet.addCustomer(c5);

			// display report about the objects
			worksheet.displayReport();

			// ask the users to enter a keyword query to search for objects and
			// display
			// the result
			worksheet.search();

			// end
			putln("Good bye.");
		} catch (NotPossibleException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
