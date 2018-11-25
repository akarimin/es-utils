package edu.akarimin.esutils.commons;

import java.util.Scanner;

/**
 * Created by akarimin on 11/1/17.
 */
public final class ScannerUtil {

	private static final Scanner SCANNER = new Scanner(System.in);

	public static Object fetchConsoleInput(String title, String preInputMessage) {
		System.out.println("#######################################---" + title
				+ "---#####################################\n" + preInputMessage);
		return SCANNER.nextLine();
	}

}
