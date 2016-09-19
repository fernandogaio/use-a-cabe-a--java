import java.io.*;

public class SimpleDotComGame {
	public static void main (String[] args){
		int numOfGuesses = 0;
		GameHelper helper = new GameHelper();
		SimpleDotCom dot = new SimpleDotCom();
		int randomNum = (int) (Math.random() * 5);
		int locations [] = {randomNum, randomNum + 1, randomNum + 2};
		dot.setLocationCells(locations) ;
		boolean isAlive = true;

		while (isAlive == true) {
			String guess = helper.getUserInput ("insira um numero");
			String result = dot.checkYorself(guess);
			numOfGuesses++;

			if (result.equals("eliminacao")) {
				isAlive = false;
				System.out.println("Voce usou " + numOfGuesses + " palpites");
			}
		}
	}
}

class SimpleDotCom {
	private int[] locationCells;
	private int numOfHits = 0;

	public String checkYorself(String userGuess) {
		int guess = Integer.parseInt(userGuess);
		String result = "errado";
		for (int cell : locationCells) {
			if(guess == cell) {
				result = "acerto";
				numOfHits++;
				break;
			}
		} 
		if(numOfHits == locationCells.length) {
			result = "eliminacao";
		}
		System.out.println(result);
		return result;
	}
	public void	setLocationCells(int[] cellLocations) {
		locationCells = cellLocations;
	}
}

class GameHelper {
	public String getUserInput(String prompt) {
		String inputLine = null;
		System.out.print(prompt + " ");
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
			inputLine = is.readLine();
			if (inputLine.length() == 0) return null;
		} catch (IOException e){
			System.out.println("IOException: " + e);
		}
		return inputLine;
	}
}