package fsis;

import kengine.NotPossibleException;

/**
 * 
 * @author hieudt
 * 
 */
public class Customer implements Comparable<Customer>, Document {
	@DomainConstraint(type = "Integer", mutable = false, optional = false, length = 8, min = 1, max = 99999999)
	private int id;
	@DomainConstraint(type = "String", mutable = true, optional = false, length = 50)
	private String name;
	@DomainConstraint(type = "String", mutable = true, optional = false, length = 10)
	private String phoneNumber;
	@DomainConstraint(type = "String", mutable = true, optional = false, length = 100)
	private String address;

	// private static Integer COUNTER;

	public Customer(int id, String name, String phoneNumber, String adress) {
		// if (Customer.COUNTER == null) {
		// Customer.COUNTER = new Integer(0);
		// }
		// this.id = Customer.COUNTER.intValue() + 1;
		// Customer.COUNTER = new Integer(this.id);

		this.id = id;

		if (validate(name, phoneNumber, adress)) {
			this.name = name;
			this.phoneNumber = phoneNumber;
			this.address = adress;
		} else {
			throw new NotPossibleException("Customer<init>: invalid arguments");
		}
	}

	public void setName(String name) {
		if (validateName(name))
			this.name = name;
		else
			throw new NotPossibleException("Customer.setName: invalid name "
					+ name);
	}

	public void setPhoneNumber(String phoneNumber) {
		if (validatePhoneNumber(phoneNumber))
			this.phoneNumber = phoneNumber;
		else
			throw new NotPossibleException(
					"Customer.setPhoneNumber: invalid phone " + phoneNumber);
	}

	public void setAddress(String address) {
		if (validateAddress(address))
			this.address = address;
		else
			throw new NotPossibleException(
					"Customer.setAddress: invalid address " + address);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public boolean repOK() {
		if (id < 1 || id > 999999999 || name == null || phoneNumber == null
				|| address == null)
			return false;
		return true;
	}

	public String toString() {
		return "Customer:<" + id + ", " + name + ", " + phoneNumber + ", "
				+ address + ">";
	}

	private boolean validateName(String n) {
		if (n == null)
			return false;
		else
			return true;
	}

	private boolean validatePhoneNumber(String n) {
		if (n == null)
			return false;
		else
			return true;
	}

	private boolean validateAddress(String n) {
		if (n == null)
			return false;
		else
			return true;
	}

	private boolean validate(String name, String phone, String address) {
		return validateName(name) && validatePhoneNumber(phone)
				&& validateAddress(address);
	}

	@Override
	public int compareTo(Customer o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public String toHtmlDoc() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Customer: " + this.name + "</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(this.id + " " + this.name + " " + this.phoneNumber + " "
				+ this.address);
		sb.append("</body></html>");
		return sb.toString();
	}
}
