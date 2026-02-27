package battleship;

import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);


    static void main(String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();

        System.out.println("Player 1, place your ships on the game field");
        System.out.println();
        printMatrix(player1.ownField);
        placeShip("Aircraft Carrier",5,player1.ownField);
        placeShip("Battleship",4,player1.ownField);
        placeShip("Submarine",3,player1.ownField);
        placeShip("Cruiser",3,player1.ownField);
        placeShip("Destroyer",2,player1.ownField);
        System.out.println();// get the \n before Press enter
        passMove();

        System.out.println("Player 2, place your ships on the game field");
        System.out.println();
        printMatrix(player2.ownField);

        placeShip("Aircraft Carrier",5,player2.ownField);
        placeShip("Battleship",4,player2.ownField);
        placeShip("Submarine",3,player2.ownField);
        placeShip("Cruiser",3,player2.ownField);
        placeShip("Destroyer",2,player2.ownField);
        System.out.println();
        passMove();


        while (true) {


            printBothFields(player1);

            System.out.println("Player 1, it's your turn:");

            startShooting(player1, player2);
            if (player2.cellsLeft == 0) {
                System.out.println();

                System.out.println("You sank the last ship. You won. Congratulations!");
                System.out.println();

                break;
            }
            passMove();
            printBothFields(player2);

            System.out.println("Player 2, it's your turn:");



            startShooting(player2, player1);
            if (player1.cellsLeft == 0) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                System.out.println();

                break;
            }

            passMove();
        }


        //startShooting(hidedMatrix,matrix,1);

        sc.close();

    }
    public static void printBothFields(Player player) {

        printMatrix(player.fogField);

        System.out.println("---------------------");

        printMatrix(player.ownField);
        System.out.println();
    }

    public static void passMove() {
        System.out.println("Press Enter and pass the move to another player");
        sc.nextLine();

    }

    public static void startShooting(Player current, Player opponent)
    {
        while(true) {
            String shot = sc.nextLine().trim().toUpperCase();

            if (shot.length() < 2) {


                System.out.println("Error! You entered the wrong coordinates! Try again:");


                continue;
            }

            char row = shot.charAt(0);
            if (row < 'A' || row > 'J') {


                System.out.println("Error! You entered the wrong coordinates! Try again:");


                continue;
            }
            int startCol;

            try {
                startCol = Integer.parseInt(shot.substring(1));
            } catch (NumberFormatException e) {


                System.out.println("Error! You entered the wrong coordinates! Try again:");


                continue;
            }

            if (startCol < 1 || startCol > 10) {


                System.out.println("Error! You entered the wrong coordinates! Try again:");


                continue;
            }


            int startRow = row - 'A' + 1;

            if(current.fogField[startRow][startCol].equals("M ") ){

                System.out.println("You missed!");

                break;

            }else if (current.fogField[startRow][startCol].equals("X ") ){


                System.out.println("You hit a ship!");



                break;

            }

            if (!isShipNotHere(opponent.ownField, startRow, startCol)) { // true , part of the ship is here !
                current.fogField[startRow][startCol] = "X ";
                opponent.ownField[startRow][startCol] ="X ";



                if(isBoatPartNear(opponent.ownField,startRow,startCol)){

                    System.out.println("You hit a ship!");
                    opponent.cellsLeft --;

                    break;

                }

                else{
                    opponent.shipsLeft--;
                    opponent.cellsLeft --;
                    if(opponent.cellsLeft  == 0 ){
                        //System.out.println("You sank the last ship. You won. Congratulations!");
                        break;
                    }

                    System.out.println("You sank a ship!");
                    break;
                }

                //printMatrix(matrix)
            }
            else {
                current.fogField[startRow][startCol] = "M ";
                //opponent.ownField[startRow][startCol] = "M ";



                System.out.println("You missed!");
                break;
            }

        }
    }
    public static boolean isShipNotHere(String[][] matrix,
                                        int row, int col){

       return !matrix[row][col].equals("O ");

    }
    public static boolean isAreaFree(String[][] matrix,
                                     int rowStart, int rowEnd,
                                     int colStart, int colEnd) {

        int checkRowStart = Math.max(1, rowStart - 1);
        int checkRowEnd   = Math.min(10, rowEnd + 1);

        int checkColStart = Math.max(1, colStart - 1);
        int checkColEnd   = Math.min(10, colEnd + 1);

        for (int i = checkRowStart; i <= checkRowEnd; i++) {
            for (int j = checkColStart; j <= checkColEnd; j++) {
                if (matrix[i][j].equals("O ")) {
                    return false;
                }
            }
        }

        return true;
    }
    public static boolean isBoatPartNear(String[][] matrix, int row, int col) {
        // Check all 4 directions: Up, Down, Left, Right
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) { // if row= 1 , col = 1
            int currentRow = row + dRow[i]; // 2
            int currentCol = col + dCol[i]; // 1

            // Keep walking in this direction as long as we see 'X' (hits)
            while (currentRow >= 1 && currentRow <= 10 &&
                    currentCol >= 1 && currentCol <= 10 &&
                    matrix[currentRow][currentCol].equals("X ")) {
                currentRow += dRow[i];
                currentCol += dCol[i];
            }

            // After passing all 'X's, if we hit an 'O', the ship is still alive!
            if (currentRow >= 1 && currentRow <= 10 &&
                    currentCol >= 1 && currentCol <= 10 &&
                    matrix[currentRow][currentCol].equals("O ")) {
                return true;
            }
        }

        // If we checked all 4 directions and never hit an 'O', the ship is sunk.
        return false;
    }
    public static void placeShip(String name , int cells , String[][] matrix) {
        System.out.println();
        System.out.printf("Enter the coordinates of the %s (%d cells):",name,cells);
        System.out.println();

        System.out.println();

        while (true) {

            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            char startRow = Character.toUpperCase(parts[0].charAt(0));
            int startCol = Integer.parseInt(parts[0].substring(1));

            char endRow = Character.toUpperCase(parts[1].charAt(0));
            int endCol = Integer.parseInt(parts[1].substring(1));
            int length = length0fShip(startRow, startCol, endRow, endCol);

            if(length == -1) {
                System.out.println();
                System.out.println("Error! Wrong ship location! Try again:");
                System.out.println();
                continue;

            }
            if (length != cells ) {
                System.out.println();

                System.out.printf("Error! Wrong length of the %s! Try again:\n",name);
                System.out.println();

                continue;
            }


            if (startCol == endCol) {

                int firstRow = Math.min(startRow, endRow) - 'A' + 1; // gives alpabetic order ASCII table, No# Letter
                int lastRow = Math.max(startRow, endRow) - 'A' + 1;
                if (!isAreaFree(matrix, firstRow, lastRow, startCol, endCol)) {
                    System.out.println();

                    System.out.println("Error! You placed it too close to another one. Try again:");
                    System.out.println();


                }
                else {
                    for (int i = firstRow; i <= lastRow; i++) {
                        matrix[i][startCol] = "O ";
                    }
                    break;
                }


            } else if (startRow == endRow) {
                int rowIndex = startRow - 'A' + 1;
                int start = Math.min(startCol, endCol);
                int end = Math.max(startCol, endCol);
                if (!isAreaFree(matrix, rowIndex, rowIndex, start, end)) {
                    System.out.println();

                    System.out.println("Error! You placed it too close to another one. Try again:");
                    System.out.println();


                }
                else {
                    for (int i = start; i <= end; i++) {
                        matrix[rowIndex][i] = "O ";
                    }
                    break;
                }
            }



        }
        System.out.println();
        printMatrix(matrix);


    }

    public static void printMatrix(String[][] matrix) {
        //System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf(matrix[i][j]);

            }
            System.out.println();
        }
    }


    public static int length0fShip(char startRow, int startCol, char endRow, int endCol) {

        if (startRow == endRow) { // horizontal
            int start = Math.min(startCol, endCol);
            int end = Math.max(startCol, endCol);

            return end - start + 1;


        }
        else if (startCol == endCol) {


            int firstRow = Math.min(startRow, endRow) - 'A' + 1; // gives alpabetic order ASCII table
            int lastRow = Math.max(startRow, endRow) - 'A' + 1;

            return lastRow - firstRow + 1;

        } else if (startCol != endCol && startRow != endRow) {

            return -1;// error

        }
        return -1;
    }
    public static void fillMatrix( String[][] matrix){
        String[] letters = {"A", "B", "C","D", "E","F","G","H","I","J"};
        for(int i =0; i < matrix.length ; i++){

            for(int j =0 ; j < matrix[i].length; j ++){
                if(i ==0 ){
                    if(j ==0 ) {
                        matrix[i][j] = "  ";
                    }
                    else{

                        matrix[i][j] =  j + " ";
                    }
                }
                else if(j==0){

                    matrix[i][j] = letters[i-1] + " ";


                }
                else{
                    matrix[i][j] = "~ ";
                }



            }
        }

    }
}
class Player {

    String[][] ownField = new String[11][11];
    String[][] fogField = new String[11][11]; //the oppenet's field
    int shipsLeft = 5;
    int cellsLeft = 17;

    public Player() {
        Main.fillMatrix(ownField);
        Main.fillMatrix(fogField);
    }
}


