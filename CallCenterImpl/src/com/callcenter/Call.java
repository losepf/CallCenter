package com.callcenter;

public class Call {
	private int callId;
	private boolean solved = false;

	public Call(int callId) {
		this.callId = callId;
	}

	public int getCallId() {
		return callId;
	}

	public void setCallId(int callId) {
		this.callId = callId;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	@Override
	public String toString() {
		return "Call [callId=" + callId + "]";
	}

}
