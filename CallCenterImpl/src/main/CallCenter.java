package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.callcenter.Call;
import com.callcenter.employee.Employee;

public class CallCenter {
	private Employee productManager;
	private Employee technicalLead;
	private List<Employee> employees;

	public CallCenter() {

	}

	private CallCenter(CallCenterBuilder builder) {
		this.productManager = builder.productManager;
		this.technicalLead = builder.technicalLead;
		this.employees = builder.employees;
	}

	public boolean inCall(Call call) {
		while (true) {
			try {
				employees.parallelStream().filter(s -> s.available()).findAny().orElse(null).handlingCall(call);
				break;
			} catch (NullPointerException e) {				
				System.out.println("No one is available now so recall " + call.toString());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
		return true;
	}

	public static class CallCenterBuilder {
		private Employee productManager;
		private Employee technicalLead;
		private List<Employee> employees;

		public CallCenterBuilder setProductManager(Employee productManager) {
			this.productManager = productManager;
			return this;
		}

		public CallCenterBuilder setTechnicalLead(Employee technicalLead) {
			this.technicalLead = technicalLead;
			return this;
		}

		public CallCenterBuilder setFreshers(List<Employee> freshers) {
			if (null == this.employees) {
				this.employees = Collections.synchronizedList(new ArrayList<Employee>());
			}
			this.employees.addAll(freshers);
			return this;
		}

		public CallCenter build() {
			this.technicalLead.setSupervisor(productManager);
			employees.forEach(s -> s.setSupervisor(technicalLead));
			return new CallCenter(this);
		}

	}

	public Employee getProductManager() {
		return productManager;
	}

	public void setProductManager(Employee productManager) {
		this.productManager = productManager;
	}

	public Employee getTechnicalLead() {
		return technicalLead;
	}

	public void setTechnicalLead(Employee technicalLead) {
		this.technicalLead = technicalLead;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}