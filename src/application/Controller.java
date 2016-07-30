package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;



public class Controller {
	@FXML private TextArea exceptions;
	@FXML private TextField eYY;
	@FXML private TextField eMM;
	@FXML private TextField eDD;
	@FXML private TextField fromYY;
	@FXML private TextField fromMM;
	@FXML private TextField fromDD;
	@FXML private TextField toYY;
	@FXML private TextField toMM;
	@FXML private TextField toDD;
	
	HashMap<Integer, HashMap<Integer,ArrayList<Integer>>> excMap =new HashMap<>();
	
	@FXML protected void Add(ActionEvent event) {
		
		String eyy = new String(eYY.getText());
		String emm = new String(eMM.getText());
		String edd = new String(eDD.getText());
		
		String empty = new String("");
		
		if(!eyy.equals(empty)&&!emm.equals(empty)&&!edd.equals(empty)){
			if(eyy.matches("\\d+")&&emm.matches("\\d+")&&edd.matches("\\d+")){
		        if(exceptions.getText().equals(empty))
					exceptions.setText(
							eyy+"/"+
							emm+"/"+
							edd
			        );
		        else 
		    		exceptions.setText(
		    				exceptions.getText()+"\n"+
		    				eyy+"/"+
		    				emm+"/"+
		    				edd
		            );
		        if(!excMap.containsKey(Integer.parseInt(eyy))){
		        	excMap.put(Integer.parseInt(eyy), new HashMap<Integer,ArrayList<Integer>>());
		        	System.out.println("created hashmap");
		        
		        }
	        	if(excMap.get(Integer.parseInt(eyy)).containsKey(emm)){
	        		System.out.println("adding to arr");
	        		excMap.get(Integer.parseInt(eyy)).get(Integer.parseInt(emm)).add(Integer.parseInt(edd));
	        	}
	        	else{ 
	        		excMap.get(Integer.parseInt(eyy)).put(Integer.parseInt(emm) , new ArrayList<Integer>());
	        		excMap.get(Integer.parseInt(eyy)).get(Integer.parseInt(emm)).add(Integer.parseInt(edd));
	        		System.out.println("creating arr"+excMap.get(Integer.parseInt(eyy)).get(Integer.parseInt(emm)));
	        	}
		        
		        
			}
		}
    }
	
	@FXML protected void Clear(ActionEvent event) {
		exceptions.setText("");
    }
	
	@FXML protected void Eval(ActionEvent event) {

		String fyy = new String(fromYY.getText());
		String fmm = new String(fromMM.getText());
		String fdd = new String(fromDD.getText());
		
		String tyy = new String(toYY.getText());
		String tmm = new String(toMM.getText());
		String tdd = new String(toDD.getText());
		
		ArrayList<String> exceptions;
		
		String empty = new String("");
		
		
		if(!fyy.equals(empty)&&!fmm.equals(empty)&&!fdd.equals(empty)&&
				!tyy.equals(empty)&&!tmm.equals(empty)&&!tdd.equals(empty)){
			if(fyy.matches("\\d+")&&fmm.matches("\\d+")&&fdd.matches("\\d+")&&
					tyy.matches("\\d+")&&tmm.matches("\\d+")&&tdd.matches("\\d+")){
		        System.out.println("im in");
				if(fyy.equals(tyy)){//on the same year
					String year = fyy;
					evaluateYear(Integer.parseInt(fyy), Integer.parseInt(fmm), Integer.parseInt(tmm), Integer.parseInt(fdd), Integer.parseInt(tdd), excMap);
					
				}
				else{
					
				}
			}
		}
		
    }
	
	private void evaluateYear(int year, int fmm, int tmm, int fdd, int tdd, HashMap<Integer, HashMap<Integer,ArrayList<Integer>> > exc ){
	
		ArrayList<Integer> earr;
		int dim;
		int i;
		String solution = new String("");
		if(fmm==tmm){
			if(exc.containsKey(year)&&exc.get(year).containsKey(fmm)){
				
				 earr=exc.get(year).get(fmm);
				 Collections.sort(earr);
				 dim=earr.size();
				 for(i=0;;i++){
					 if(earr.get(i)!=1)
						 solution+=eval(fdd,earr.get(i)-1,2);
					 if(i+1<dim)//there is another exception later
						 solution+=eval(earr.get(i)+1,earr.get(i+1)-1,2);
					 else if(i+1>=dim){
						 if (tdd > earr.get(i)+1)
							 solution+=eval( earr.get(i)+1,tdd,2);
						 //else already done before
						 break;
					 }
				 }
			}
			else solution+=eval(fdd, tdd, 2);
		}
		System.out.println(solution);
		
		
	}
	
	private String eval(Integer from , Integer to , int cs){

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
			BufferedReader reader = 
            new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ( (line = reader.readLine()) != null) {
			   builder.append(line);
			   //builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
			return result.substring(2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}

}
