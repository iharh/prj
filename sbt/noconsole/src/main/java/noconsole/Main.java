package noconsole;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
	public static void main(String [] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (int i = 0; i < 3; ++i)
			iteration(i, br);
	}

	private static void iteration(int num, BufferedReader br) {
		System.out.println("enter line " + num);

		// !!! Creating BufferedReader each time causes invalid stream manipulations for piped input !!!
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String result = null;
		try {
			result = br.readLine();
			System.out.println("!!! readen value: " + result);
		}
		catch (IOException ioe) {
			System.out.println("IOException caught!!!");
		}
	}
}

