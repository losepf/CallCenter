package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.callcenter.Call;
import com.callcenter.employee.Employee;
import com.callcenter.employee.Fresher;
import com.callcenter.employee.ProductManager;
import com.callcenter.employee.TechnicalLead;

import main.CallCenter;

public class CallCenterTest {

	@Test
	public void test() {
		List<Employee> freshers = new ArrayList<Employee>();
		int fresherCount = 6;
		int inComingCallCount = 1000;
		int threadCountInPool = 10;
		
		for (int i = 1; i <= fresherCount; i++) {
			Fresher fresher = new Fresher(i);
			freshers.add(fresher);
		}
		
		// 1 ProductManager
		// 1 TechnicalLead
		// N Freshers
		CallCenter callCenter = new CallCenter.CallCenterBuilder().setTechnicalLead(new TechnicalLead(1))
				.setProductManager(new ProductManager(1)).setFreshers(freshers).build();
		
        
		ExecutorService executor = Executors.newFixedThreadPool(threadCountInPool);
		List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
		List<Call> calls = new ArrayList<Call>();
		for (int i = 0; i < inComingCallCount; i++) {
			Call call = new Call(i);
			calls.add(call);
			tasks.add(() -> callCenter.inCall(call));
		}

		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			assertTrue(false);
		} finally {
			executor.shutdown();
		}

		long count = calls.parallelStream().filter(s -> s.isSolved()).count();
		assertEquals(new Integer(inComingCallCount).longValue(), count);
	}

}