package com.callcenter.employee;

import java.util.concurrent.atomic.AtomicBoolean;

import com.callcenter.Call;
import com.callcenter.helper.SolveDiceRollHelper;

public abstract class Employee {
	protected String employeeLevel;
	protected int employeeId;
	protected Employee supervisor;
	protected AtomicBoolean available = new AtomicBoolean(true);
	protected Call handlingCall;

	public void setSupervisor(Employee employee) {
		this.supervisor = employee;
	}

	public synchronized void handlingCall(Call handlingCall) {
		this.available.set(false);
		this.handlingCall = handlingCall;
		if (SolveDiceRollHelper.checkSolved()) {
			setAvailable();
			handlingCall.setSolved(true);
			System.out.println(employeeLevel + " no:" + employeeId + " solve the call " + handlingCall.toString());
		} else {
			System.out.println(
					employeeLevel + " no:" + employeeId + " can't solve the call " + this.handlingCall.toString());
			System.out.println(
					employeeLevel + " no:" + employeeId + " escalate the call " + this.handlingCall.toString());
			setAvailable();
			escalate(handlingCall);
		}
	}

	public synchronized void escalate(Call transitCall) {
		if (null == this.supervisor) {
			System.out.println(
					employeeLevel + " no:" + employeeId + " can't escalate the call " + transitCall.toString());
			this.available.set(true);
			transitCall.setSolved(true);
			return;
		}
		while (true) {
			if (this.supervisor.available()) {
				setAvailable();
				this.supervisor.handlingCall(transitCall);
				return;
			}
		}
	}

	private void setAvailable() {
		this.handlingCall = null;
		this.available.set(true);
	}

	public boolean available() {
		return this.available.get();
	}
}