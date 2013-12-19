/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsis;

/**
 * 
 * @author hieudt
 */
public class HighEarner extends Customer implements Document {
	@DomainConstraint(type = "float", mutable = true, optional = false, min = 10000000)
	private float income;

	/**
	 * Constructor of HighEarner class
	 * 
	 * @param name
	 * @param phoneNumber
	 * @param adress
	 * @param income
	 */
	public HighEarner(int id, String name, String phoneNumber, String adress,
			float income) {
		super(id, name, phoneNumber, adress);
		this.income = income;
	}

	/**
	 * @return this income
	 */
	public float getIncome() {
		return this.income;
	}

	@Override
	public String toString() {
		return "HighEarner:<" + getId() + ", " + getName() + ", "
				+ getPhoneNumber() + ", " + getAddress() + ", " + income + ">";
	}

	@Override
	public String toHtmlDoc() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>HighEarner: " + this.getName() + "</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(this.getId() + " " + this.getName() + " "
				+ this.getPhoneNumber() + " " + this.getAddress() + " "
				+ this.getIncome());
		sb.append("</body></html>");
		return sb.toString();
	}

}
