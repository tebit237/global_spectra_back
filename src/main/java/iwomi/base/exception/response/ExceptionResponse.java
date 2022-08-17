package iwomi.base.exception.response;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionResponse {
	private Date timeStamp;
	private String message;
	private String details;
	private String statusEntity;
	
	public ExceptionResponse(Date timeStamp, String message, String details, String statusEntity) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
		this.statusEntity = statusEntity;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getStatusEntity() {
		return statusEntity;
	}

	public void setStatusEntity(String statusEntity) {
		this.statusEntity = statusEntity;
	}
	
}
