package main;

import java.util.ArrayList;
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

	/**
	 * accept call & allocate to an available fresher or available technical lead
	 * @param call
	 * @return
	 */
	public boolean inCall(Call call) {
		try {
			employees.parallelStream().filter(s -> s.available()).findAny().orElse(null).handlingCall(call);
		} catch (NullPointerException e) {
			System.out.println("No fresher is available now ,so escalate the call to " + call.toString());
			technicalLead.handlingCall(call);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static class CallCenterBuilder {
		private Employee productManager = new Employee(1, "ProductManager");
		private Employee technicalLead = new Employee(1, "TechnicalLead");
		private List<Employee> employees = new ArrayList<Employee>();

		public CallCenterBuilder setProductManager(Employee productManager) {
			this.productManager = productManager;
			return this;
		}

		public CallCenterBuilder setTechnicalLead(Employee technicalLead) {
			this.technicalLead = technicalLead;
			return this;
		}

		public CallCenterBuilder setFreshers(List<Employee> freshers) {
			this.employees.addAll(freshers);
			return this;
		}

		public CallCenter build() {
			this.technicalLead.setSupervisor(productManager);
			if (employees.size() == 0) {
				employees.add(new Employee(1, "Fresher"));
			}
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