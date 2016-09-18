package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
	
		BufferedReader br = null;
		String from[];
		String to[];
		String excDiv[];
		Controller ctrl = new Controller();
		int num,days;
		
        try {

            // Refer to this http://www.mkyong.com/java/how-to-read-input-from-console-java/
            // for JDK 1.6, please use java.io.Console class to read system input.
            br = new BufferedReader(new InputStreamReader(System.in));

         

                System.out.print("Enter START date in the format 'Year month day' (SEPARATED by ' '): ");
                String startDate = br.readLine();
                from = startDate.split(" ");
                if(from.length!=3){
                	System.out.println("ERROR IN THE FORMAT");
                	return;
                }

                System.out.print("Enter END date in the format 'Year month day' (SEPARATED by ' '): ");
                String endDate = br.readLine();
                to=endDate.split(" ");
                if(from.length!=3){
                	System.out.println("ERROR IN THE FORMAT");
                	return;
                }

                System.out.println("Enter EXCEPTION dates in the format 'Year month day' (SEPARATED by ' ')");
                System.out.println("(enter 'q' when you have finish):");
                String exc = br.readLine();
                
                //adding exceptions to the list in Controls
                while(!exc.equals("q")){
                	excDiv = exc.split(" ");
                	if(excDiv.length!=3) continue;
                	ctrl.Add(excDiv[0], excDiv[1], excDiv[2]);
                	exc = br.readLine();
                }
                

                System.out.println("Select the type of regexp");
                System.out.println("1 - all the days");
                System.out.println("2 - even days");
                System.out.println("3 - odd days");
                try {days = Integer.parseInt( br.readLine());}
                catch (NumberFormatException e){
                	days=1;
                }
                if(days==1) ctrl.setNormal();
                else if(days==2) ctrl.setEven();
                else if(days==3) ctrl.setOdd();
                
                while(true){
	                System.out.println("Select the format (enter the number):");
	                System.out.println("1 - YY/MM/DD");
	                System.out.println("2 - DD/MM/YY");
	                try {num = Integer.parseInt( br.readLine());}
	                catch (NumberFormatException e){
	                	num=0;
	                }
	                if(num==1){
	                	ctrl.setFormat("YY/MM/DD");
	                	break;
	                }
	                else if(num==2){
	                	ctrl.setFormat("DD/MM/YY");
	                	break;
	                }     
	                else 
	                	System.out.println("WRONG number\n");
                }
                
                
                System.out.print("Enter the separator (for example '/'):");                
                ctrl.setSeparator(br.readLine());	                
	                
                System.out.println("CALCULATING\n\n");
                
	            System.out.println(ctrl.Eval(from[0], from[1], from[2], to[0], to[1], to[2]));
	               
           

        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {			
			e.printStackTrace(); 
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}