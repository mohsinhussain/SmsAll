package Classes;

public class ContactDetails {
	 String contactname = null;
	 String contactnumber = null;
	 boolean selected = false;
	  
	 public ContactDetails(String name, String number, boolean selected) {
	  super();
	  this.contactname = name;
	  this.contactnumber = number;
	  this.selected = selected;
	 }
	  
	 public String getcontactname() {
	  return contactname;
	 }
	 public void setcontactname(String code) {
	  this.contactname = code;
	 }
	 public String getcontactnumber() {
	  return contactnumber;
	 }
	 public void setcontactnumber(String name) {
	  this.contactnumber = name;
	 }
	 
	 public boolean isSelected() {
	  return selected;
	 }
	 public void setSelected(boolean selected) {
	  this.selected = selected;
	 }
	  
	}
