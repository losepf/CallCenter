package com.callcenter.employee;

import java.util.concurrent.atomic.AtomicBoolean;

import com.callcenter.Call;
import com.callcenter.helper.SolveDiceRollHelper;

public class Employee {
	private String employeeLevel;
	private int employeeId;
	private Employee supervisor;
	private AtomicBoolean available = new AtomicBoolean(true);
	private Call handlingCall;

	public Employee() {

	}

	public Employee(int id, String level) {
		this.employeeId = id;
		this.employeeLevel = level;
	}

	/**
	 * handling call by dice doll & check availability
	 * 
	 * @param handlingCall
	 */
	public synchronized void handlingCall(Call handlingCall) {
		if (available()) {
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
				escalate(handlingCall, getSupervisor(), this);
			}

		} else {
			System.out.println(
					employeeLevel + " no:" + employeeId + " isn't available the call " + this.handlingCall.toString());
			System.out.println(
					employeeLevel + " no:" + employeeId + " escalate the call " + this.handlingCall.toString());
			setAvailable();
			escalate(handlingCall, getSupervisor(), this);
		}
	}

	/**
	 * escalate unresolvable call
	 * 
	 * @param transitCall
	 * @param transfer
	 * @param transfee
	 */
	public void escalate(Call transitCall, Employee transfer, Employee transfee) {
		if (null == transfer) {
			synchronized (this) {
				System.out.println(transfee.getEmployeeLevel() + " no:" + transfee.getEmployeeId()
						+ " can't escalate the call " + transitCall.toString());
				transfee.available.set(true);
				transfee.handlingCall = null;
				transitCall.setSolved(true);
			}
		} else {
			if (transfer.available()) {
				synchronized (this) {
					transfer.available.set(true);
					transfer.handlingCall = null;
					transfer.handlingCall(transitCall);
				}
			} else {
				System.out.println(transfer.getEmployeeLevel() + " no:" + transfer.getEmployeeId()
						+ " isn't available the call " + transitCall.toString());
				System.out.println(transfer.getEmployeeLevel() + " no:" + transfer.getEmployeeId()
						+ " escalate the call " + transitCall.toString());
				escalate(transitCall, transfer.getSupervisor(), transfer);
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

	public void setSupervisor(Employee employee) {
		this.supervisor = employee;
	}

	public Employee getSupervisor() {
		return supervisor;
	}

	public String getEmployeeLevel() {
		return employeeLevel;
	}

	public void setEmployeeLevel(String employeeLevel) {
		this.employeeLevel = employeeLevel;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

}