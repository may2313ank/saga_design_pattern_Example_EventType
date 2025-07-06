package com.example.event;

public class OrderEvent {
	private Long orderId;
    private String eventType; // e.g. ORDER_CREATED, PAYMENT_SUCCESS, PAYMENT_FAILED
	
    public OrderEvent() {
		super();
	}
	public OrderEvent(Long orderId, String eventType) {
		super();
		this.orderId = orderId;
		this.eventType = eventType;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
    
    
}
