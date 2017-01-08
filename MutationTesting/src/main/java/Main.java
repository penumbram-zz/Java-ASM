public class Main {

	public static void main(String[] args){
	}
	
	public int Method1(){
		int result = 2;
		
		result += 5;
		
		return result;
	}

	public int Method2(){
		int result;
		int a = 4;
		result = a * 2;
		
		return result;
	}
	
	public boolean Method3(){		
		int number1 = 5;
		int number2 = 6;
		
		if(number1 == number2){
			return true;
		}
		
		return false;
	}
	
	public boolean Method4(boolean value1, boolean value2){
		if (value1 && value2) {
			return true;
		}
		return false;
	}


}
