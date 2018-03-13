package com.callcenter.helper;

import java.util.Random;

public class SolveDiceRollHelper {
	public static boolean checkSolved() {
		try {
			// random solved time
			Thread.sleep(new Integer(new Random().nextInt(3) * 10).longValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Random().nextInt(100) > 50;
	}
}