/**
 * 
 */
package com.smart.model;

/**
 * @author Adnan
 *
 */
public class EmailSend {
  private String to="";
  private String subject="";
  private String message="";
public String getTo() {
	return to;
}
public void setTo(String to) {
	this.to = to;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
@Override
public String toString() {
	return "EmailSend [to=" + to + ", subject=" + subject + ", message=" + message + "]";
}

  
}
