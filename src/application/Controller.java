package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;

public class Controller {
	
	String regexpUNIQUE = "";
	String even="";
	String format;
	String separator;
	
	/*
	 * Integer = the year
	 * HashMap = for a given year contains 
	 * 				Integer = month
	 * 				ArrayList = list of exception days
	 * */
	HashMap<Integer, HashMap<Integer,ArrayList<Integer>>> excMap =new HashMap<>();
	
	ArrayList<Integer> alreadyPresentDayRegExp = new ArrayList<>();
	
	 HashMap<Integer,Integer> month_day = new HashMap<Integer,Integer>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		 	put(1, 31);
			put(2, 28);
			put(3, 31);
			put(4, 30);
			put(5, 31);
			put(6, 30);
			put(7, 31);
			put(8, 31);
			put(9, 30);
			put(10, 31);
			put(11, 30);
			put(12, 31);
	 }};
	
	 String divisor;
	 
	 public void setEven(){
		 even="even";
	 }
	 
	 public void setNormal(){
		 even="normal";
	 }
	 public void setOdd(){
		 even="odd";
	 }
	 
	 public void setFormat(String format){
		 this.format=format;
	 }
	 
	 public void setSeparator(String separator){
		 this.separator=separator;
	 }
			 
	void Add(String eyy, String emm, String edd) {
		
		String empty = new String("");
		
		if(!eyy.equals(empty)&&!emm.equals(empty)&&!edd.equals(empty)){
			if(eyy.matches("\\d+")&&emm.matches("\\d+")&&edd.matches("\\d+")){
		        if(!excMap.containsKey(Integer.parseInt(eyy))){
		        	excMap.put(Integer.parseInt(eyy), new HashMap<Integer,ArrayList<Integer>>());
		        	//System.out.println("created hashmap");
		        
		        }
	        	if(excMap.get(Integer.parseInt(eyy)).containsKey(emm)){
	        		//System.out.println("adding to arr");
	        		excMap.get(Integer.parseInt(eyy)).get(Integer.parseInt(emm)).add(Integer.parseInt(edd));
	        	}
	        	else{ 
	        		excMap.get(Integer.parseInt(eyy)).put(Integer.parseInt(emm) , new ArrayList<Integer>());
	        		excMap.get(Integer.parseInt(eyy)).get(Integer.parseInt(emm)).add(Integer.parseInt(edd));
	        		//System.out.println("creating arr"+excMap.get(Integer.parseInt(eyy)).get(Integer.parseInt(emm)));
	        	}
		        
		        
			}
		}
		
		
    }
	
	
	
	public String Eval(String fyy, String fmm, String fdd, String tyy, String tmm, String tdd) throws InterruptedException {

		alreadyPresentDayRegExp = new ArrayList<>();
		String result="ERR Eval";

		
		String empty = new String("");
		String solution = "";
		
		int i;
		int unique=1;
		
//		System.out.println(fyy);
//		System.out.println(fmm);
//		System.out.println(fdd);
//		System.out.println(tyy);
//		System.out.println(tmm);
//		System.out.println(tdd);
		
		divisor="\""+separator+"\"";
		
		if(!fyy.equals(empty)&&!fmm.equals(empty)&&!fdd.equals(empty)&&
				!tyy.equals(empty)&&!tmm.equals(empty)&&!tdd.equals(empty)){
				
			if(fyy.matches("\\d+")&&fmm.matches("\\d+")&&fdd.matches("\\d+")&&
					tyy.matches("\\d+")&&tmm.matches("\\d+")&&tdd.matches("\\d+")){
					
		        //System.out.println("im in");
				Integer fy = Integer.parseInt(fyy);
				Integer fm = Integer.parseInt(fmm);
				Integer fd = Integer.parseInt(fdd);
				
				Integer ty = Integer.parseInt(tyy);
				Integer tm = Integer.parseInt(tmm);
				Integer td = Integer.parseInt(tdd);
				
				if(fyy.equals(tyy)){//on the same year
					result=evaluateYear(fy, new ArrayList<Integer>(), fm, tm, fd, td, excMap);
					
				}
				else{
					if(ty==fy+1){
						regexpUNIQUE="1";
						//MOD
						String partial = evaluateYear(fy, new ArrayList<Integer>(), fm, 12, fd, 31, excMap);
						regexpUNIQUE="2";
						
						partial+="\n\n"+evaluateYear(ty, new ArrayList<Integer>(), 1, tm, 1, td, excMap);
						solution = "date={date1}|{date2}";
						result=partial+"\n\n"+solution;
					}
					else if(ty>fy+1){
						
						ArrayList<Integer> bissextile = new ArrayList<Integer>() ;
						
						regexpUNIQUE=Integer.toString(unique++);
						
						String partial = evaluateYear(fy, new ArrayList<Integer>(), fm, 12, fd, 31, excMap);
						ArrayList<Integer> years = new ArrayList<Integer>();
						for(i=fy+1;i<=ty-1;i++){
							if(!excMap.containsKey(i))
								if(bissextile(i))
									bissextile.add(i);
								else
									years.add(i);
							//System.out.println(i+" "+years.size()+" "+years.get(0));
							else{
								regexpUNIQUE=Integer.toString(unique++);
								
								partial+="\n\n"+evaluateYear(i, new ArrayList<Integer>(), 1, 12, 1, 31, excMap);
							}
							
						}
						if(!bissextile.isEmpty()){
							regexpUNIQUE=Integer.toString(unique++);
							
							partial+="\n\n"+evaluateYear(bissextile.remove(0), bissextile, 1, 12, 1, 31, excMap);
						}
						regexpUNIQUE=Integer.toString(unique++);
						
						partial+="\n\n"+evaluateYear(years.remove(0), years, 1, 12, 1, 31, excMap);
						regexpUNIQUE=Integer.toString(unique);
						
						partial+="\n\n"+evaluateYear(ty, new ArrayList<Integer>(), 1, tm, 1, td, excMap);
						solution="date=";
						for(i=1;i<=unique;i++)
							solution += "{date"+i+"}|";
						solution=solution.substring(0, solution.length()-1);
						result=partial+"\n\n"+solution;
					}
				}
				
			}
		}
		return result;
		
    }
	/*
	 *	year = the year for which evaluate the months
	 * 	fmm = from month
	 * 	tmm = to month
	 *  fdd = from day
	 *  tdd = to day
	 *  HashMap = map containing exceptions
	 */
	private String evaluateYear(int year, ArrayList<Integer> yeararr, int fmm, int tmm, int fdd, int tdd, HashMap<Integer, HashMap<Integer,ArrayList<Integer>> > exc ) throws InterruptedException{
		HashMap<Integer, ArrayList<Integer>> partials = new HashMap<>();
		HashMap<Integer, String> excSol = new HashMap<>();
		ArrayList<Integer> earr; //array to store temporary the exceptions for a month
		String result="";
		
		int dim;
		int i,j;
		int tday,fday;
		int cs; //0 even - 1 odd - >1 normal
		
		if(even.equals("normal"))
			cs=2;
		else if(even.equals("even"))
			cs=0;
		else 
			cs=1;
		
		String solution = new String("");
		if(fmm==tmm&&yeararr.isEmpty()){
			//only 1 month
			if(exc.containsKey(year)&&exc.get(year).containsKey(fmm)){ 
				//there is an exception for the given month of the given year
				 earr=exc.get(year).get(fmm);
				 Collections.sort(earr);
				 dim=earr.size();
				 for(i=0;;i++){
					 if(earr.get(i)!=1){
						 if(fdd>10)
							 solution+="|"+eval(fdd,earr.get(i)-1,cs);
						 else 
							 solution+="|0"+eval(fdd,earr.get(i)-1,cs); 
					 }
					 if(i+1<dim){//there is another exception later
						 if(earr.get(i)>10)
							 solution+="|"+eval(earr.get(i)+1,earr.get(i+1)-1,cs);
						 else
							 solution+="|0"+eval(earr.get(i)+1,earr.get(i+1)-1,cs);
					 }
					 else if(i+1>=dim){
						 if (tdd > earr.get(i)+1)
							 if(earr.get(i)+1>10)
								 solution+="|"+eval( earr.get(i)+1,tdd,cs);
							 else
								 solution+="|0"+eval( earr.get(i)+1,tdd,cs);
						 //else already done before
						 break;
					 }
				 }
			}
			else 
				if(fdd<10)
				solution+="0"+eval(fdd, tdd, cs);
				else 
					solution+=eval(fdd, tdd, cs);
			switch (format) {
			case "YY/MM/DD":
				if(yeararr.isEmpty())//MOD
					result="month_day"+regexpUNIQUE+"="+fmm+divisor+solution+"\ndate"+regexpUNIQUE+"="
								+year+divisor+"{month_day"+regexpUNIQUE+"}";
				else{
					String years=Integer.toString(year)+"|";
					for(Integer y : yeararr){
						if(y!=year)
							years+=y+"|";
					}
					years=years.substring(0, years.length()-1);
					result="month_day"+regexpUNIQUE+"="+fmm+divisor+solution+"\ndate"+regexpUNIQUE+"="
							+years+divisor+"{month_day"+regexpUNIQUE+"}";
				}
				break;
				
			case "DD/MM/YY"://MOD
				result="day_month"+regexpUNIQUE+"="+solution+divisor+fmm+"\ndate"+regexpUNIQUE+"="+"{day_month"+regexpUNIQUE+"}"+divisor+year;
				break;

			default:
				break;
			}
		}
		else{
			//more then 1 month
			for(i=fmm;i<tmm+1;i++){
				
				solution="|";
				if(i!=tmm)
					tday=month_day.get(i);
				else
					tday=tdd;
				
				if(i!=fmm) 
					fday=1;
				else 
					fday=fdd;
				
				if(i==2){
					if(bissextile(year))//checked
						tday=29;
					else
						tday=28;
				}
			
				if(exc.containsKey(year)&&exc.get(year).containsKey(i)){ 
					//there is an exception for the given month of the given year
					 
					 earr=exc.get(year).get(i);
					 Collections.sort(earr);
					 dim=earr.size();
					 for(j=0;;j++){
						 if(earr.get(j)!=1){
							 if(fday<10)
								 solution+="|0"+eval(fday,earr.get(j)-1,cs);
							else 
								 solution+="|"+eval(fday,earr.get(j)-1,cs);
						 }
							 
						 if(j+1<dim){//there is another exception later
							 if(earr.get(j)+1<10)
								 solution+="|0"+eval(earr.get(j)+1,earr.get(j+1)-1,cs);
							 else
								 solution+="|"+eval(earr.get(j)+1,earr.get(j+1)-1,cs);
						 }
					
						 else if(j+1>=dim){
							 if (tday > earr.get(j)+1)
								 if(earr.get(j)+1<10)
									 solution+="|0"+eval(earr.get(j)+1,tday,cs);
								 else 
									 solution+="|"+eval( earr.get(j)+1,tday,cs);
							 excSol.put(i, solution.substring(2));
							 
							 break;
						 }
					 }
				}
				else if(i==fmm && fdd!=1){
					if(fdd<10)
						excSol.put(i,"0"+eval(fdd, tday, cs));
					else 
						excSol.put(i,eval(fdd, tday, cs));
				}
				else if (i==tmm && tdd!=month_day.get(i)){
					if(fday<10)
						excSol.put(i,"0"+eval(fday, tdd, cs));
					else
						excSol.put(i,eval(fday, tdd, cs));
				}
				else{
					if(!partials.containsKey(tday))
						partials.put(tday, new ArrayList<Integer>());
					partials.get(tday).add(i);
				}
			}
			//System.out.println(partials);
			//System.out.println(excSol);
			solution="";
			for(Integer d : partials.keySet()){
				if(!alreadyPresentDayRegExp.contains(d)){
					solution+="day"+d+"="+"0"+eval(1,d,cs)+"\n";
					alreadyPresentDayRegExp.add(d);
				}
			}
			switch (format) {
			case "YY/MM/DD":
				solution+="month_day"+regexpUNIQUE+"=";
				for(Integer d : partials.keySet()){
					solution+="(";
					for(Integer m : partials.get(d)){
						solution+=m+"|";
					}
					solution=solution.substring(0, solution.length()-1);
					solution+=")"+divisor+"{day"+d+"}|";
					
				}
				for(Integer d : excSol.keySet()){
					solution+=d+divisor+"("+excSol.get(d)+")"+"|";
				}
				if(yeararr.isEmpty())
					solution=solution.substring(0, solution.length()-1)+"\nyear"+regexpUNIQUE+"="+
								year+"\ndate"+regexpUNIQUE+"={year"+regexpUNIQUE+"}"+divisor+"{month_day"+regexpUNIQUE+"}";
				else{
					String years=Integer.toString(year)+"|";
					for(Integer y : yeararr){
						if(y!=year)
							years+=y+"|";
					}
					years=years.substring(0, years.length()-1);
					solution=solution.substring(0, solution.length()-1)+"\nyear"+regexpUNIQUE+"="+
							years+"\ndate"+regexpUNIQUE+"={year"+regexpUNIQUE+"}"+divisor+"{month_day"+regexpUNIQUE+"}";
					
				}
				
				break;
				
			case "DD/MM/YY":
				solution+="day_month"+regexpUNIQUE+"=";
				for(Integer d : partials.keySet()){
					solution+="{day"+d+"}"+divisor+"(";
					
					for(Integer m : partials.get(d)){
						solution+=m+"|";
					}
					solution=solution.substring(0, solution.length()-1);
					solution+=")|";
					
				}
				for(Integer d : excSol.keySet()){
					solution+="("+excSol.get(d)+")"+divisor+d+"|";
				}
				if(yeararr.isEmpty())
					solution=solution.substring(0, solution.length()-1)+"\nyear"+regexpUNIQUE+"="+
								year+"\ndate"+regexpUNIQUE+"={day_month"+regexpUNIQUE+"}"+divisor+"{year}";
				else{
					String years=Integer.toString(year)+"|";
					for(Integer y : yeararr){
						if(y!=year)
							years+=y+"|";
					}
					years=years.substring(0, years.length()-1);
					solution=solution.substring(0, solution.length()-1)+"\nyear"+regexpUNIQUE+"="+
							years+"\ndate"+regexpUNIQUE+"={day_month"+regexpUNIQUE+"}"+divisor+"{year}";
				}
				
				break;

			default:
				break;
			}
			result=solution;
		}
		
		return result;
		
	}
	
	private String eval(Integer from , Integer to , int cs) throws InterruptedException{
		String result="ERR eval";
		ProcessBuilder pb;
		switch (cs) {
		case 0://even
			pb = new ProcessBuilder("do.sh",from.toString(),to.toString(),"n","0");
			break;
			
		case 1://odd
			pb = new ProcessBuilder("do.sh",from.toString(),to.toString(),"n","1");
			break;		
			
		default:
			pb = new ProcessBuilder("do.sh",from.toString(),to.toString(),"n");
			break;
		}
		Process p;
		try {
			
			pb.directory(new File("./regexp"));
			p = pb.start();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			StringBuilder builder = new StringBuilder();
//			String line = null;
//			while ( (line = reader.readLine()) != null) {
//				System.out.print("line:"+line+"__");
//			   builder.append(line);
//			   //builder.append(System.getProperty("line.separator"));
//			}
//			String result = builder.toString();
//			return result;
			
			IOThreadHandler outhandler = new IOThreadHandler(p.getInputStream());
			outhandler.start();
			p.waitFor();
			result=outhandler.getOutput().toString();
			result=result.substring(2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	
	
	private boolean bissextile(int year){
		if(year%100==0 && year%400!=0)
			return false;
		if(year%4==0)
			return true;
		return false;
	}
}
	