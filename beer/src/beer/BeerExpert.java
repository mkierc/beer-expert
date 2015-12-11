package beer;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import alice.tuprolog.*;

public class BeerExpert {
	
	static final String SMAK = "smak";
	static final String AROMAT = "aromat";
	static final String BARWA = "barwa";
	static final String GORYCZKA = "gorycz";
	
	static String SPLITTER = ",";
	
	static String[] params = {SMAK,AROMAT,BARWA,GORYCZKA};
	
	static String[] smaki;
	static String[] aromaty;
	static String[] barwy;
	static String[] goryczki;
	
	static String preAssert = "assertz(";
	static String suffAssert = ")).";

	static String[] results = { "beerstyle(X).","category(X).","lager.","bitter." };

	public static void main(String[] args) {
		System.out.println("START");
		Scanner scanner = new Scanner(System.in);
		
		for(String param : params){
			System.out.println("Parametr: "+param);
			String[] val = scanner.nextLine().split(SPLITTER);
			switch(param){
				case SMAK:
					smaki = val;
					break;
				case AROMAT:
					aromaty = val;
					break;
				case BARWA:
					barwy = val;
					break;
				case GORYCZKA:
					goryczki = val;
			}
		}
		
		Prolog engine = new Prolog();

		try {
			Theory beers = new Theory(new FileInputStream("beer.pl"));
			engine.setTheory(beers);
			for(String arg : smaki){
				SolveInfo info = engine.solve(assertz(SMAK,arg));
				if (info.isSuccess()) {
					System.out.println(SMAK+" OK: " + arg);
				}
			}
			for(String arg : barwy){
				SolveInfo info = engine.solve(assertz(BARWA,arg));
				if (info.isSuccess()) {
					System.out.println(BARWA+" OK: " + arg);
				}
			}
			for(String arg : aromaty){
				SolveInfo info = engine.solve(assertz(AROMAT,arg));
				if (info.isSuccess()) {
					System.out.println(AROMAT+" OK: " + arg);
				}
			}
			for(String arg : goryczki){
				SolveInfo info = engine.solve(assertz(GORYCZKA,arg));
				if (info.isSuccess()) {
					System.out.println(GORYCZKA+" OK:"+ arg);
				}
			}
			
			System.out.println("\nRESULTS\n");
			for (String arg : results) {
				SolveInfo info = engine.solve(arg);
				if (info.isSuccess()) {
					System.out.println("OK: " + arg);
					if(arg.startsWith("beer")){
						System.out.println(info.getTerm("X"));
						while(engine.hasOpenAlternatives()){
							info = engine.solveNext();
							if(info!=null&&info.isSuccess()){
								System.out.println(info.getTerm("X"));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String assertz(String type,String val){
		return preAssert+type+"("+val+suffAssert;
	}
}